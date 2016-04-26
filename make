#!/bin/bash
cd xqt.model
mvn clean install
cd ..

cd xqt.lang
mvn clean install
cd ..

cd xqt.engine
mvn clean install
cd ..

cd xqt.adapters.builtin
mvn clean install
cd ..

cd xqt.adapters.csv
mvn clean install
cd ..

cd xqt.adapters.dbms
mvn clean install
cd ..

cd xqt.eninge.builtin
mvn clean install
cd ..

cd xqt.runtime
mvn clean install
cd ..

cd xqt.api
mvn clean install
cd ..

