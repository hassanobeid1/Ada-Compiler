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


if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "7p" | grep -q -e "'+':"
then
    echo "Le '+' fonctionne"
else
    echo "erreur sur '+'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "4p" | grep -q -e "'=':"
then
    echo "Le '=' fonctionne"
else
    echo "erreur sur '='"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "21p" | grep -q -e "'<':"
then
    echo "Le '<' fonctionne"
else
    echo "erreur sur '<'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "27p" | grep -q -e "'>':"
then
    echo "Le '>' fonctionne"
else
    echo "erreur sur '>'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "12p" | grep -q -e "'-':"
then
    echo "Le '-' fonctionne"
else
    echo "erreur sur '-'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "10p" | grep -q -e "'*':"
then
    echo "Le '*' fonctionne"
else
    echo "erreur sur '*'"
    exit 1
fi


if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "5p" | grep -q -e "'(':"
then
    echo "Le '(' fonctionne"
else
    echo "erreur sur '('"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "9p" | grep -q -e "')':"
then
    echo "Le ')' fonctionne"
else
    echo "erreur sur ')'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "1p" | grep -q -e "'{':"
then
    echo "Le '{' fonctionne"
else
    echo "erreur sur '{'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "57p" | grep -q -e "'}':"
then
    echo "Le '}' fonctionne"
else
    echo "erreur sur '}'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "14p" | grep -q -e "';':"
then
    echo "Le ';' fonctionne"
else
    echo "erreur sur ';'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "31p" | grep -q -e "'==':"
then
    echo "Le '==' fonctionne"
else
    echo "erreur sur '=='"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "54p" | grep -q -e "'!=':"
then
    echo "Le '!=' fonctionne"
else
    echo "erreur sur '!='"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "41p" | grep -q -e "'>=':"
then
    echo "Le '>=' fonctionne"
else
    echo "erreur sur '>='"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "35p" | grep -q -e "'<=':"
then
    echo "Le '<=' fonctionne"
else
    echo "erreur sur '<='"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "38p" | grep -q -e "'&&':"
then
    echo "Le '&&' fonctionne"
else
    echo "erreur sur '&&'"
    exit 1
fi

if test_lex src/test/deca/syntax/valid/others/testSymbolesSpeciaux.deca 2>&1 \
    | sed -n "24p" | grep -q -e "'||':"
then
    echo "Le '||' fonctionne"
else
    echo "erreur sur '||'"
    exit 1
fi