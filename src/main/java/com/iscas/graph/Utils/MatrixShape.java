package com.iscas.graph.Utils;

public class MatrixShape {

    // 输出浮点矩阵形状
    public void printDoubleMatrixShape(double[][] matrix) {
        int rows = matrix.length; // 获取矩阵的行数
        int cols = matrix[0].length; // 获取矩阵的列数

        System.out.println("Matrix shape:" + rows + " 行" + "||" + cols + " 列");
    }

    public static void main(String[] args) {
        // 实例矩阵
        double[][] matrix = {
                {1.5, 0.0, 0.0, 0.0},
                {0.0, 0.0, 3.2, 0.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 5.7, 0.0, 0.0}
        };

        MatrixShape matrixShape = new MatrixShape();
        matrixShape.printDoubleMatrixShape(matrix);
    }

}
