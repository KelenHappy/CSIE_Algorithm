 
/* HW2. Fruits and hash tables
 * This file contains 7 classes:
 * 		- Row represents a row of fruits,
 * 		- CountConfigurationsNaive counts stable configurations naively,
 * 		- Quadruple manipulates quadruplets,
 * 		- HashTable builds a hash table,
 * 		- CountConfigurationsHashTable counts stable configurations using our hash table,
 * 		- Triple manipulates triplets,
 * 		- CountConfigurationsHashMap counts stable configurations using the HashMap of java.
 */


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

class Row { // represent a row of fruits
	private final int[] fruits;

	// empty row constructor
	Row() {
		this.fruits = new int[0];
	}

	// constructor from the field fruits
	Row(int[] fruits) {
		this.fruits = fruits;
	}

	// equals method to compare the row to an object o
	@Override
	public boolean equals(Object o) {
		// we start by transforming the object o into an object of the class Row
		// here we suppose that o will always be of the class Row
		Row that = (Row) o;
		// we check if the two rows have the same length
		if (this.fruits.length != that.fruits.length)
			return false;
		// we check if the i-th fruits of the two rows coincide
		for (int i = 0; i < this.fruits.length; ++i) {
			if (this.fruits[i] != that.fruits[i])
				return false;
		}
		// we have the equality of the two rows
		return true;
	}

	// hash code of the row
	@Override
	public int hashCode() {
		int hash = 0;
		for (int i = 0; i < fruits.length; ++i) {
			hash = 2 * hash + fruits[i];
		}
		return hash;
	}

	// string representing the row
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < fruits.length; ++i)
			s.append(fruits[i]);
		return s.toString();
	}

	// Question 1

	// returns a new row by adding fruit to the end of the row
	Row extendedWith(int fruit) {
		// 建立一個長度加 1 的新陣列
		int[] newFruits = new int[this.fruits.length + 1];
		// 將原本的水果陣列複製到新陣列中
		System.arraycopy(this.fruits, 0, newFruits, 0, this.fruits.length);
		// 在最後一個位置加入新的水果
		newFruits[this.fruits.length] = fruit;
		// 回傳包含新陣列的 Row 物件
		return new Row(newFruits);
	}

	// return the list of all stable rows of width width
	static LinkedList<Row> allStableRows(int width) {
		LinkedList<Row> result = new LinkedList<>();
		// 基本情況：寬度為 0 時，只有一個空的 row
		if (width == 0) {
			result.add(new Row());
			return result;
		}

		// 先取得所有寬度為 width - 1 的穩定 row
		LinkedList<Row> previousRows = allStableRows(width - 1);
		for (Row r : previousRows) {
			int len = r.fruits.length;
			// 對每個既有 row，嘗試在尾端加入 0 與 1
			for (int fruit = 0; fruit <= 1; fruit++) {
				// 只有當最後兩個水果不會和 fruit 形成三個連續相同時，才能加入
				if (len < 2 || r.fruits[len - 1] != fruit || r.fruits[len - 2] != fruit) {
					result.add(r.extendedWith(fruit));
				}
			}
		}
		return result;
	}


	// check if the row can be stacked with rows r1 and r2
	// without having three fruits of the same type adjacent
	boolean areStackable(Row r1, Row r2) {
		// 檢查這三行 (this, r1, r2) 的長度是否相等
		if (this.fruits.length != r1.fruits.length || this.fruits.length != r2.fruits.length) {
			return false;
		}
		
		// 檢查同一個直行 (column) 內，是否同時出現三個相同的水果
		for (int i = 0; i < this.fruits.length; i++) {
			if (this.fruits[i] == r1.fruits[i] && r1.fruits[i] == r2.fruits[i]) {
				return false; // 如果三個水果種類相同，則不可堆疊
			}
		}
		return true;
	}
}

// Naive counting
class CountConfigurationsNaive {  // counting of stable configurations

	// Question 2

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		// 題目指定：若高度小於等於 1，無法形成以前兩列為開頭的合法 grid
		if (height <= 1) {
			return 0;
		}

		// 若高度剛好為 2，代表 grid 就只有 r1、r2 這兩列，因此恰有 1 種
		if (height == 2) {
			return 1;
		}

