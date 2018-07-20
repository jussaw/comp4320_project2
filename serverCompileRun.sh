#!/bin/bash
javac -d server_out server/*.java
cd server_out
java server.UDPServer
cd ..