package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

/**
 * New statement
 *
 * @author Tanguy Poinson
 *
 * @date 11/01/2021
 */
public class New extends AbstractExpr {

    private AbstractIdentifier instanceName;

    public New(AbstractIdentifier instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type = this.instanceName.verifyType(compiler);
        if (!type.isClass()) {
            throw new ContextualError("New prend comme parametre une classe", instanceName.getLocation());
        }
        instanceName.setType(type);
        this.setType(type);
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        s.print(this.instanceName.getName().toString());
        s.print("()");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instanceName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        instanceName.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister registre) {
        ClassDefinition classe = (ClassDefinition) compiler.getType(instanceName.getName());

        compiler.addInstruction(new NEW(classe.getNumberOfFields() + 1, registre));
        compiler.addInstruction(new LEA(classe.getTableMethode(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, registre)));
        compiler.addInstruction(new PUSH(registre));
        compiler.addInstruction(new BSR(new LabelOperand(classe.getInit())));
        compiler.addInstruction(new POP(registre));

    }
}
