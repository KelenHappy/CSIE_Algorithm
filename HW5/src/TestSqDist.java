/**
 * This is a test class for the sqDist method in the KDTree class.
 * It includes several test cases to verify the correctness of the sqDist method.
 *
 * @author Kélen
 *
 */
public class TestSqDist {
    public static void main(String[] args) {
        // Test case 1: 2D points from the problem description
        double[] p1 = {-1, 1};
        double[] p2 = {1, -1};
        System.out.println("Test Case 1 (2D):");
        System.out.println("sqDist(p1, p2): " + KDTree.sqDist(p1, p2)); // Expected: 8.0

        // Test case 2: 3D points
        double[] p3 = {1, 2, 3};
        double[] p4 = {4, 5, 6};
        System.out.println("\nTest Case 2 (3D):");
        System.out.println("sqDist(p3, p4): " + KDTree.sqDist(p3, p4)); // Expected: 27.0

        // Test case 3: 1D points
        double[] p5 = {10};
        double[] p6 = {20};
        System.out.println("\nTest Case 3 (1D):");
        System.out.println("sqDist(p5, p6): " + KDTree.sqDist(p5, p6)); // Expected: 100.0

        // Test case 4: Same points
        double[] p7 = {1, 2, 3};
        double[] p8 = {1, 2, 3};
        System.out.println("\nTest Case 4 (Same points):");
        System.out.println("sqDist(p7, p8): " + KDTree.sqDist(p7, p8)); // Expected: 0.0

        // Test case 5: Zero-dimension points (empty arrays)
        double[] p9 = {};
        double[] p10 = {};
        System.out.println("\nTest Case 5 (0D):");
        System.out.println("sqDist(p9, p10): " + KDTree.sqDist(p9, p10)); // Expected: 0.0
    }
}
