#!/bin/bash

echo "START 01_compile"

set -e

STD_DIR=$1
DOC_DIR=$2
echo "STD_DIR=${STD_DIR}"
echo "DOC_DIR=${DOC_DIR}"


echo "update docs"

pandoc --metadata=title:"Standardized Data GDC Converters" -o ${STD_DIR}/apps/BatchEffectsViewer/src/main/webapp/GDCConverters.html --self-contained ${DOC_DIR}/STD/StdData_03_Docs_GDCConverters.docx

pandoc --metadata=title:"Query Form Usage" -o ${STD_DIR}/apps/BatchEffectsViewer/src/main/webapp/QFusage.html --self-contained ${DOC_DIR}/SDB/StdData_03_Docs_QueryForm.docx

pandoc --metadata=title:"MBatch Statistics" -o ${STD_DIR}/apps/BatchEffectsViewer/src/main/webapp/MBatch_04-99_Statistics.html --self-contained ${DOC_DIR}/MBatch/MBatch_04-99_Statistics.docx

echo "compile BatchEffectsViewer"
cd ${STD_DIR}/apps/BatchEffectsViewer

echo "compile java file"
javac -d ./ ./src/main/java/edu/mda/bcb/bev/util/BEV_AppGenerator.java

echo "build BatchEffectsViewer Stand Alone HTML file"
java edu/mda/bcb/bev/util/BEV_AppGenerator ./src/main/webapp/view/ ./src/main/webapp/bevApp.html

echo "remove compiled class"
rm -r edu

echo "build BatchEffectsViewer war"
mvn clean install dependency:copy-dependencies

#echo "Check DAPIR"
#cd ${STD_DIR}/apps
#R CMD check DAPIR
#R CMD build DAPIR

echo "list BatchEffectsViewer/target/*.war"
ls -lh ${STD_DIR}/apps/BatchEffectsViewer/target/*.war
echo "list DAPIR"
ls -lh ${STD_DIR}/apps

echo "FINISH 01_compile"

