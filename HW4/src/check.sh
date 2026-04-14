#!/bin/sh
set -eu

cd "$(dirname "$0")"

echo "Compiling..."
javac HW4.java Util.java Cell.java Test1.java Test2.java Test3.java Test4.java

echo ""
echo "Running tests with assertions enabled (-ea)..."
java -ea Test1
java -ea Test2
java -ea Test3
java -ea Test4

echo ""
echo "All tests finished."
