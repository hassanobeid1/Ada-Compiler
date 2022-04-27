package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;
import java.util.Iterator;

/**
 *
 * @author gl33
 * @date 01/01/2021
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        if (isEmpty()) {
            return;
        }
        Iterator<AbstractDeclClass> it = iterator();
        it.next().decompile(s);
        it.forEachRemaining(c -> {
            s.println();
            c.decompile(s);
        });
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass c : getList()) {
            c.verifyClassMembers(compiler);
        }
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass c : getList()) {
            c.verifyClassBody(compiler);
        }
    }

    public void generateTableMethodes(DecacCompiler compiler) {
        for (AbstractDeclClass c : getList()) {
            c.generateTableMethodes(compiler);
        }
    }

    public void generateInits(DecacCompiler compiler) {
        for (AbstractDeclClass c : getList()) {
            c.codeGenInits(compiler);
        }
    }

    public void generateMethodes(DecacCompiler compiler) {
        for (AbstractDeclClass c : getList()) {
            c.codeGenMethodes(compiler);
        }
    }

}
