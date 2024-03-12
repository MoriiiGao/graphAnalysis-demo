package com.iscas.graph.PerformanceAnalysis.MatrixProcessing;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public class BayesianRegressionExample {
    // 贝叶斯回归
    public static void main(String[] args) {
        // 权重矩阵
        double[][] weights = {
                {0.567361834739465, -0.3473977382212329, 0.42987395701536746, 0.27580137143949757},
                {0.026428616802097918, 0.1570617983522099, 0.021051751405027606, -0.4111611517837113},
                {0.6209422817214059, 0.7211296388250621, 0.017533719796588104, 0.14126556307456406},
                {0.044047694670163194, 0.26176966392035, 0.035086252341712844, -0.6852685863061851},
                {0.04746547383660841, -0.17516788271840966, 0.6468305845669055, -0.3489506271700645},
                {0.3630837893506954, -0.2796881255452494, -0.6041082070943753, -0.12503902821175406},
                {0.3947274385751011, -0.3964667140241889, -0.1728878173831047, -0.3576727796584635}
        };

        // 观测数据
        double[] observations = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0};

        // 贝叶斯回归
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        // 构建设计矩阵
        double[][] designMatrix = new double[weights.length][weights[0].length + 1];
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                designMatrix[i][j + 1] = weights[i][j];
            }
            designMatrix[i][0] = 1.0;
        }

        // 设置响应变量
        regression.newSampleData(observations, designMatrix);

        // 计算权重估计值
        double[] coefficients = regression.estimateRegressionParameters();
        double[] weightsEstimate = new double[coefficients.length - 1];
        for (int i = 0; i < weightsEstimate.length; i++) {
            weightsEstimate[i] = coefficients[i + 1];
        }

        System.out.println("Estimated weights:");
        for (double weight : weightsEstimate) {
            System.out.println(weight);
        }
    }
}