#!/bin/sh
JVM_PATH=$1
MVN_PATH=$2
WORKING_PATH=$3
BUILD_OPT=$4

pwd $WORKING_PATH
JAVA_HOME=$JVM_PATH

$MVN_PATH $BUILD_OPT