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

	boolean compare(double[] a) {
		throw(new Error("TODO"));
	}

	static KDTree insert(KDTree tree, double[] p) {
		throw(new Error("TODO"));
	}

	static double sqDist(double[] a, double[] b) {
		throw(new Error("TODO"));
	}

	static double[] closestNaive(KDTree tree, double[] a, double[] champion) {
		throw(new Error("TODO"));
	}


	static double[] closestNaive(KDTree tree, double[] a) {
		throw(new Error("TODO"));
	}

	static double[] closest(KDTree tree, double[] a, double[] champion) {
		if (tree == null)
			return champion;

		// sert pour InteractiveClosest.
		InteractiveClosest.trace(tree.point, champion);
		
		throw(new Error("TODO"));
	}

	static double[] closest(KDTree tree, double[] a) {
		throw(new Error("TODO"));
	}

	static int size(KDTree tree) {
		throw(new Error("TODO"));
	}

	static void sum(KDTree tree, double[] acc) {
		throw(new Error("TODO"));
	}

	static double[] average(KDTree tree) {
		throw(new Error("TODO"));
	}


	static Vector<double[]> palette(KDTree tree, int maxpoints) {
		throw(new Error("TODO"));
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
