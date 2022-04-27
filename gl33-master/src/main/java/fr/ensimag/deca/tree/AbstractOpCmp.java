package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.NotImplementedException;

/**
 *
 * @author gl33
 * @date 01/01/2021
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        return verifyNotExact(compiler, type1, type2);
    }

    protected Type verifyNotExact(DecacCompiler compiler, Type type1, Type type2) throws ContextualError {
        if ((type1.isInt() || type1.isFloat()) && (type2.isInt() || type2.isFloat())) {
            if (type1.isInt() && type2.isFloat()) {
                this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                this.getLeftOperand().setType(compiler.getType(compiler.getSymbol("float")).getType());
            } else if (type2.isInt() && type1.isFloat()) {
                this.setRightOperand(new ConvFloat(this.getRightOperand()));
                this.getRightOperand().setType(compiler.getType(compiler.getSymbol("float")).getType());
            }
            this.setType(compiler.getType(compiler.getSymbol("boolean")).getType());

            return this.getType();
        } else {
            Location loc;
            if (!(type1.isFloat() || type2.isInt())) {
                loc = this.getLeftOperand().getLocation();
            } else {
                loc = this.getRightOperand().getLocation();
            }
            throw new ContextualError("Les operations de comparaison prennent comme paramètres des int ou des float",
                    loc);
        }
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, boolean val, Label label) {
        GPRegister registre = compiler.getMinReg();
        getLeftOperand().codeGenInst(compiler, registre);
        GPRegister nouveau_registre = compiler.getNewReg();
        getRightOperand().codeGenInst(compiler, nouveau_registre);
        nouveau_registre = compiler.popReg();
        compiler.addInstruction(new CMP(nouveau_registre, registre));

        switch (getOperatorName()) {
            case "==":
                if (val) {
                    compiler.addInstruction(new BEQ(label));
                } else {
                    compiler.addInstruction(new BNE(label));
                }
                break;
            case "!=":
                if (val) {
                    compiler.addInstruction(new BNE(label));
                } else {
                    compiler.addInstruction(new BEQ(label));
                }
                break;
            case ">=":
                if (val) {
                    compiler.addInstruction(new BGE(label));
                } else {
                    compiler.addInstruction(new BLT(label));
                }
                break;
            case ">":
                if (val) {
                    compiler.addInstruction(new BGT(label));
                } else {
                    compiler.addInstruction(new BLE(label));
                }
                break;
            case "<=":
                if (val) {
                    compiler.addInstruction(new BLE(label));
                } else {
                    compiler.addInstruction(new BGT(label));
                }
                break;
            case "<":
                if (val) {
                    compiler.addInstruction(new BLT(label));
                } else {
                    compiler.addInstruction(new BGE(label));
                }
                break;
            default:
                throw new NotImplementedException("This operation is not implemented");
        }
    }
}
