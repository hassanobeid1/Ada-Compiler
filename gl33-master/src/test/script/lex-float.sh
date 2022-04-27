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


if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='12.45'"
then
    echo "12.45 est reconnu"
else
    echo "erreur : 12.45 non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4f'"
then
    echo "3.4f est reconnu"
else
    echo "erreur : 3.4f non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4F'"
then
    echo "3.4F est reconnu"
else
    echo "erreur : 3.4F non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4E+3'"
then
    echo "3.4E+3 est reconnu"
else
    echo "erreur : 3.4E+3 non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4E+3f'"
then
    echo "3.4E+3f est reconnu"
else
    echo "erreur : 3.4E+3f non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4E+3F'"
then
    echo "3.4E+3F est reconnu"
else
    echo "erreur : 3.4E+3F non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4E-3'"
then
    echo "3.4E-3 est reconnu"
else
    echo "erreur : 3.4E-3 non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='E-3'"
then
    echo "E-3 est reconnu"
else
    echo "E-3 non reconnu"
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4e+3'"
then
    echo "3.4e+3 est reconnu"
else
    echo "erreur : 3.4e+3 non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4e+3f'"
then
    echo "3.4e+3f est reconnu"
else
    echo "erreur : 3.4e+3f non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='0xAA.FFP+30'"
then
    echo "0xAA.FFP+30 est reconnu"
else
    echo "erreur : 0xAA.FFP+30 non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='0Xaa.FFP+271f'"
then
    echo "0Xaa.FFP+271f est reconnu"
else
    echo "erreur : 0Xaa.FFP+271f non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testFloat.deca 2>&1 \
    | grep -q -e "='3.4e+3f'"
then
    echo "0X4a4.FFp-38F est reconnu"
else
    echo "erreur : 0X4a4.FFp-38F non reconnu"
    exit 1
fi