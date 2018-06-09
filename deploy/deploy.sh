#!/usr/bin/env sh

if [[ $TRAVIS_PULL_REQUEST == "false" ]] && [[ ! -z $TRAVIS_TAG ]]; then
    mvn deploy --settings .m2/settings.xml -DskipTests=true
    exit $?
fi
