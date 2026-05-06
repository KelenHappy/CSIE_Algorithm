#!/bin/sh
set -eu



echo "Compiling..."
javac *.java

echo ""
echo "Running tests with assertions enabled (-ea)..."
java -ea TestCompare
java -ea TestSqDist
java -ea TestInsert
java -ea TestClosestNaive

java -ea TestClosestOptimized
java -ea TestAverage

# 額外的視覺化測試（需要 GUI 視窗與 photo.jpg）
java -ea ColorPalette
# 互動測試會開啟視窗且不會自動結束，放在最後
java -ea InteractiveClosest


echo ""
echo "All tests finished."
