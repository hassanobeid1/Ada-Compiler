package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * Function calls
 * 
 * function(args..)
 *
 * @@author Tanguy Poinson
 *
 * @date 11/01/2021
 */
public class FunctionCall extends AbstractExpr {

    private AbstractIdentifier functionName;
    private ListExpr args;

    public FunctionCall(AbstractIdentifier functionName, ListExpr args) {
        Validate.notNull(functionName);
        Validate.notNull(args);

        this.functionName = functionName;
        this.args = args;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        ExpDefinition expDefinition = currentClass.getMembers().get(functionName.getName());
        if (expDefinition == null || !expDefinition.isMethod()) {
            throw new ContextualError(
                    "Il n'existe pas de methode " + functionName.getName() + " dans la class courante",
                    functionName.getLocation());
        }

        MethodDefinition functionDefinition = (MethodDefinition) expDefinition;

        args.verifyRValueStar(compiler, localEnv, currentClass, functionDefinition.getSignature());

        this.setType(functionDefinition.getType());
        this.functionName.setDefinition(functionDefinition);

        return functionDefinition.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(functionName.getName().toString());
        s.print("(");
        args.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        functionName.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        functionName.iter(f);
        args.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister registre) {
        this.callMethod(compiler, registre, new RegisterOffset(-2, Register.LB));
    }

    public void callMethod(DecacCompiler compiler, GPRegister registre, DAddr obj) {
        MethodDefinition def = this.functionName.getMethodDefinition();
        compiler.addInstruction(new ADDSP(def.getSignature().size() + 1));

        compiler.addInstruction(new LOAD(obj, registre));
        compiler.addInstruction(new STORE(registre, new RegisterOffset(0, Register.SP)));

        for (int i = 0; i < this.args.size(); i++) {
            this.args.getList().get(i).codeGenInst(compiler, registre);
            compiler.addInstruction(new STORE(registre, new RegisterOffset(-1 - i, Register.SP)));
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), registre));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));

        compiler.addInstruction(new LOAD(new RegisterOffset(0, registre), registre));
        compiler.addInstruction(new BSR(new RegisterOffset(def.getIndex() + 1, registre)));

        compiler.addInstruction(new SUBSP(def.getSignature().size() + 1));

        compiler.addInstruction(new LOAD(Register.R0, registre));

        compiler.callFunction(2 + def.getSignature().size() + 1);
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, boolean val, Label label) {
        this.codeGenInst(compiler, Register.R0);
        compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
        if (val) {
            compiler.addInstruction(new BNE(label));
        } else {
            compiler.addInstruction(new BEQ(label));
        }
    }
}
