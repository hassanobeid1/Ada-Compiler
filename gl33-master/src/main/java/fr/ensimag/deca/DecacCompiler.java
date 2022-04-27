package fr.ensimag.deca;

import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.VoidType;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl33
 * @date 01/01/2021
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.envTypePredef();
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers to
     * use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     *      java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String name = source.getName();
        Path path = source.toPath().toAbsolutePath();

        String destFile = path.getParent().toString() + "/";
        int pos = name.lastIndexOf(".");
        if (pos == -1) {
            destFile += name + ".ass";
        } else {
            destFile += name.substring(0, pos + 1) + "ass";
        }

        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName   name of the destination (assembly) file
     * @param out        stream to use for standard output (output of decac -p)
     * @param err        stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName, PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert (prog.checkAllLocations());

        if (this.getCompilerOptions().getParse()) {
            prog.decompile(System.out);
            return false;
        }

        prog.verifyProgram(this);
        assert (prog.checkAllDecorations());

        if (this.getCompilerOptions().getVerification()) {
            return false;
        }

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract syntax
     * tree.
     *
     * @param sourceName Name of the file to parse
     * @param err        Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError    When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     *                            compiler.
     * @throws LocationException  When a compilation error (incorrect program)
     *                            occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    private SymbolTable symbols = new SymbolTable();

    /**
     * 
     * @param name Le nom du symbole
     * @return Le symbole dans la table globale
     */
    public Symbol getSymbol(String name) {
        return symbols.create(name);
    }

    private Map<Symbol, TypeDefinition> envType;

    private void envTypePredef() {
        envType = new HashMap<Symbol, TypeDefinition>();
        try {
            this.declareType(this.getSymbol("void"),
                    new TypeDefinition(new VoidType(this.getSymbol("void")), Location.BUILTIN));
            this.declareType(this.getSymbol("boolean"),
                    new TypeDefinition(new BooleanType(this.getSymbol("boolean")), Location.BUILTIN));
            this.declareType(this.getSymbol("float"),
                    new TypeDefinition(new FloatType(this.getSymbol("float")), Location.BUILTIN));
            this.declareType(this.getSymbol("int"),
                    new TypeDefinition(new IntType(this.getSymbol("int")), Location.BUILTIN));
            this.declareType(this.getSymbol("String"),
                    new TypeDefinition(new StringType(this.getSymbol("String")), Location.BUILTIN));
            this.declareType(this.getSymbol("null"),
                    new TypeDefinition(new NullType(this.getSymbol("null")), Location.BUILTIN));

            ClassType object = new ClassType(getSymbol("Object"), Location.BUILTIN, null);
            this.declareType(getSymbol("Object"), object.getDefinition());

            object.getDefinition().setInit(getLabel("Object.init"));

            Signature signatureObject = new Signature();
            signatureObject.add(object);
            MethodDefinition def = new MethodDefinition(getType(getSymbol("boolean")).getType(), Location.BUILTIN,
                    signatureObject, 0);
            def.setLabel(getLabel("Object.equals"));
            try {
                object.getDefinition().getMembers().declare(getSymbol("equals"), def);
                object.getDefinition().incNumberOfMethods();
            } catch (DoubleDefException e) {
                throw new RuntimeException("Ca ne devrait pas arriver");
            }

        } catch (ContextualError e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Declare un type dans envype
     * 
     * @param name
     * @param type
     * @param location
     * @throws ContextualError si le nom est déjà utilisé
     */
    public void declareType(Symbol name, TypeDefinition type) throws ContextualError {
        if (this.envType.containsKey(name)) {
            throw new ContextualError(name + " is declared multiple times", type.getLocation());
        } else {
            this.envType.put(name, type);
        }
    }

    /**
     * Recupère le type dans envType
     * 
     * @param name
     * @return null si le type n'existe pas
     */
    public TypeDefinition getType(Symbol name) {
        return envType.get(name);
    }

    private int minRegDispo = 2; // 2

    private int maxRegUsed = 1; // 1

    private int maxNumParameters = 0; // 0

    private int minReg() {
        return 2;
    }

    /**
     * 
     * @return Le nombre de push utilisés pour les registres
     */
    public int getMaxPush() {
        return Integer.max(0, maxRegUsed - maxReg());
    }

    private int maxReg() {
        return this.getCompilerOptions().getNbRegisters() - 1;
    }

    private void incReg() {
        minRegDispo = minRegDispo + 1;
        maxRegUsed = Integer.max(maxRegUsed, minRegDispo - 1);
    }

    private void decReg() {
        minRegDispo = minRegDispo - 1;
    }

    /**
     * Récupère un nouveau registre Si il y a des registres libre plus haut,
     * l'utilise Sinon PUSH du registre courant
     * 
     * Attention : Il faut utiliser popReg pour pouvoir accèder au données qui
     * étaient dans reg
     * 
     * @return Un registre de libre
     */
    public GPRegister getNewReg() {
        if (minRegDispo < maxReg() + 1) {
            incReg();
            return Register.getR(minRegDispo - 1);
        } else {
            addInstruction(new PUSH(Register.getR(maxReg())));
            incReg();
            return Register.getR(maxReg());
        }
    }

    /**
     * A utiliser si il n'y a pas de registres utilisés
     * 
     * @return Le premier registre.
     */
    public GPRegister getMinReg() {
        minRegDispo = minReg();
        return getNewReg();
    }

    /**
     * A utiliser avec getNewReg
     * 
     * Si un registre à été PUSH après getNewReg, la fonction deplace les données de
     * RMAX dans R0 et POP dans RMAX
     * 
     * @return
     */
    public GPRegister popReg() {
        if (minRegDispo > maxReg() + 1) {
            addInstruction(new LOAD(Register.getR(maxReg()), Register.R0));
            addInstruction(new POP(Register.getR(maxReg())));
            decReg();
            return Register.R0;
        } else {
            decReg();
            return Register.getR(minRegDispo);
        }
    }

    /**
     * A utiliser quand un nouveau block est crée pour la gestion des registres
     */
    public void newBlock() {
        minRegDispo = 2;
        maxRegUsed = 1;
        maxNumParameters = 0;
        returnLabel = null;
    }

    boolean inMethod = false;

    /**
     * Set le flag inMethod
     */
    public void setInMethod(boolean bool) {
        inMethod = true;
    }

    /**
     * @return le flag inMethod
     */
    public boolean inMethod() {
        return inMethod;
    }

    /**
     * @return Le nombre de registre à push pour cette methode
     */
    public int numToPush() {
        if (inMethod() || inInit()) {
            return Integer.min(maxRegUsed, maxReg()) - minReg() + 1;
        } else {
            return 0;
        }
    }

    /**
     * @return Le nombre de GPRegistres sans compter R0 et R1
     */
    public int numReg() {
        return maxReg() - minReg() + 1;
    }

    private int label_number = 0;

    /**
     * @return Un Label unique
     */
    public Label getLabel(String name) {
        return new Label("a" + label_number++ + "_" + name);
    }

    int methodTableOffset = 1;

    /**
     * Retourne la prochaine adresse dans la table des methodes
     */
    public RegisterOffset getNextTablePos() {
        return new RegisterOffset(methodTableOffset++, Register.GB);
    }

    /**
     * @return La taille de la table des methodes
     */
    public int getTailleTableMethode() {
        return methodTableOffset - 1;
    }

    /**
     * Permet de calculer le nombre de registres nésesaires pour un bloc
     */
    public void callFunction(int nbParameters) {
        maxNumParameters = Integer.max(maxNumParameters, nbParameters);
    }

    /**
     * @return La place necessaire pour appeler la fonction avec le plus de
     *         paramêtres
     */
    public int getMaxNbParameter() {
        return maxNumParameters;
    }

    private Label returnLabel;

    /**
     * @return le label de fin de fonction de la methode courante.
     */
    public Label getReturnLabel() {
        Validate.notNull(returnLabel);
        return returnLabel;
    }

    /**
     * Defini le label de fin de fonction
     */
    public void setReturnLabel(Label returnLabel) {
        this.returnLabel = returnLabel;
    }

    private boolean inInit = false;

    /**
     * Set le flag inInit
     */
    public void setInInit(boolean b) {
        inInit = b;
    }

    /**
     * @return le flag inInit
     */
    public boolean inInit() {
        return inInit;
    }
}
