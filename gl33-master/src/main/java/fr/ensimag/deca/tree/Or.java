package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl33
 * @date 01/01/2021
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, boolean val, Label label) {
        new Not(new And(new Not(this.getLeftOperand()), new Not(this.getRightOperand()))).codeGenBool(compiler, val,
                label);
        ;
    }
}