		// 否則枚舉所有可作為下一列的 row r3
		long result = 0;
		for (Row r3 : rows) {
			// 只有當 r1、r2、r3 可以上下堆疊時，才遞迴計算剩下高度的組合數
			if (r3.areStackable(r1, r2)) {
				result += count(r2, r3, rows, height - 1);
			}
		}
		return result;
	}

	// returning the number of grids with n lines and n columns
	static long count(int n) {
		// 0x0 的 grid 只有一種：空 grid
		if (n == 0) {
			return 1;
		}

		// 1x1 的 grid 有兩種：放 0 或放 1
		if (n == 1) {
			return 2;
		}

		// 先產生所有寬度為 n 的穩定 row
		LinkedList<Row> rows = Row.allStableRows(n);

		// 枚舉前兩列 r1、r2，累加所有高度為 n 的合法 grid 數量
		long result = 0;
		for (Row r1 : rows) {
			for (Row r2 : rows) {
				result += count(r1, r2, rows, n);
			}
		}
		return result;
	}
}

// Construction and use of a hash table

class Quadruple { // quadruplet (r1, r2, height, result)
	Row r1;
	Row r2;
	int height;
	long result;

	Quadruple(Row r1, Row r2, int height, long result) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
		this.result = result;
	}
}

class HashTable { // hash table
	final static int M = 50000;
	Vector<LinkedList<Quadruple>> buckets;

	// Question 3.1

	// constructor
	HashTable() {
		// 建立容量為 M 的 Vector
		this.buckets = new Vector<LinkedList<Quadruple>>(M);
		// 注意：Vector 只有 capacity，不會自動有 M 個元素，必須逐一加入
		for (int i = 0; i < M; i++) {
			this.buckets.add(new LinkedList<Quadruple>());
		}
	}

	// Question 3.2

	// return the hash code of the triplet (r1, r2, height)
	static int hashCode(Row r1, Row r2, int height) {
		// 混合 r1、r2 與 height 的雜湊值，避免公式過度單純
		return 31 * r1.hashCode() + 17 * r2.hashCode() + height;
	}

	// return the bucket of the triplet (r1, r2, height)
	int bucket(Row r1, Row r2, int height) {
		// 先取雜湊值，再取模；為了避免 Java 對負數取模得到負值，手動調整到 [0, M) 範圍
		int b = hashCode(r1, r2, height) % M;
		if (b < 0) {
			b += M;
		}
		return b;
	}

	// Question 3.3

	// add the quadruplet (r1, r2, height, result) in the bucket indicated by the
	// method bucket
	void add(Row r1, Row r2, int height, long result) {
		// 找到對應的 bucket，直接把新的 quadruple 加進 linked list
		int b = bucket(r1, r2, height);
		buckets.get(b).add(new Quadruple(r1, r2, height, result));
	}

	// Question 3.4

	// search in the table an entry for the triplet (r1, r2, height)
	Long find(Row r1, Row r2, int height) {
		// 只需要在對應的 bucket 中線性搜尋
		int b = bucket(r1, r2, height);
		for (Quadruple q : buckets.get(b)) {
			if (q.height == height && q.r1.equals(r1) && q.r2.equals(r2)) {
				return Long.valueOf(q.result);
			}
		}
		// 若找不到，回傳 null
		return null;
	}

}

class CountConfigurationsHashTable { // counting of stable configurations using our hash table
	// 這個版本是為了解決 naive 版本在 8x8 以上明顯過慢的問題。
	// 原因是許多相同的狀態 (r1, r2, height) 會被反覆遞迴計算。
	// 透過 hash table 做 memoization 之後，第一次算出的結果會被存起來，
	// 之後若再次遇到同樣的狀態，就可以直接查表回傳，不必重算。
	// 因此像 8x8、9x9、10x10 這類較大的情況，速度會比 naive 版本快很多。
	static HashTable memo = new HashTable();

	// Question 4

	// return the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using our hash table
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		// 與 naive 版本相同的基本情況
		if (height <= 1) {
			return 0;
		}
		if (height == 2) {
			return 1;
		}

		// 先查 memo，若這個狀態已經算過就直接回傳
		Long cached = memo.find(r1, r2, height);
		if (cached != null) {
			return cached.longValue();
		}

