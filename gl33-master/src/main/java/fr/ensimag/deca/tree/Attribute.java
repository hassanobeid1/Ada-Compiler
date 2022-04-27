package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

/**
 * Attribut : x.a
 */
public class Attribute extends AbstractLValue {

    private AbstractLValue instanceName;
    private AbstractIdentifier attributeName;

    public Attribute(AbstractLValue instanceName, AbstractIdentifier attributeName) {
        this.instanceName = instanceName;
        this.attributeName = attributeName;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        ClassType type = instanceName.verifyExpr(compiler, localEnv, currentClass)
                .asClassType("Seule les classes ont des attributs", instanceName.getLocation());
        ExpDefinition def = type.getDefinition().getMembers().get(attributeName.getName());
        if (def == null || !def.isField()) {
            throw new ContextualError(attributeName.getName() + " n'est pas un attribut", attributeName.getLocation());
        }
        FieldDefinition fieldDefinition = (FieldDefinition) def;
        if (fieldDefinition.getVisibility() == Visibility.PROTECTED && !(currentClass != null
                && currentClass.getType().isSubType(fieldDefinition.getContainingClass().getType()))) {
            throw new ContextualError("L'attribut est protected", attributeName.getLocation());
        }
        attributeName.setDefinition(fieldDefinition);
        this.setType(fieldDefinition.getType());
        return fieldDefinition.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        instanceName.decompile(s);
        s.print(".");
        attributeName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        instanceName.prettyPrint(s, prefix, false);
        attributeName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        instanceName.iter(f);
        attributeName.iter(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, GPRegister registre) {
        instanceName.codeGenInst(compiler, registre);
        compiler.addInstruction(new CMP(new NullOperand(), registre));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));
        compiler.addInstruction(new LOAD(
                new RegisterOffset(this.attributeName.getFieldDefinition().getIndex() + 1, registre), registre));
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler) {
        instanceName.codeGenInst(compiler, Register.R1);
        compiler.addInstruction(new CMP(new NullOperand(), Register.R1));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));
        return new RegisterOffset(this.attributeName.getFieldDefinition().getIndex() + 1, Register.R1);
    }
}
