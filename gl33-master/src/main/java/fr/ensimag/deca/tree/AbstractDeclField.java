package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;

public abstract class AbstractDeclField extends Tree {

    /**
     * Etape 2
     * 
     * @param compiler
     * @throws ContextualError
     */
    protected abstract void verifyField(DecacCompiler compiler, ClassDefinition def) throws ContextualError;

    public abstract void verifyFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    public abstract void codeGenField(DecacCompiler compiler, int i, GPRegister obj);

    public abstract int getIndex();
}
