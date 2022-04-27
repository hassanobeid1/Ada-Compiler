package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Paramètre utiliser dans la définition des methodes
 */
public class Parameter extends AbstractParameter {

    private AbstractIdentifier type;
    private AbstractIdentifier name;

    public Parameter(AbstractIdentifier type, AbstractIdentifier name) {
        Validate.notNull(type);
        Validate.notNull(name);

        this.type = type;
        this.name = name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        Type type = this.type.verifyType(compiler);
        if (type.isVoid()) {
            throw new ContextualError("Le type du paramêtre ne peut pas être void", this.type.getLocation());
        }

        ParamDefinition def = new ParamDefinition(type, name.getLocation());
        name.setDefinition(def);

        return type;
    }

    @Override
    public void addToEnv(EnvironmentExp env, int i) throws ContextualError {
        try {
            name.getExpDefinition().setOperand(new RegisterOffset(-i - 3, Register.LB));
            env.declare(name.getName(), name.getExpDefinition());
        } catch (DoubleDefException e) {
            throw new ContextualError("Plusieurs paramêtres ont pour nom : " + name.getName(), name.getLocation());
        }
    }
}
