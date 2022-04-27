package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl33
 * @date 01/01/2021
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        if (!this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass).isInt()) {
            throw new ContextualError("Modulo prend comme paramètre des int", this.getLeftOperand().getLocation());
        }
        if (!this.getRightOperand().verifyExpr(compiler, localEnv, currentClass).isInt()) {
            throw new ContextualError("Modulo prend comme paramètre des int", this.getRightOperand().getLocation());
        }
        this.setType(compiler.getType(compiler.getSymbol("int")).getType());
        return this.getType();
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

}
