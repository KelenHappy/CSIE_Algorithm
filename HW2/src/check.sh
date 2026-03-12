#!/bin/sh
set -eu

cd "$(dirname "$0")"

echo "Compiling..."
javac HW2.java Test1.java Test2.java Test3.java Test4.java Test5.java

echo ""
echo "Running tests with assertions enabled (-ea)..."
java -ea Test1
# Test2 採用 naive（暴力遞迴）方法，在較大的 n 時會變得非常慢
#java -ea Test2
java -ea Test3
java -ea Test4
java -ea Test5

echo ""
echo "All tests finished."