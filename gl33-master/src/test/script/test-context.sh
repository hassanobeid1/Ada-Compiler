#! /bin/sh

# Auteur : Tanguy Poinson
# Version initiale : 17/01/2021

# Tests sur la syntaxe du Deca sans objet
# On lance test_synt sur tous les fichiers valides puis invalides du dossier syntax

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# exemple de définition d'une fonction
test_context_invalide () {
    # $1 = premier argument.
    if test_context "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_context sur $1."

    else
        echo "Succes inattendu de test_context sur $1."
        exit 1
    fi
}

test_context_valide () {
    if test_context "$1" 2>&1 | \
    grep -q -e ':[0-9][0-9]*:'
then
    echo "Echec inattendu pour test_context sur $1"
    exit 1
else
    fichier=$(basename "$1")
    fichier=${fichier%%.*}
    test_context "$1" > "$1".tmp
    if cmp -s "$1".tmp "$chemin"expected/"$fichier".lis
    then
      echo "Succes attendu sur $1"
      rm "$1".tmp
    else
      echo "Resultat different de l'attendu sur $1"
      rm "$1".tmp
      exit 1
    fi
fi
}

#Test sur les fichiers invalides
chemin=src/test/deca/context/invalid/other/
for cas_de_test in "$chemin"*.deca
do
    test_context_invalide "$cas_de_test"
done

chemin=src/test/deca/context/valid/others/
#Test sur les fichiers valides
for cas_de_test in "$chemin"*.deca
do
    test_context_valide "$cas_de_test"
done

exit 0