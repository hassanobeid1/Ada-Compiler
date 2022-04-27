package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.NotImplementedException;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl33
 * @date 01/01/2021
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if ((type1.isInt() || type1.isFloat()) && (type2.isInt() || type2.isFloat())) {
            if (type1.isFloat() && type2.isInt()) {
                this.setRightOperand(new ConvFloat(this.getRightOperand()));
                this.getRightOperand().setType(compiler.getType(compiler.getSymbol("float")).getType());
                this.setType(type1);
            } else if (type1.isInt() && type2.isFloat()) {
                this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
                this.getLeftOperand().setType(compiler.getType(compiler.getSymbol("float")).getType());
                this.setType(type2);
            } else {
                this.setType(type2);
            }
            return this.getType();
        } else {
            Location loc;
            if (!(type1.isFloat() || type2.isInt())) {
                loc = this.getLeftOperand().getLocation();
            } else {
                loc = this.getRightOperand().getLocation();
            }
            throw new ContextualError("Les operations arithmetiques prennent comme paramètres des int ou des float",
                    loc);
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister registre) {
        this.getLeftOperand().codeGenInst(compiler, registre);
        GPRegister nouveau_registre = compiler.getNewReg();
        this.getRightOperand().codeGenInst(compiler, nouveau_registre);
        nouveau_registre = compiler.popReg();
        switch (getOperatorName()) {
            case "+":
                compiler.addInstruction(new ADD(nouveau_registre, registre));
                break;
            case "-":
                compiler.addInstruction(new SUB(nouveau_registre, registre));
                break;
            case "*":
                compiler.addInstruction(new MUL(nouveau_registre, registre));
                break;
            case "/":
                if (this.getLeftOperand().getType().isInt()) {
                    compiler.addInstruction(new QUO(nouveau_registre, registre));
                } else {
                    compiler.addInstruction(new DIV(nouveau_registre, registre));
                }
                compiler.addInstruction(new BOV(new Label("div0")));
                break;
            case "%":
                if (this.getLeftOperand().getType().isInt() && this.getRightOperand().getType().isInt()) {
                    compiler.addInstruction(new REM(nouveau_registre, registre));
                    compiler.addInstruction(new BOV(new Label("modulo0")));
                }
                else
                {
                    //Si on fait appel à l'opérateur de reste de division entière sur autre chose qu'un entier, on produit une erreur
                    compiler.addInstruction(new WSTR(new ImmediateString("Erreur : tentative d'obtenir le reste d'une division non entière")));
                    compiler.addInstruction(new WNL());
                    compiler.addInstruction(new ERROR());
                }
                break;
            default:
                throw new NotImplementedException("this operation is not implemented");
        }
    }
}
