package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.HashMap;

/**
 * Dictionary associating identifier's ExpDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentExp has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current"
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current"
 * dictionary and in the parentEnvironment if it fails.
 * 
 * Insertion (through method declare) is always done in the "current"
 * dictionary.
 * 
 * @author gl33
 * @date 01/01/2021
 */
public class EnvironmentExp {
    // A FAIRE : implémenter la structure de donnée représentant un
    // environnement (association nom -> définition, avec possibilité
    // d'empilement).

    EnvironmentExp parentEnvironment;
    HashMap<Symbol, ExpDefinition> assos;

    public EnvironmentExp(EnvironmentExp parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
        this.assos = new HashMap<Symbol, ExpDefinition>();
    }

    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }

    /**
     * Return the definition of the symbol in the environment, or null if the symbol
     * is undefined.
     */
    public ExpDefinition get(Symbol key) {
        ExpDefinition val = this.assos.get(key);
        if (val == null && this.parentEnvironment != null) {
            val = this.parentEnvironment.get(key);
        }
        return val;
    }

    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment, - throws
     * DoubleDefException if the symbol is in the "current" dictionary - or, hides
     * the previous declaration otherwise.
     * 
     * @param name Name of the symbol to define
     * @param def  Definition of the symbol
     * @throws DoubleDefException if the symbol is already defined at the "current"
     *                            dictionary
     *
     */
    public void declare(Symbol name, ExpDefinition def) throws DoubleDefException {
        if (this.assos.containsKey(name)) {
            throw new DoubleDefException();
        } else {
            this.assos.put(name, def);
        }
    }

    public MethodDefinition[] getMethodes(MethodDefinition[] methods) {

        if (this.parentEnvironment != null) {
            methods = this.parentEnvironment.getMethodes(methods);
        }

        for (ExpDefinition def : assos.values()) {
            if (def.isMethod()) {
                MethodDefinition methodDefinition = (MethodDefinition) def;
                methods[methodDefinition.getIndex()] = methodDefinition;

            }
        }
        return methods;
    }
}
