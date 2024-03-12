package com.iscas.graph.PerformanceAnalysis.MatrixProcessing;

public class NegativeMatrixValidator {
    // 判断当前矩阵是否存在小于零的元素
    public boolean NegativeMatrix(double[][] matrix) {
        // 遍历矩阵的每个元素，判断是否存在非负数
        for (int i = 0; i < matrix.length; i ++) {
            for (int j = 0; j < matrix[i].length; j ++) {
                if (matrix[i][j] <= 0) {
                    return true; // 存在负矩阵
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        double[][] matrix = {
                {1.0, -2.0, -3},
                {-4.0, 5, -6},
                {-7, 8, 9}
            };

        NegativeMatrixValidator negativeMatrixValidator = new NegativeMatrixValidator();
        if (negativeMatrixValidator.NegativeMatrix(matrix)) {
            System.out.println("the matrix is not a negative matrix");
        } else {
            System.out.println("the matrix is a negative matrix");
        }
    }

}
