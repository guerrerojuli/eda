public class MinPathFinder {

    public int getMinPath(int[][] weightMatrix) {
        if (weightMatrix == null || weightMatrix.length == 0 || weightMatrix[0].length == 0) {
            throw new IllegalArgumentException("Matrix cannot be null or empty");
        }
        int rows = weightMatrix.length;
        int cols = weightMatrix[0].length;
        int[][] dp = new int[rows][cols];

        // Initialize the top left cell
        dp[0][0] = weightMatrix[0][0];

        // Initialize first column
        for (int i = 1; i < rows; i++) {
            dp[i][0] = dp[i - 1][0] + weightMatrix[i][0];
        }

        // Initialize first row
        for (int j = 1; j < cols; j++) {
            dp[0][j] = dp[0][j - 1] + weightMatrix[0][j];
        }

        // Fill up the dp table
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                dp[i][j] = weightMatrix[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[rows - 1][cols - 1];
    }

    public static void main(String[] args) {
        int[][] v = new int [][] {
            {2, 8, 32, 30},
            {12, 6, 18, 19},
            {1, 2, 4, 8},
            {1, 31, 1, 16}
        };
        MinPathFinder minPathFinder = new MinPathFinder();
        int ans = minPathFinder.getMinPath(v);
        System.out.println(ans);
    }
}
