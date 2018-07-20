#!/bin/bash
javac -d client_out client/*.java
cd client_out
java client.UDPClient .1
cd ..
