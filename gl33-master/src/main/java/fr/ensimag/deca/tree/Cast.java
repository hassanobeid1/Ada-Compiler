package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Parses the cast : (type) varibaleName
 *
 * @author Tanguy Poinson
 *
 * @date 08/01/2021
 */
public class Cast extends AbstractExpr {

    final private AbstractIdentifier type;
    final private AbstractExpr expr;

    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.type = type;
        this.expr = expr;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type1 = type.verifyType(compiler);
        Type type2 = expr.verifyExpr(compiler, localEnv, currentClass);
        type1.isCastCompatible(type2);
        this.setType(type1);
        return type1;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        type.decompile(s);
        s.print(")");
        s.print("(");
        expr.decompile(s);
        s.print(")");

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister registre) {

        if (type.getType().isInt() && expr.getType().isFloat()) {
            compiler.addComment("Cast from float to int");
            this.expr.codeGenInst(compiler, registre);
            compiler.addInstruction(new INT(registre, registre));
            return;
        }

        Label label = compiler.getLabel("si_true");
        compiler.addComment("cast " + expr.getType().getName().getName() + " to " + type.getName().getName());

        ClassDefinition compared = type.getClassDefinition();
        expr.codeGenInst(compiler, registre);
        compiler.addInstruction(new CMP(new NullOperand(), registre));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));

        compiler.addComment(expr.getType().getName().getName() + " instance of " + type.getName().getName() + "?");
        Label debut_boucle = compiler.getLabel("debut_boucle");
        Label fin_boucle = compiler.getLabel("fin_boucle");
        compiler.addLabel(debut_boucle);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, registre), registre));
        compiler.addInstruction(new BEQ(fin_boucle));

        compiler.addInstruction(new LEA(compared.getTableMethode(), Register.R0));
        compiler.addInstruction(new CMP(Register.R0, registre));
        compiler.addInstruction(new BEQ(label));
        compiler.addInstruction(new BRA(debut_boucle));
        compiler.addLabel(fin_boucle);

        compiler.addInstruction(new WSTR(new ImmediateString("Erreur : Tentative de cast vers un type incompatible")));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());
        compiler.addLabel(label);
        this.expr.codeGenInst(compiler, registre);
    }
}
