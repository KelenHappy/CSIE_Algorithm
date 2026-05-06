import java.util.Vector;

public class KDTree {
	int depth;
	double[] point;
	KDTree left;
	KDTree right;

	KDTree(double[] point, int depth) {
		this.point = point;
		this.depth = depth;
	}

	double diff(double[] a) {
		int axis = this.depth % this.point.length;
		return a[axis] - this.point[axis];
	}

	boolean compare(double[] a) {
		// 相等時往右子樹，符合測試對 tie-break 的期待
		return diff(a) >= 0;
	}

	static KDTree insert(KDTree tree, double[] p) {
		if (tree == null) {
			return new KDTree(p, 0);
		}

		KDTree current = tree;
		while (true) {
			if (current.compare(p)) { // p should go to the right
				if (current.right == null) {
					current.right = new KDTree(p, current.depth + 1);
					break;
				}
				current = current.right;
			} else { // p should go to the left
				if (current.left == null) {
					current.left = new KDTree(p, current.depth + 1);
					break;
				}
				current = current.left;
			}
		}
		return tree; // Return the original root
	}

	static double sqDist(double[] a, double[] b) {
		double dist = 0;
		for (int i = 0; i < a.length; i++) {
			double diff = a[i] - b[i];
			dist += diff * diff;
		}
		return dist;
	}

	static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
		if (tree == null) {
			return champion;
		}

		if (sqDist(a, tree.point) < sqDist(a, champion)) {
			champion = tree.point;
		}

		champion = closestNaive(tree.left, a, champion);
		champion = closestNaive(tree.right, a, champion);

		return champion;
	}


	static double[] closestNaive(KDTree tree, double[] a) {
		if (tree == null) {
			return null;
		}
		return closestNaive(tree, a, tree.point);
	}

	static double[] closest(KDTree tree, double[] a, double[] champion) {
		if (tree == null) {
			return champion;
		}
		InteractiveClosest.trace(tree.point, champion);
		if (sqDist(a, tree.point) < sqDist(a, champion)) {
			champion = tree.point;
		}
		KDTree goodSide = tree.compare(a) ? tree.right : tree.left;
		KDTree badSide = tree.compare(a) ? tree.left : tree.right;

		champion = closest(goodSide, a, champion);
		if (tree.diff(a) * tree.diff(a) < sqDist(a, champion)) {
			champion = closest(badSide, a, champion);
		}
		return champion;
	}

	static double[] closest(KDTree tree, double[] a) {
		if (tree == null) {
			return null;
		}
		return closest(tree, a, tree.point);
	}

	static int size(KDTree tree) {
		if (tree == null) {
			return 0;
		}
		return 1 + size(tree.left) + size(tree.right);
	}

	static void sum(KDTree tree, double[] acc) {
		if (tree == null) {
			return;
		}
		for (int i = 0; i < tree.point.length; i++) {
			acc[i] += tree.point[i];
		}
		sum(tree.left, acc);
		sum(tree.right, acc);
	}

	static double[] average(KDTree tree) {
		if (tree == null) {
			return null;
		}
		int n = size(tree);
		double[] acc = new double[tree.point.length];
		sum(tree, acc);
		for (int i = 0; i < acc.length; i++) {
			acc[i] /= n;
		}
		return acc;
	}


	static Vector<double[]> palette(KDTree tree, int maxpoints) {
		Vector<double[]> palette = new Vector<double[]>();
		if (tree == null) {
			return palette;
		}

		java.util.PriorityQueue<KDTree> pq = new java.util.PriorityQueue<KDTree>(
				(a, b) -> Integer.compare(size(b), size(a)));

		pq.add(tree);

		while (palette.size() < maxpoints && !pq.isEmpty()) {
			KDTree current = pq.poll();
			if (current == null) {
				continue;
			}

			if (current.left == null && current.right == null) {
				palette.add(current.point);
			} else if (palette.size() + pq.size() + (current.left != null ? 1 : 0)
					+ (current.right != null ? 1 : 0) <= maxpoints) {
				if (current.left != null) {
					pq.add(current.left);
				}
				if (current.right != null) {
					pq.add(current.right);
				}
			} else {
				palette.add(average(current));
			}
		}

		while (!pq.isEmpty()) {
			palette.add(average(pq.poll()));
		}

		return palette;
	}

	public String pointToString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		if (this.point.length > 0)
			sb.append(this.point[0]);
		for (int i = 1; i < this.point.length; i++)
			sb.append("," + this.point[i]);
		sb.append("]");
		return sb.toString();
	}

}
