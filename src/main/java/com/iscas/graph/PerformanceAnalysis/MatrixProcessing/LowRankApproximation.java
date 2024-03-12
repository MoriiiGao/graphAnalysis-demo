package com.iscas.graph.PerformanceAnalysis.MatrixProcessing;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import java.util.Arrays;

public class LowRankApproximation {

//    private int k;
//
//    public LowRankApproximation(int k) {
//        this.k = k;
//    }
//
//    public LowRankApproximation() {
//        this.k = 2;
//    }

    public double[][] lowRankApproximation (double[][] sparseMatrixData) {

        // 将稀疏矩阵转换为实数矩阵
        RealMatrix sparseMatrix = MatrixUtils.createRealMatrix(sparseMatrixData);

        // 进行奇异值分解
        SingularValueDecomposition svd = new SingularValueDecomposition(sparseMatrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getVT();

        int k = 2;

        // 截断奇异值和正交矩阵
        RealMatrix S_truncated = S.getSubMatrix(0, k - 1, 0, k - 1);
        RealMatrix U_truncated = U.getSubMatrix(0, U.getRowDimension() - 1, 0, k - 1);
        RealMatrix V_truncated = V.getSubMatrix(0, k - 1, 0, V.getColumnDimension() - 1);

        // 生成lowrank矩阵
        RealMatrix approxMatrix = U_truncated.multiply(S_truncated).multiply(V_truncated.transpose());

        return approxMatrix.getData();
    }

    public static void main(String[] args) {
        // 指标矩阵
//        double[][] matrixArray = {
//                {8.0, 6.0, 0.0, 14.0},
//                {0.0, 0.0, 3.0, 0.0},
//                {0.0, 12.0, 11.0, 12.0},
//                {0.0, 0.0, 5.0, 0.0},
//                {6.0, 0.0, 0.0, 0.0},
//                {0.0, 0.0, 0.0, 12.0},
//                {4.0, 0.0, 0.0, 12.0}
//        };
//
//        LowRankApproximation lowRankApproximation = new LowRankApproximation();
//        double[][] matrix = lowRankApproximation.lowRankApproximation(matrixArray);
//        for (double[] row: matrix) {
//            System.out.println(Arrays.toString(row));
//        }
        // 创建稀疏矩阵
        double[][] sparseMatrixData = {
                {1, 0, 2},
                {0, 3, 0},
                {4, 0, 5}
        };

        // 将稀疏矩阵转换为实数矩阵
        RealMatrix sparseMatrix = MatrixUtils.createRealMatrix(sparseMatrixData);

        // 计算矩阵的秩
        int k = 2;

        // 进行奇异值分解
        SingularValueDecomposition svd = new SingularValueDecomposition(sparseMatrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getVT();



        // 截断奇异值和正交矩阵
        RealMatrix S_truncated = S.getSubMatrix(0, k - 1, 0, k - 1);
        RealMatrix U_truncated = U.getSubMatrix(0, U.getRowDimension() - 1, 0, k - 1);
        RealMatrix V_truncated = V.getSubMatrix(0, k - 1, 0, V.getColumnDimension() - 1);

        // 生成低秩近似矩阵
        RealMatrix approxMatrix = U_truncated.multiply(S_truncated).multiply(V_truncated.transpose());

        // 打印低秩近似矩阵
        System.out.println("Low-Rank Approximation Matrix:");
        for (double[] row : approxMatrix.getData()) {
            for (double element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }


    }
}
