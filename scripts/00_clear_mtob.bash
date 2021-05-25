#!/bin/bash

echo "START 00_clear"

set -e

STD_DIR=$1

echo "Clear build and target files"
rm -rf ${STD_DIR}/apps/*/build/*
rm -rf ${STD_DIR}/apps/*/target/*

echo "clear image components"
rm -f ${STD_DIR}/docker-build/MtOB/docker-compose.*.yml
rm -f ${STD_DIR}/docker-build/MtOB/Dockerfile
rm -f ${STD_DIR}/docker-build/MtOB/installations/*.war

echo "FINISH 00_clear"

