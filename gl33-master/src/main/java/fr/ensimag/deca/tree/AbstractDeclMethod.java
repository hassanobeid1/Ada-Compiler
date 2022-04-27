package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclMethod extends Tree {

    /**
     * Etape 2
     * 
     * @param compiler
     * @throws ContextualError
     */
    protected abstract void verifySignature(DecacCompiler compiler, ClassDefinition def) throws ContextualError;

    /**
     * Etape 3
     * 
     * @param compiler
     * @param classDefinition
     * @param members
     * @throws ContextualError
     */
    protected abstract void verifyBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    public abstract void codeGenMethod(DecacCompiler compiler);
}
