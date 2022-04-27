#! /bin/sh

# Auteur : gl33
# Version initiale : 01/01/2021

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

decac src/test/deca/codegen/valid/others/testBooleanType.deca

if ima src/test/deca/codegen/valid/others/testBooleanType.ass 2>&1 | \
    grep -q -e '1'
then
    echo "Test passé"
else
    echo "Test échoué"
    exit 1
fi