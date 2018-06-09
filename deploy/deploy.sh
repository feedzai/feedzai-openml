#!/usr/bin/env sh

if [[ $TRAVIS_PULL_REQUEST == "false" ]] && [[ ! -z "$TRAVIS_TAG" ]]; then
    mvn deploy --settings $GPG_DIR/settings.xml -DperformRelease=true -DskipTests=true
    exit $?
fi
