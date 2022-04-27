package fr.ensimag.deca.context;

import fr.ensimag.deca.tree.Location;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Label;

import org.apache.commons.lang.Validate;

/**
 * Definition of a class.
 *
 * @author gl33
 * @date 01/01/2021
 */
public class ClassDefinition extends TypeDefinition {

    public void setNumberOfFields(int numberOfFields) {
        this.numberOfFields = numberOfFields;
    }

    public DAddr getTableMethode() {
        Validate.notNull(tableMethode);
        return tableMethode;
    }

    public void setTableMethode(DAddr tableMethode) {
        this.tableMethode = tableMethode;
    }

    public int getNumberOfFields() {
        return numberOfFields;
    }

    public void incNumberOfFields() {
        this.numberOfFields++;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public void setNumberOfMethods(int n) {
        Validate.isTrue(n >= 0);
        numberOfMethods = n;
    }

    public int incNumberOfMethods() {
        numberOfMethods++;
        return numberOfMethods;
    }

    private int numberOfFields = 0;
    private int numberOfMethods = 0;

    @Override
    public boolean isClass() {
        return true;
    }

    @Override
    public ClassType getType() {
        // Cast succeeds by construction because the type has been correctly set
        // in the constructor.
        return (ClassType) super.getType();
    };

    public ClassDefinition getSuperClass() {
        return superClass;
    }

    private final EnvironmentExp members;
    private final ClassDefinition superClass;

    public EnvironmentExp getMembers() {
        return members;
    }

    public ClassDefinition(ClassType type, Location location, ClassDefinition superClass) {
        super(type, location);
        EnvironmentExp parent;
        if (superClass != null) {
            parent = superClass.getMembers();
        } else {
            parent = null;
        }
        members = new EnvironmentExp(parent);
        this.superClass = superClass;
    }

    public MethodDefinition[] getMethodes() {
        MethodDefinition[] methods = new MethodDefinition[numberOfMethods];
        return this.members.getMethodes(methods);
    }

    private DAddr tableMethode;

    private Label init;

    public void setInit(Label label) {
        this.init = label;
    }

    public Label getInit() {
        Validate.notNull(init);
        return init;
    }

}