		// 尚未算過時，才真正遞迴計算
		long result = 0;
		for (Row r3 : rows) {
			// 只有當 r1、r2、r3 可以上下堆疊時，才累加後續結果
			if (r3.areStackable(r1, r2)) {
				result += count(r2, r3, rows, height - 1);
			}
		}

		// 把這次算出的結果存進 hash table，之後可直接重用
		memo.add(r1, r2, height, result);
		return result;
	}

	// return the number of grids with n lines and n columns
	// 特別是當 n = 8 時，naive 版本通常已經會慢到像卡住，
	// 但在這裡因為相同子問題會被記錄下來，所以仍可在合理時間內完成。
	static long count(int n) {
		// 每次重新計算 n x n 時，先清空 memo，避免受到之前結果影響
		memo = new HashTable();

		// 和 naive 版本相同的基本情況
		if (n == 0) {
			return 1;
		}
		if (n == 1) {
			return 2;
		}

		// 先產生所有寬度為 n 的穩定 row
		LinkedList<Row> rows = Row.allStableRows(n);

		// 枚舉前兩列 r1、r2，累加所有高度為 n 的合法 grid 數量
		long result = 0;
		for (Row r1 : rows) {
			for (Row r2 : rows) {
				result += count(r1, r2, rows, n);
			}
		}
		return result;
	}
}

//Use of HashMap

class Triple { // triplet (r1, r2, height)
	Row r1;
	Row r2;
	int height;

	// 建立一個三元組，作為 HashMap 的 key
	Triple(Row r1, Row r2, int height) {
		this.r1 = r1;
		this.r2 = r2;
		this.height = height;
	}

	@Override
	public boolean equals(Object o) {
		// 先檢查是否為同一個物件
		if (this == o) {
			return true;
		}
		// 若不是 Triple，則一定不相等
		if (!(o instanceof Triple)) {
			return false;
		}
		// 比較三個欄位是否都相同
		Triple that = (Triple) o;
		return this.height == that.height && this.r1.equals(that.r1) && this.r2.equals(that.r2);
	}

	@Override
	public int hashCode() {
		// 直接沿用前面 HashTable 的雜湊邏輯，保持一致
		return HashTable.hashCode(r1, r2, height);
	}
}

class CountConfigurationsHashMap { // counting of stable configurations using the HashMap of java
	// 這個版本與 CountConfigurationsHashTable 的想法相同，
	// 只是把自己實作的 HashTable 換成 Java 標準函式庫提供的 HashMap。
	// key 是 Triple(r1, r2, height)，value 是對應的計算結果。
	static HashMap<Triple, Long> memo = new HashMap<Triple, Long>();

	// Question 5

	// returning the number of grids whose first lines are r1 and r2,
	// whose lines are lines of rows and whose height is height
	// using the HashMap of java
	static long count(Row r1, Row r2, LinkedList<Row> rows, int height) {
		// 與前面版本相同的基本情況
		if (height <= 1) {
			return 0;
		}
		if (height == 2) {
			return 1;
		}

		// 先把目前狀態包成 Triple，作為 HashMap 的 key
		Triple key = new Triple(r1, r2, height);

		// 若先前已經算過，直接取出結果
		Long cached = memo.get(key);
		if (cached != null) {
			return cached.longValue();
		}

		// 否則才真正遞迴計算
		long result = 0;
		for (Row r3 : rows) {
			// 只有在 r1、r2、r3 可以合法堆疊時，才繼續往下算
			if (r3.areStackable(r1, r2)) {
				result += count(r2, r3, rows, height - 1);
			}
		}

		// 把新算出的結果存入 HashMap，供後續重用
		memo.put(key, Long.valueOf(result));
		return result;
	}

	// return the number of grids with n lines and n columns
	static long count(int n) {
		// 每次重新計算不同大小時，都先清空 memo
		memo = new HashMap<Triple, Long>();

		// 基本情況與前面一致
		if (n == 0) {
			return 1;
		}
		if (n == 1) {
			return 2;
		}

		// 先產生所有寬度為 n 的穩定 row
		LinkedList<Row> rows = Row.allStableRows(n);

		// 枚舉前兩列，累加所有合法的 n x n grid 數量
		long result = 0;
		for (Row r1 : rows) {
			for (Row r2 : rows) {
				result += count(r1, r2, rows, n);
			}
		}
		return result;
	}
}
