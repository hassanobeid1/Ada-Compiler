package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl33
 * @date 01/01/2021
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar var : this.getList()) {
            var.decompile(s);
        }
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * 
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     its "parentEnvironment" corresponds to "env_exp_sup"
     *                     attribute in precondition, its "current" dictionary
     *                     corresponds to the "env_exp" attribute in postcondition,
     *                     its "current" dictionary corresponds to the "env_exp_r"
     *                     attribute
     * @param currentClass corresponds to "class" attribute (null in the main bloc).
     */
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        for (AbstractDeclVar var : this.getList()) {
            var.verifyDeclVar(compiler, localEnv, currentClass);
        }
    }

    public void codeGenListVar(DecacCompiler compiler) {
        for (int i = 0; i < this.getList().size(); i++) {
            this.getList().get(i).codeGenVar(compiler, i);
        }
    }

}
