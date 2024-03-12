package com.iscas.graph.PerformanceAnalysis.IndicatorsWeight;

public class IndicatorsAdditive {
    // 加性权重运算
    public double[] IndicatorsWeight(double[][] matrix) {

        // 计算每一行指标的权重
        double[] rowWeights = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < matrix[0].length; j++) {
                sum += Math.abs(matrix[i][j]);
//                sum += matrix[i][j];
            }
            rowWeights[i] = sum;
        }

        return rowWeights;
    }
}
