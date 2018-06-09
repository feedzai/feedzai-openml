#!/usr/bin/env bash

if [ "$TRAVIS_PULL_REQUEST" = "false" ] && [ ! -z "$TRAVIS_TAG" ]; then
    mvn deploy -Prelease --settings .m2/settings.xml -DskipTests=true
    exit $?
fi
