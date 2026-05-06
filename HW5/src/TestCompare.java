/**
 * This is a test class for the compare method in the KDTree class.
 * It includes several test cases to verify the correctness of the compare method.
 *
 * @author Kélen
 *
 */
public class TestCompare {
    public static void main(String[] args) {
        // Test case 1: 2D point, depth 0 (compare x-coordinate)
        double[] p1 = {5.0, 3.0};
        KDTree node1 = new KDTree(p1, 0);
        double[] p2 = {7.0, 2.0}; // Should go right (true)
        double[] p3 = {3.0, 4.0}; // Should go left (false)
        System.out.println("Test Case 1 (2D, depth 0):");
        System.out.println("p2 vs p1: " + node1.compare(p2)); // Expected: true
        System.out.println("p3 vs p1: " + node1.compare(p3)); // Expected: false

        // Test case 2: 2D point, depth 1 (compare y-coordinate)
        double[] p4 = {5.0, 3.0};
        KDTree node2 = new KDTree(p4, 1);
        double[] p5 = {7.0, 4.0}; // Should go right (true)
        double[] p6 = {3.0, 2.0}; // Should go left (false)
        System.out.println("\nTest Case 2 (2D, depth 1):");
        System.out.println("p5 vs p4: " + node2.compare(p5)); // Expected: true
        System.out.println("p6 vs p4: " + node2.compare(p6)); // Expected: false

        // Test case 3: 3D point, depth 0 (compare x-coordinate)
        double[] p7 = {5.0, 3.0, 8.0};
        KDTree node3 = new KDTree(p7, 0);
        double[] p8 = {7.0, 2.0, 9.0}; // Should go right (true)
        double[] p9 = {3.0, 4.0, 7.0}; // Should go left (false)
        System.out.println("\nTest Case 3 (3D, depth 0):");
        System.out.println("p8 vs p7: " + node3.compare(p8)); // Expected: true
        System.out.println("p9 vs p7: " + node3.compare(p9)); // Expected: false

        // Test case 4: 3D point, depth 1 (compare y-coordinate)
        double[] p10 = {5.0, 3.0, 8.0};
        KDTree node4 = new KDTree(p10, 1);
        double[] p11 = {7.0, 4.0, 9.0}; // Should go right (true)
        double[] p12 = {3.0, 2.0, 7.0}; // Should go left (false)
        System.out.println("\nTest Case 4 (3D, depth 1):");
        System.out.println("p11 vs p10: " + node4.compare(p11)); // Expected: true
        System.out.println("p12 vs p10: " + node4.compare(p12)); // Expected: false

        // Test case 5: 3D point, depth 2 (compare z-coordinate)
        double[] p13 = {5.0, 3.0, 8.0};
        KDTree node5 = new KDTree(p13, 2);
        double[] p14 = {7.0, 2.0, 9.0}; // Should go right (true)
        double[] p15 = {3.0, 4.0, 7.0}; // Should go left (false)
        System.out.println("\nTest Case 5 (3D, depth 2):");
        System.out.println("p14 vs p13: " + node5.compare(p14)); // Expected: true
        System.out.println("p15 vs p13: " + node5.compare(p15)); // Expected: false
    }
}
