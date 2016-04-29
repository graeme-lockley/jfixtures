#!/bin/bash

if [ "" == "$1" ]
then
	echo Provide a release
	exit -1
else
	echo Creating branch jfixtures-$1
	git branch jfixtures-$1
	git checkout jfixtures-$1

	mvn versions:set -DnewVersion=$1 -Prelease
	mvn clean deploy -Prelease
fi

