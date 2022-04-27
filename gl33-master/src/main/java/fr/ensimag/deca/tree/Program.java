package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl33
 * @date 01/01/2021
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public ListDeclClass getClasses() {
        return classes;
    }

    public AbstractMain getMain() {
        return main;
    }

    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        this.classes.verifyListClass(compiler);
        LOG.debug("Passe 1 OK");
        this.classes.verifyListClassMembers(compiler);
        LOG.debug("Passe 2 OK");
        this.classes.verifyListClassBody(compiler);
        LOG.debug("Passe 3 Classes OK");
        this.main.verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        Line tstoline = new Line("TSTO");
        compiler.add(tstoline);
        compiler.addInstruction(new BOV(new Label("pile_pleine")));

        this.generateObjectTable(compiler);
        classes.generateTableMethodes(compiler);

        tstoline.setInstruction(new TSTO(compiler.getTailleTableMethode()));
        compiler.addInstruction(new ADDSP(compiler.getTailleTableMethode()));

        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());

        // Stack overflow
        compiler.addLabel(new Label("pile_pleine"));
        compiler.addInstruction(new WSTR(new ImmediateString("Stack overflow")));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        // Null pointer
        compiler.addLabel(new Label("dereferencement.null"));
        compiler.addInstruction(new WSTR(new ImmediateString("Erreur : dereferencement de null")));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        // Division by 0
        compiler.addLabel(new Label("div0"));
        compiler.addInstruction(new WSTR(new ImmediateString("Erreur : tentative de division par 0")));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        // Reste de division par 0
        compiler.addLabel(new Label("modulo0"));
        compiler.addInstruction(new WSTR(new ImmediateString("Erreur : tentative d'obtenir le reste d'une division par 0")));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        generateObjectInit(compiler);
        this.classes.generateInits(compiler);
        generateObjectEquals(compiler);
        this.classes.generateMethodes(compiler);
    }

    private void generateObjectInit(DecacCompiler compiler) {
        ClassDefinition def = (ClassDefinition) compiler.getType(compiler.getSymbol("Object"));
        compiler.addLabel(def.getInit());
        compiler.addInstruction(new RTS());
    }

    private void generateObjectEquals(DecacCompiler compiler) {
        ClassDefinition def = (ClassDefinition) compiler.getType(compiler.getSymbol("Object"));
        MethodDefinition methodeDef = def.getMethodes()[0];
        compiler.addLabel(methodeDef.getLabel());
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.SP), Register.R1));
        compiler.addInstruction(new CMP(new RegisterOffset(-2, Register.SP), Register.R1));

        Label labelEq = compiler.getLabel("Eq");
        compiler.addInstruction(new BEQ(labelEq));
        compiler.addInstruction(new LOAD(0, Register.R0));
        compiler.addInstruction(new RTS());
        compiler.addLabel(labelEq);
        compiler.addInstruction(new LOAD(1, Register.R0));
        compiler.addInstruction(new RTS());

    }

    private void generateObjectTable(DecacCompiler compiler) {
        ClassDefinition def = (ClassDefinition) compiler.getType(compiler.getSymbol("Object"));

        DAddr pos = compiler.getNextTablePos();
        def.setTableMethode(pos);

        compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, pos));

        for (MethodDefinition method : def.getMethodes()) {
            compiler.addInstruction(new LOAD(new LabelOperand(method.getLabel()), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, compiler.getNextTablePos()));
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
