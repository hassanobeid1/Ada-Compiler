package fr.ensimag.deca.tree;

import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.deca.DecacCompiler;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl33
 * @date 01/01/2021
 */
public abstract class AbstractLValue extends AbstractExpr {
    public abstract DAddr getAddr(DecacCompiler compiler);
}
