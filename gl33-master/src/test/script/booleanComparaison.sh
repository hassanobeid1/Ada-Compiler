#! /bin/sh

# Auteur : gl33
# Version initiale : 01/01/2021

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

decac src/test/deca/codegen/valid/others/testComparaisons.deca

if ima src/test/deca/syntax/valid/others/testComparaisons.ass 2>&1 \
    | grep -q -e "0"
then
    echo "il y a des erreurs"
    exit 1
fi