package com.iscas.graph.PerformanceAnalysis.MatrixProcessing;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class MatrixDensificationSVD {
    public static void main(String[] args) {
        double[][] matrixArray = {
                {8.0, 6.0, 0.0, 14.0},
                {0.0, 0.0, 3.0, 0.0},
                {0.0, 12.0, 11.0, 12.0},
                {0.0, 0.0, 5.0, 0.0},
                {6.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 12.0},
                {4.0, 0.0, 0.0, 12.0}
        };

        // 将矩阵数组转换为 RealMatrix
        RealMatrix matrix = new Array2DRowRealMatrix(matrixArray);

        // 执行奇异值分解
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);

        // 获取奇异值矩阵
        RealMatrix sMatrix = svd.getS();
        double[] singularValues = svd.getSingularValues();

        // 设置截断阈值，仅保留大于阈值的奇异值
        double threshold = 0.00001;

        // 根据阈值稠密化奇异值矩阵
        for (int i = 0; i < singularValues.length; i++) {
            if (Math.abs(singularValues[i]) < threshold) {
                sMatrix.setEntry(i, i, 0.0);
            }
        }

        // 使用稠密化后的奇异值矩阵重构原始矩阵
        RealMatrix reconstructedMatrix = svd.getU().multiply(sMatrix).multiply(svd.getVT());

        // 打印重构后的矩阵
        double[][] reconstructedArray = reconstructedMatrix.getData();
        for (double[] row : reconstructedArray) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}