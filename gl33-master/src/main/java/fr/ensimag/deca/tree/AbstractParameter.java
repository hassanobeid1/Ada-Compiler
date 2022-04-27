package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public abstract class AbstractParameter extends Tree {

    public abstract Type verifyType(DecacCompiler compiler) throws ContextualError;

    public abstract void addToEnv(EnvironmentExp env, int i) throws ContextualError;
}
