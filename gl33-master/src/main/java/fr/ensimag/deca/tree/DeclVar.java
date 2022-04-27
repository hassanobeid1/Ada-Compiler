package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl33
 * @date 01/01/2021
 */
public class DeclVar extends AbstractDeclVar {

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;
    private VariableDefinition def;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type type = this.type.verifyType(compiler);
        Symbol name = this.varName.getName();

        if (type.isVoid()) {
            throw new ContextualError("La variable à pour type void", this.type.getLocation());
        }

        this.initialization.verifyInitialization(compiler, type, localEnv, currentClass);

        def = new VariableDefinition(type, this.varName.getLocation());
        this.varName.setDefinition(def);

        try {
            localEnv.declare(name, def);
        } catch (DoubleDefException e) {
            throw new ContextualError("Variable " + name.toString() + " is already defined",
                    this.varName.getLocation());
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(type.getName() + " " + varName.getName());
        initialization.decompile(s);
        s.println(";");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    public void codeGenVar(DecacCompiler compiler, int i) {
        int tableMethode = 0;
        if (!compiler.inMethod()) {
            tableMethode = compiler.getTailleTableMethode();
        }
        def.setOperand(new RegisterOffset(i + 1 + tableMethode, Register.LB));
        this.initialization.codeGenInitialization(compiler, new RegisterOffset(i + 1 + tableMethode, Register.LB),
                compiler.getMinReg());
    }
}
