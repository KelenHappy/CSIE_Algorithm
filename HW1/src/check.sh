#!/bin/sh
set -eu

cd "$(dirname "$0")"

echo "Compiling..."
javac HW1.java Test1.java Test21a.java Test21b.java Test22.java Test23.java Test31.java Test32.java Test33.java Test41.java Test42.java

echo ""
echo "Running tests with assertions enabled (-ea)..."
java -ea Test1
java -ea Test21a
java -ea Test21b
java -ea Test22
# Test23 is heavy (up to ~1 minute)
java -ea Test23
java -ea Test31
java -ea Test32
java -ea Test33
java -ea Test41
java -ea Test42

echo ""
echo "All tests finished."
