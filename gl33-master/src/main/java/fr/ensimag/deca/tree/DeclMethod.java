package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Déclaration de méthode
 */
public class DeclMethod extends AbstractDeclMethod {

    private AbstractIdentifier name;
    private AbstractIdentifier returnType;
    private ListParameter args;
    private AbstractBody body;

    public DeclMethod(AbstractIdentifier name, AbstractIdentifier returnType, ListParameter args, AbstractBody body) {
        Validate.notNull(name);
        Validate.notNull(returnType);
        Validate.notNull(args);
        Validate.notNull(body);

        this.name = name;
        this.returnType = returnType;
        this.args = args;
        this.body = body;
    }

    @Override
    protected void verifySignature(DecacCompiler compiler, ClassDefinition def) throws ContextualError {
        Type type = returnType.verifyType(compiler);

        Signature sig = args.verifyParameters(compiler);

        ExpDefinition parentDef = def.getSuperClass().getMembers().get(name.getName());

        int index;

        if (parentDef != null) {
            if (!parentDef.isMethod()) {
                throw new ContextualError("L'identifier " + name.getName() + " n'est pas une méthode pour le parent",
                        name.getLocation());
            }
            MethodDefinition parentDefMethod = (MethodDefinition) parentDef;
            if (!sig.equals(parentDefMethod.getSignature())) {
                throw new ContextualError("Si une méthode est redéfinie, elle doit avoir la même signature",
                        args.getLocation());
            }

            if (!type.isSubType(parentDefMethod.getType())) {
                throw new ContextualError(
                        "Le type " + type.getName() + " n'est pas un sous type de " + parentDefMethod.getType(),
                        returnType.getLocation());
            }
            index = parentDefMethod.getIndex();
        } else {
            index = def.getNumberOfMethods();
            def.incNumberOfMethods();
        }

        MethodDefinition methodDefinition = new MethodDefinition(type, name.getLocation(), sig, index);
        try {
            def.getMembers().declare(name.getName(), methodDefinition);
        } catch (DoubleDefException e) {
            throw new ContextualError("La méthode " + name.getName() + " est déjà definie", name.getLocation());
        }

        this.name.setDefinition(methodDefinition);
        this.name.setType(type);

        methodDefinition.setLabel(compiler.getLabel(this.name.getName().getName()));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        returnType.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print("(");
        args.decompile(s);
        s.print(")");
        body.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnType.iter(f);
        name.iter(f);
        args.iter(f);
        body.iter(f);
    }

    @Override
    protected void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        EnvironmentExp argsEnv = this.args.getEnv(localEnv);
        body.verifyBody(compiler, argsEnv, currentClass, name.getType());
    }

    public void codeGenMethod(DecacCompiler compiler) {
        compiler.addComment("Code de la methode " + this.name.getName().getName());
        Label label = this.name.getMethodDefinition().getLabel();
        compiler.addLabel(label);
        compiler.setInMethod(true);
        body.codeGen(compiler);
        compiler.setInMethod(false);
    }
}
