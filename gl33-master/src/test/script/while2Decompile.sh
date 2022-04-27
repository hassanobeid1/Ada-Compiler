#! /bin/sh

# Auteur : gl33
# Version initiale : 01/01/2021

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

decac -p src/test/deca/codegen/valid/others/while2.deca > src/test/deca/codegen/valid/others/test-p.deca
decac src/test/deca/codegen/valid/others/while2.deca
decac src/test/deca/codegen/valid/others/test-p.deca

ima src/test/deca/codegen/valid/others/test-p.ass 2>&1 > src/test/deca/codegen/valid/others/resultat.txt
ima src/test/deca/codegen/valid/others/while2.ass 2>&1 > src/test/deca/codegen/valid/others/resultatAttendu.txt
file1="src/test/deca/codegen/valid/others/resultat.txt"
file2="src/test/deca/codegen/valid/others/resultatAttendu.txt"

if cmp -s "$file1" "$file2"
then
    rm src/test/deca/codegen/valid/others/test-p.deca
    rm src/test/deca/codegen/valid/others/resultat.txt
    rm src/test/deca/codegen/valid/others/resultatAttendu.txt
    echo "Test passé"
else
    rm src/test/deca/codegen/valid/others/test-p.deca
    rm src/test/deca/codegen/valid/others/resultat.txt
    rm src/test/deca/codegen/valid/others/resultatAttendu.txt
    echo "Test échoué"
    exit 1
fi