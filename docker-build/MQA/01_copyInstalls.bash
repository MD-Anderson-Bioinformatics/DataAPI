#!/bin/bash

echo "START 01_copyInstalls"

set -e

STD_DIR=$1

echo "compile Batch Effects Viewer"
cd ${STD_DIR}/apps/BatchEffectsViewer
mvn clean install dependency:copy-dependencies

echo "Copy Batch Effects Viewer WAR"
cp ${STD_DIR}/apps/BatchEffectsViewer/target/BatchEffectsViewer-*.war ${STD_DIR}/docker-build/MQA/installations/MQA.war

echo "List StdMW Installations"
ls -lh ${STD_DIR}/docker-build/MQA/installations/

echo "FINISH 01_copyInstalls"

