package com.iscas.graph.PerformanceAnalysis.DataNormlization;

import com.iscas.graph.PerformanceAnalysis.NumCalFunc.LinearFunction;

import java.util.Arrays;

public class BatchNormalization {

    public double[] batchNormalization(double[] data) {

        LinearFunction linearFunction = new LinearFunction();
        BatchNormalization batchNormalization = new BatchNormalization();
        // 计算均值
        double mean = linearFunction.calculateMean(data);
        // 计算方差
        double variance = linearFunction.calculateVariance(data, mean);
        // 归一化操作
        double[] normalizedData = batchNormalization.normalizeData(data, mean, variance);

        return normalizedData;
    }

    // 归一化 ẋ = (x - μ) / √(σ^2 + ε)
    public double[] normalizeData(double[] data, double mean, double variance) {
        // 避免方差为0的情况出现
        double epsilon = 1e-8;
        // 初始化归一化数组
        double[] normalizedData = new double[data.length];
        for (int i = 0; i < data.length; i ++) {
            normalizedData[i] = (data[i] - mean) / Math.sqrt(variance + epsilon);
        }
        return normalizedData;

    }


    public static void main(String[] args) {
        double[] data = {0.0, 1000.0, 0.4, 200.0, 30.0, 7.1, 100.0, 0.0};

        BatchNormalization batchNormalization = new BatchNormalization();
        double[] data1 = batchNormalization.batchNormalization(data);
        System.out.println(Arrays.toString(data1
        ));

    }
}
