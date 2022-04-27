package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl33
 * @date 01/01/2021
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }

    public Type paramNumber(int n) {
        return args.get(n);
    }

    public int size() {
        return args.size();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Signature)) {
            return false;
        }

        Signature otherSig = (Signature) other;

        if (otherSig.args.size() != args.size()) {
            return false;
        }

        for (int i = 0; i < args.size(); i++) {
            if (!args.get(i).sameType(otherSig.args.get(i))) {
                return false;
            }
        }
        return true;
    }
}
