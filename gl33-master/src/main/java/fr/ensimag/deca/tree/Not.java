package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl33
 * @date 01/01/2021
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        this.getOperand().verifyCondition(compiler, localEnv, currentClass);
        this.setType(this.getOperand().getType());
        return this.getType();
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, boolean val, Label label) {
        this.getOperand().codeGenBool(compiler, !val, label);
    }
}
