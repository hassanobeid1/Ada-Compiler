#! /bin/sh

# Auteur : gl33
# Version initiale : 01/01/2021

# Base pour un script de test de la lexicographie.
# On teste un fichier valide et un fichier invalide.
# Il est conseillé de garder ce fichier tel quel, et de créer de
# nouveaux scripts (en s'inspirant si besoin de ceux fournis).

# Il faudrait améliorer ce script pour qu'il puisse lancer test_lex
# sur un grand nombre de fichiers à la suite.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


if test_lex src/test/deca/syntax/valid/others/testString.deca 2>&1 \
    | grep -q -e "='\"hello\"'"
then
    echo "\"hello\" est reconnu"
else
    echo "erreur : \"hello\" non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testString.deca 2>&1 \
    | grep -q -e "='\"hello\\\nworld\"'"
then
    echo "\"hello\\\nworld\" est reconnu"
else
    echo "erreur : ='\"hello\\\nworld\"' non reconnu"
fi
