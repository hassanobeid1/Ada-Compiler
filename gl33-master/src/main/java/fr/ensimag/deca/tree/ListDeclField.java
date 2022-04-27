package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import java.util.Iterator;

/**
 * Liste de declaration d'attributs
 */
public class ListDeclField extends TreeList<AbstractDeclField> {

    public void verifyListDeclField(DecacCompiler compiler, ClassDefinition def) throws ContextualError {
        for (AbstractDeclField field : getList()) {
            field.verifyField(compiler, def);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (isEmpty()) {
            return;
        }
        Iterator<AbstractDeclField> it = iterator();
        it.forEachRemaining(field -> {
            field.decompile(s);
            s.println();
        });
        s.println();
    }

    public void verifyListFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        for (AbstractDeclField field : getList()) {
            field.verifyFieldBody(compiler, localEnv, currentClass);
        }
    }

    /**
     * Génère l'initialisation des fields
     *
     * @param compiler le compilateur du programme
     */
    public void codeGenListFields(DecacCompiler compiler, ClassDefinition classe) {
        Label label = classe.getInit();
        compiler.addLabel(label);

        compiler.newBlock();
        compiler.setInInit(true);

        Line tstoline = new Line("TSTO");
        compiler.add(tstoline);
        compiler.addInstruction(new BOV(new Label("pile_pleine")));

        GPRegister obj = compiler.getMinReg();

        Line[] regPush = new Line[compiler.numReg()];

        for (int i = 0; i < regPush.length; i++) {
            Line line = new Line("Push Registre Placeholder");
            compiler.add(line);
            regPush[i] = line;
        }

        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), obj));

        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        for (int i = 1; i <= classe.getNumberOfFields(); i++) {
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(i, obj)));
        }

        int pushInitParent = 0;
        if (classe.getSuperClass() != null) {
            // Initialisation des champs hérités
            compiler.addInstruction(new PUSH(obj));
            compiler.addInstruction(new BSR(classe.getSuperClass().getInit()));
            compiler.addInstruction(new SUBSP(1));
            pushInitParent = 3;
        }

        for (int i = 0; i < this.getList().size(); i++) {
            AbstractDeclField field = this.getList().get(i);
            field.codeGenField(compiler, field.getIndex(), obj);
        }
        int numToPush = compiler.numToPush();
        int maxpush = compiler.getMaxPush();

        for (int i = 0; i < numToPush; i++) {
            regPush[i].setInstruction(new PUSH(Register.getR(i + 2)));
        }

        for (int i = numToPush - 1; i >= 0; i--) {
            compiler.addInstruction(new POP(Register.getR(i + 2)));
        }

        tstoline.setInstruction(new TSTO(maxpush + numToPush + pushInitParent + compiler.getMaxNbParameter()));

        compiler.addInstruction(new RTS());

        compiler.setInInit(false);

    }
}
