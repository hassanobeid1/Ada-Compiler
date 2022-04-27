package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * Declaration d'attribut
 */
public class DeclField extends AbstractDeclField {

    Visibility visibility;
    AbstractIdentifier type;
    AbstractIdentifier name;
    AbstractInitialization value;

    public DeclField(Visibility visibility, AbstractIdentifier type, AbstractIdentifier name,
            AbstractInitialization value) {
        Validate.notNull(visibility);
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(value);

        this.visibility = visibility;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (this.visibility == Visibility.PROTECTED) {
            s.print("protected ");
        } else {
            s.print("public ");
        }
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        value.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        value.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        value.iter(f);
    }

    @Override
    protected void verifyField(DecacCompiler compiler, ClassDefinition def) throws ContextualError {
        Type fieldType = this.type.verifyType(compiler);

        if (fieldType.isVoid()) {
            throw new ContextualError("Le type de l'attribut ne peut pas être null", this.getLocation());
        }

        ExpDefinition parentDef = def.getSuperClass().getMembers().get(name.getName());
        if (parentDef != null && !parentDef.isField()) {
            throw new ContextualError(
                    "L'attribut " + name.getName() + " est défini dans son parent et n'est pas un attribut",
                    name.getLocation());
        }

        FieldDefinition fieldDefinition = new FieldDefinition(fieldType, name.getLocation(), visibility, def,
                def.getNumberOfFields());

        name.setDefinition(fieldDefinition);

        try {
            def.incNumberOfFields();
            def.getMembers().declare(name.getName(), fieldDefinition);
        } catch (DoubleDefException e) {
            throw new ContextualError("Le field " + name.getName().getName() + " est déjà defini", name.getLocation());
        }

        this.name.setType(fieldType);

    }

    @Override
    public void verifyFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        this.value.verifyInitialization(compiler, this.name.getType(), localEnv, currentClass);
    }

    /**
     * Initialisation des champs de la classe
     * 
     * @param compiler
     */
    public void codeGenField(DecacCompiler compiler, int i, GPRegister reg) {
        value.codeGenInitialization(compiler, new RegisterOffset(i + 1, reg), compiler.getNewReg());
        compiler.popReg();
    }

    @Override
    public int getIndex() {
        return this.name.getFieldDefinition().getIndex();
    }
}
