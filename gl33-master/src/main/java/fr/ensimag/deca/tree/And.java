package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl33
 * @date 01/01/2021
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, boolean val, Label label) {
        if (val) {
            Label E_fin = compiler.getLabel("E_fin");
            this.getLeftOperand().codeGenBool(compiler, false, E_fin);
            this.getRightOperand().codeGenBool(compiler, true, label);
            compiler.addLabel(E_fin);
        } else {
            this.getLeftOperand().codeGenBool(compiler, val, label);
            this.getRightOperand().codeGenBool(compiler, val, label);
        }
    }

}
