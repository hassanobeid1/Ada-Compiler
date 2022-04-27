package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Bloc deca
 */
public class BlockBody extends AbstractBody {

    private ListDeclVar varDecl;
    private ListInst insts;
    private boolean returnVoid;

    public BlockBody(ListDeclVar varDecl, ListInst insts) {
        Validate.notNull(varDecl);
        Validate.notNull(insts);

        this.varDecl = varDecl;
        this.insts = insts;
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        varDecl.iter(f);
        insts.iter(f);
    }

    @Override
    public void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        returnVoid = returnType.isVoid();
        varDecl.verifyListDeclVariable(compiler, localEnv, currentClass);
        insts.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.varDecl.prettyPrint(s, prefix, false);
        this.insts.prettyPrint(s, prefix, true);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        this.varDecl.decompile(s);
        this.insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    public void codeGen(DecacCompiler compiler) {
        compiler.newBlock();

        Label label = compiler.getLabel("return");
        compiler.setReturnLabel(label);

        Line tstoline = new Line("TSTO");
        compiler.add(tstoline);
        compiler.addInstruction(new BOV(new Label("pile_pleine")));

        Line[] regPush = new Line[compiler.numReg()];

        if (compiler.inInit() || compiler.inMethod()) {
            for (int i = 0; i < regPush.length; i++) {
                Line line = new Line("Push Registre Placeholder");
                compiler.add(line);
                regPush[i] = line;
            }
        }

        int nbVariables = this.varDecl.getList().size();
        if (nbVariables > 0) {
            compiler.addInstruction(new ADDSP(nbVariables));
        }
        this.varDecl.codeGenListVar(compiler);

        compiler.addComment("Beginning of instructions:");
        this.insts.codeGenListInst(compiler);

        int numToPush = compiler.numToPush();
        int maxpush = compiler.getMaxPush();

        for (int i = 0; i < numToPush; i++) {
            regPush[i].setInstruction(new PUSH(Register.getR(i + 2)));
        }

        if (!returnVoid) {
            compiler.addInstruction(new WSTR("Erreur : sortie de la methode sans return"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
            compiler.addLabel(label);
        }

        for (int i = numToPush - 1; i >= 0; i--) {
            compiler.addInstruction(new POP(Register.getR(i + 2)));
        }

        if (compiler.inMethod()) {
            compiler.addInstruction(new RTS());
        }

        tstoline.setInstruction(new TSTO(nbVariables + maxpush + numToPush + compiler.getMaxNbParameter()));
    }

}
