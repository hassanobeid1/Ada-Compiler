#! /bin/sh

# Auteur : Tanguy Poinson
# Version initiale : 25/01/2021

# Tests sur la syntaxe du Deca sans objet
# On lance test_lex ou test_synt sur tous les fichiers valides puis invalides du dossier syntax

# dans le cas du fichier valide, on teste seulement qu'il n'y a pas eu
# d'erreur. Il faudrait tester que l'arbre donné est bien le bon. Par
# exemple, en stoquant la valeur attendue quelque part, et en
# utilisant la commande unix "diff".

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

test_invalide () {
    fichier=$(basename "$1")
    fichier=${fichier%%.*}

    if [ -e "$chemin"expected/"$fichier".lxr ]
    then
      test_lex "$1" > "$1.tmp" 2>&1
      chemin_exp="$chemin"expected/"$fichier".lxr
    else
      test_synt "$1" > "$1.tmp" 2>&1
      chemin_exp="$chemin"expected/"$fichier".lis
    fi

    if grep "$1.tmp" -q -e ':[0-9][0-9]*:' || grep "$1.tmp" -q -e 'Exception'
then
    if cmp -s "$1".tmp "$chemin_exp"
    then
      echo "Echec attendu sur $1"
      rm "$1.tmp"
    else
      echo "Resultat diiferent de l'attendu sur $1"
      rm "$1.tmp"
      exit 1
    fi
else
    echo "Succes inattendu pour test_lex_synt sur $1"
    rm "$1.tmp"
    exit 1
fi
}

test_valide () {
    if grep "$1.tmp" -q -e ':[0-9][0-9]*:'
then
    echo "Echec inattendu pour test_lex_synt sur $1"
    rm "$1.tmp"
    exit 1
else
    if cmp -s "$1.tmp" "$2"
    then
      echo "Succes attendu sur $1"
      rm "$1.tmp"
    else
      echo "Resultat diiferent de l'attendu sur $1"
      rm "$1.tmp"
      exit 1
    fi
fi
}

#Test sur les fichiers invalides
chemin=src/test/deca/syntax/invalid/others/
for cas_de_test in "$chemin"*.deca
do
    test_invalide "$cas_de_test"
done

chemin=src/test/deca/syntax/valid/others/
#Test sur les fichiers valides
for cas_de_test in "$chemin"*.deca
do
    fichier=$(basename "$cas_de_test")
    fichier=${fichier%%.*}
    test_lex "$cas_de_test" > "$cas_de_test.tmp" 2>&1
    chemin_exp="$chemin"expected/"$fichier".lxr
    test_valide "$cas_de_test" "$chemin_exp"
    test_synt "$cas_de_test" > "$cas_de_test.tmp" 2>&1
    chemin_exp="$chemin"expected/"$fichier".lis
    test_valide "$cas_de_test" "$chemin_exp"
done

exit 0