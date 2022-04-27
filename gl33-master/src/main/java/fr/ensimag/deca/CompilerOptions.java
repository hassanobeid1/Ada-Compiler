package fr.ensimag.deca;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl33
 * @date 01/01/2021
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;

    public int getDebug() {
        return debug;
    }

    public boolean getVerification() {
        return verification;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParse() {
        return parse;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }

    public int getNbRegisters() {
        return nbRegisters;
    }

    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private List<File> sourceFiles = new ArrayList<File>();
    private boolean verification = false;
    private boolean parse = false;
    private int nbRegisters = 16;

    public void parseArgs(String[] args) throws CLIException {

        if (args.length == 0) {
            return;
        }

        int i = 0;
        while (args[i].startsWith("-")) {
            switch (args[i]) {
                case "-P":
                    this.parallel = true;
                    break;
                case "-b":
                    this.printBanner = true;
                    if (args.length == 1) {
                        return;
                    } else {
                        throw new CLIException("L'option -b est incompatible avec les autres options");
                    }
                case "-v":
                    this.verification = true;
                    break;
                case "-p":
                    this.parse = true;
                    break;
                case "-r":
                    i++;
                    this.nbRegisters = Integer.parseInt(args[i]);
                    break;
                case "-d":
                    debug++;
                    break;
                default:
                    throw new CLIException("Option " + args[i] + " inconnue");
            }
            i++;
        }

        if (nbRegisters < 4 || nbRegisters > 16) {
            throw new CLIException("On peut utiliser entre 4 et 16 registres");
        }

        if (this.verification && this.parse) {
            throw new CLIException("Les options’-p’et’-v’sont incompatibles");

        }

        for (; i < args.length; i++) {
            sourceFiles.add(new File(args[i]));
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
            case QUIET:
                break; // keep default
            case INFO:
                logger.setLevel(Level.INFO);
                break;
            case DEBUG:
                logger.setLevel(Level.DEBUG);
                break;
            case TRACE:
                logger.setLevel(Level.TRACE);
                break;
            default:
                logger.setLevel(Level.ALL);
                break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

    }

    protected void displayUsage() {
        System.out.println("Utilisation : decac [[-p | -v] [-r X] [-d]* [-P] <fichier deca>...] | [-b]");
        System.out.println(". -b       (banner)       : affiche une bannière indiquant le nom de l’équipe");
        System.out.println(
                ". -p       (parse)        : arrête decac après l’étape de construction del’arbre, et affiche la décompilation de ce dernier(i.e. s’il n’y a qu’un fichier source à compiler, la sortie doit être un programmedeca syntaxiquement correct)");
        System.out.println(
                ". -v       (verification) : arrête decac après l’étape de vérifications(ne produit aucune sortie en l’absence d’erreur)");
        System.out.println(
                ". -r X     (registers)    : limite les registres banalisés disponibles àR0 ... R{X-1}, avec 4 <= X <= 16");
        System.out.println(
                ". -d       (debug)        : active les traces de debug. Répéterl’option plusieurs fois pour avoir plus detraces.");
        System.out.println(
                ". -P       (parallel)     : s’il y a plusieurs fichiers sources,lance la compilation des fichiers enparallèle (pour accélérer la compilation)");
    }
}
