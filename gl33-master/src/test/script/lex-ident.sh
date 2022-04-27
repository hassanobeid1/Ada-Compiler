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


if test_lex src/test/deca/syntax/valid/others/testIdent.deca 2>&1 \
    | grep -q -e "='test'"
then
    echo "'test' est reconnu"
else
    echo "erreur : 'test' non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testIdent.deca 2>&1 \
    | grep -q -e "='\$test'"
then
    echo "'\$test' est reconnu"
else
    echo "erreur : '\$test' non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testIdent.deca 2>&1 \
    | grep -q -e "='_test'"
then
    echo "'_test' est reconnu"
else
    echo "erreur : '_test' non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testIdent.deca 2>&1 \
    | grep -q -e "='Test'"
then
    echo "'Test' est reconnu"
else
    echo "erreur : 'Test' non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testIdent.deca 2>&1 \
    | grep -q -e "='TEST33'"
then
    echo "'TEST33' est reconnu"
else
    echo "erreur : 'TEST33' non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testIdent.deca 2>&1 \
    | grep -q -e "='T1E2\$_S3T4'"
then
    echo "'T1E2\$_S3T4' est reconnu"
else
    echo "erreur : 'T1E2\$_S3T4' non reconnu"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testIdent.deca 2>&1 \
    | grep -q -e "='1test'"
then
    echo "erreur : '1test' est reconnu"
    exit 1
else
    echo "'1test' non reconnu"
fi