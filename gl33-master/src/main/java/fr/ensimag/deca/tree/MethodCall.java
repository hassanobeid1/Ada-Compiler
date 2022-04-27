package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * Method calls
 * 
 * x.method(args..)
 *
 * @author Tanguy Poinson (gl33)
 *
 * @date 08/01/2021
 *
 */
public class MethodCall extends AbstractExpr {
    private AbstractLValue instanceName;
    private FunctionCall functionCall;

    public MethodCall(AbstractLValue instanceName, AbstractIdentifier methodName, ListExpr arguments) {
        Validate.notNull(instanceName);

        this.instanceName = instanceName;
        this.functionCall = new FunctionCall(methodName, arguments);
        this.functionCall.setLocation(methodName.getLocation());
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        ClassType type1 = this.instanceName.verifyExpr(compiler, localEnv, currentClass)
                .asClassType("Seule les classes ont des methodes", instanceName.getLocation());
        Type type2 = this.functionCall.verifyExpr(compiler, localEnv, type1.getDefinition());
        this.setType(type2);
        return type2;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instanceName.decompile(s);
        s.print(".");
        functionCall.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instanceName.prettyPrint(s, prefix, false);
        functionCall.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        instanceName.iter(f);
        functionCall.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister registre) {
        this.functionCall.callMethod(compiler, registre, this.instanceName.getAddr(compiler));
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
