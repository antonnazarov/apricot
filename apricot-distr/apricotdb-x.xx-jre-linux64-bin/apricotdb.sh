#!/bin/bash

./jre1.8.0_212/bin/java -jar ./apricot-launcher-0.6.jar
STATUS="${?}"
if [ "$STATUS" = "0" ]; then
    ./jre1.8.0_212/bin/java -Xms1024m -Xmx2048m -jar ./apricot-ui-0.6.jar
else
    echo unable to run the ApricotDB launcher
fi
