#! /bin/sh

# Auteur : Tanguy Poinson
# Version initiale : 23/01/2021

# Tests sur le contexte du Deca sans objet
# On lance test_context sur tous les fichiers valides puis invalides du dossier context

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

test_context_invalide () {
    test_context "$1" > "$1".tmp
    if grep "$1".tmp -q -e ':[0-9][0-9]*:'
then
    fichier=$(basename "$1")
    fichier=${fichier%%.*}
    if cmp -s "$1".tmp "$chemin"expected/"$fichier".lis
    then
      echo "Echec attendu sur $1"
      rm "$1".tmp
    else
      echo "Resultat diiferent de l'attendu sur $1"
      rm "$1".tmp
      exit 1
    fi
else
    echo "Succes inattendu pour test_context sur $1"
    rm "$1".tmp
    exit 1
fi
}

test_context_valide () {
    test_context "$1" > "$1".tmp
    if grep "$1".tmp -q -e ':[0-9][0-9]*:'
then
    echo "Echec inattendu pour test_context sur $1"
    rm "$1".tmp
    exit 1
else
    fichier=$(basename "$1")
    fichier=${fichier%%.*}
    if cmp -s "$1".tmp "$chemin"expected/"$fichier".lis
    then
      echo "Succes attendu sur $1"
      rm "$1".tmp
    else
      echo "Resultat diiferent de l'attendu sur $1"
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