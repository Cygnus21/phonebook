#!/usr/bin/env bash

rm -rf svc
mkdir svc
unzip -d svc ../target/universal/phonebook-1.0-SNAPSHOT.zip
mv svc/*/* svc/
rm svc/bin/*.bat
mv svc/bin/* svc/bin/start
