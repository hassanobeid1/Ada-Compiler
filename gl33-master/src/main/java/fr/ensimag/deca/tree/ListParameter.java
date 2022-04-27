package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 * 
 * Liste de paramètres pour les signatures de fonctions
 * 
 * @author Tanguy Poinson
 *
 * @date 19/01/2021
 *
 */

public class ListParameter extends TreeList<AbstractParameter> {
    @Override
    public void decompile(IndentPrintStream s) {
        if (isEmpty()) {
            return;
        }
        Iterator<AbstractParameter> it = iterator();
        it.next().decompile(s);
        it.forEachRemaining(param -> {
            s.print(", ");
            param.decompile(s);
        });
    }

    public Signature verifyParameters(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();

        for (AbstractParameter param : getList()) {
            sig.add(param.verifyType(compiler));
        }

        return sig;
    }

    public EnvironmentExp getEnv(EnvironmentExp localEnv) throws ContextualError {
        EnvironmentExp env = new EnvironmentExp(localEnv);
        for (int i = 0; i < getList().size(); i++) {
            getList().get(i).addToEnv(env, i);
        }

        return env;
    }
}
