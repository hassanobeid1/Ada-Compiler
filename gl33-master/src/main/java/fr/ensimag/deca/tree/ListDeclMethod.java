package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 * Liste de déclaration de méthodes
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
        if (isEmpty()) {
            return;
        }
        Iterator<AbstractDeclMethod> it = iterator();
        it.forEachRemaining(method -> {
            method.decompile(s);
            s.println();
        });
    }

    public void verifyListDeclMethodSignature(DecacCompiler compiler, ClassDefinition def) throws ContextualError {
        for (AbstractDeclMethod method : getList()) {
            method.verifySignature(compiler, def);
        }
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        for (AbstractDeclMethod method : getList()) {
            method.verifyBody(compiler, localEnv, currentClass);
        }
    }

    public void codegenDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod method : this.getList()) {
            method.codeGenMethod(compiler);
        }
    }
}
