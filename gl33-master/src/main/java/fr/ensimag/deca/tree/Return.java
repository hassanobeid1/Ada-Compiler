package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Parses the return statement
 *
 * @author Tanguy Poinson
 *
 * @date 08/01/2021
 */
public class Return extends AbstractInst {

    private AbstractExpr value;

    public Return(AbstractExpr value) {
        this.value = value;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        if (returnType.isVoid()) {
            throw new ContextualError("Il n'y a pas de return dans les methodes qui retourne void",
                    value.getLocation());
        }
        this.value = this.value.verifyRValue(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister registre) {
        this.value.codeGenInst(compiler, registre);
        compiler.addInstruction(new LOAD(registre, Register.R0));
        compiler.addInstruction(new BRA(compiler.getReturnLabel()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        value.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        value.prettyPrint(s, prefix, true);

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        value.iter(f);
    }
}
