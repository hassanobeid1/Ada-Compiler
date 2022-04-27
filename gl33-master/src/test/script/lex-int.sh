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


if test_lex src/test/deca/syntax/valid/others/testInt.deca 2>&1 \
    | sed -n "5p" | grep -q -e "INT:" && test_lex src/test/deca/syntax/valid/others/testInt.deca 2>&1 \
    | sed -n "5p" | grep -q -e "='345'"
then
    echo "Le 1er int est reconnu"
else
    echo "erreur sur le 1er int"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testInt.deca 2>&1 \
    | sed -n "10p" | grep -q -e "INT:" && test_lex src/test/deca/syntax/valid/others/testInt.deca 2>&1 \
    | sed -n "10p" | grep -q -e "='0'"
then
    echo "Le 2eme int est reconnu"
else
    echo "erreur sur le 2eme int"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testInt.deca 2>&1 \
    | grep -q -e "='003'"
then
    echo "Erreur, il reconnait '003' comme un entier"
    exit 1
else
    echo "Test passé, il ne reconnait pas '003' comme int."
fi