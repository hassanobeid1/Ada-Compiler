package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl33
 * @date 01/01/2021
 */
public class DeclClass extends AbstractDeclClass {

    private AbstractIdentifier className;
    private AbstractIdentifier parentClass;
    private ListDeclField fields;
    private ListDeclMethod methods;

    public DeclClass(AbstractIdentifier className, AbstractIdentifier parentClass, ListDeclField fields,
            ListDeclMethod methods) {
        Validate.notNull(className);
        Validate.notNull(parentClass);
        Validate.notNull(fields);
        Validate.notNull(methods);

        this.className = className;
        this.parentClass = parentClass;
        this.fields = fields;
        this.methods = methods;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class " + className.decompile());
        if (!parentClass.decompile().equals("object")) {
            s.print(" extends " + parentClass.decompile());
        }
        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        TypeDefinition superClassDef = compiler.getType(parentClass.getName());

        if (superClassDef == null) {
            throw new ContextualError("La classe parente n'existe pas", parentClass.getLocation());
        }

        ClassType superClass = superClassDef.getType().asClassType(
                "La classe parente : " + parentClass.getName() + " n'existe pas", parentClass.getLocation());

        ClassType type = new ClassType(className.getName(), className.getLocation(), superClass.getDefinition());
        compiler.declareType(className.getName(), type.getDefinition());

        className.setDefinition(type.getDefinition());
        parentClass.setDefinition(superClass.getDefinition());
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {
        className.getClassDefinition().setNumberOfFields(parentClass.getClassDefinition().getNumberOfFields());
        className.getClassDefinition().setNumberOfMethods(parentClass.getClassDefinition().getNumberOfMethods());
        this.fields.verifyListDeclField(compiler, className.getClassDefinition());
        this.methods.verifyListDeclMethodSignature(compiler, className.getClassDefinition());
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        fields.verifyListFieldBody(compiler, className.getClassDefinition().getMembers(),
                className.getClassDefinition());
        methods.verifyListDeclMethodBody(compiler, className.getClassDefinition().getMembers(),
                className.getClassDefinition());
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        className.prettyPrint(s, prefix, false);
        parentClass.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        className.iter(f);
        parentClass.iter(f);
        fields.iter(f);
        methods.iter(f);
    }

    @Override
    protected void generateTableMethodes(DecacCompiler compiler) {
        className.getClassDefinition().setInit(compiler.getLabel(this.className.getName().getName() + ".init"));

        DAddr pos = compiler.getNextTablePos();
        className.getClassDefinition().setTableMethode(pos);

        compiler.addInstruction(new LEA(this.parentClass.getClassDefinition().getTableMethode(), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, pos));

        for (MethodDefinition method : this.className.getClassDefinition().getMethodes()) {
            compiler.addInstruction(new LOAD(new LabelOperand(method.getLabel()), Register.R0));
            compiler.addInstruction(new STORE(Register.R0, compiler.getNextTablePos()));
        }
    }

    @Override
    protected void codeGenInitialization(DecacCompiler compiler) {
        this.fields.codeGenListFields(compiler, this.className.getClassDefinition());
    }

    @Override
    public void codeGenInits(DecacCompiler compiler) {
        this.fields.codeGenListFields(compiler, this.className.getClassDefinition());
    }

    @Override
    public void codeGenMethodes(DecacCompiler compiler) {
        this.methods.codegenDeclMethod(compiler);
    }

}
