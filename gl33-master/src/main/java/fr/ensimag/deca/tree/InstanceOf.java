package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.context.*;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Parses the InstanceOf statement
 */
public class InstanceOf extends AbstractExpr {
    AbstractIdentifier type;
    AbstractExpr value;

    public InstanceOf(AbstractIdentifier type, AbstractExpr value) {
        Validate.notNull(type);
        Validate.notNull(value);
        this.type = type;
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        Type type1 = value.verifyExpr(compiler, localEnv, currentClass);
        Type type2 = type.verifyType(compiler);
        if (!type1.isClassOrNull()) {
            throw new ContextualError("instanceof peut être utilisée que sur des classes ou null", value.getLocation());
        } else if (!type2.isClass()) {
            throw new ContextualError("instanceof prend un type de classe", type.getLocation());
        } else {
            this.setType(compiler.getType(compiler.getSymbol("boolean")).getType());
            return this.getType();
        }
    }

    @Override
    protected void codeGenBool(DecacCompiler compiler, boolean val, Label label) {
        ClassDefinition compared = type.getClassDefinition();
        GPRegister registre = compiler.getNewReg();
        value.codeGenInst(compiler, registre);

        Label debut_boucle = compiler.getLabel("debut_boucle");
        Label fin_boucle = compiler.getLabel("fin_boucle");

        compiler.addInstruction(new CMP(new NullOperand(), registre));
        if (val) {
            compiler.addInstruction(new BEQ(fin_boucle));
        } else {
            compiler.addInstruction(new BEQ(label));
        }
        compiler.addComment(value.getType().getName().getName() + " instance of " + type.getName().getName() + "?");
        compiler.addLabel(debut_boucle);
        compiler.addInstruction(new LOAD(new RegisterOffset(0, registre), registre));
        if (val) {
            compiler.addInstruction(new BEQ(fin_boucle));
        } else {
            compiler.addInstruction(new BEQ(label));
        }
        compiler.addInstruction(new LEA(compared.getTableMethode(), Register.R0));
        compiler.addInstruction(new CMP(Register.R0, registre));
        if (val) {
            compiler.addInstruction(new BEQ(label));
        } else {
            compiler.addInstruction(new BEQ(fin_boucle));
        }
        compiler.addInstruction(new BRA(debut_boucle));
        compiler.addLabel(fin_boucle);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" instanceof ");
        this.value.decompile(s);

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        value.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        value.iter(f);
    }
}
