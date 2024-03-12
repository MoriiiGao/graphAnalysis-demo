package com.iscas.graph.PerformanceAnalysis.IndicatorsWeight;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

public class IndicatorsSVD {
    // 奇异值分解(SVD)
    public static void main(String[] args) {
        // 指标矩阵
        double[][] matrixArray = {
                {8.0, 6.0, 0.0, 14.0},
                {0.0, 0.0, 3.0, 0.0},
                {0.0, 12.0, 11.0, 12.0},
                {0.0, 0.0, 5.0, 0.0},
                {6.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 12.0},
                {4.0, 0.0, 0.0, 12.0}
        };

        // 转换为RealMatrix对象
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixArray);

        // 进行SVD分解
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix U = svd.getU();

        // 计算每一行指标的权重
        double[] rowWeights = new double[U.getRowDimension()];
        for (int i = 0; i < U.getRowDimension(); i++) {
            double sum = 0.0;
            for (int j = 0; j < U.getColumnDimension(); j++) {
                sum += Math.abs(U.getEntry(i, j));
            }
            rowWeights[i] = sum;
        }

        // 输出结果
        for (double weight : rowWeights) {
            System.out.println(weight);
        }
    }
}
