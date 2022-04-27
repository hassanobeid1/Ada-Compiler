package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.util.Iterator;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl33
 * @date 01/01/2021
 */
public class ListExpr extends TreeList<AbstractExpr> {

    @Override
    public void decompile(IndentPrintStream s) {
        if (isEmpty()) {
            return;
        }
        Iterator<AbstractExpr> it = iterator();
        it.next().decompile(s);
        it.forEachRemaining(exp -> {
            s.print(", ");
            exp.decompile(s);
        });
    }

    public void verifyRValueStar(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
            Signature expectedSignature) throws ContextualError {
        if (getList().size() != expectedSignature.size()) {
            throw new ContextualError("La methode est appelée avec un mauvais nombre de paramêtre", this.getLocation());
        }
        for (int i = 0; i < expectedSignature.size(); i++) {
            AbstractExpr expr = getList().get(i);
            this.set(i, expr.verifyRValue(compiler, localEnv, currentClass, expectedSignature.paramNumber(i)));
        }
    }

}
