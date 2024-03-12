package com.iscas.graph.PerformanceAnalysis.NumCalFunc;

import java.text.DecimalFormat;

public class LinearFunction {

    public double divide (double dividend, double divisor) {
        if (divisor == 0.0) {
            throw new ArithmeticException("除数不能为零");
        }

        double res = dividend / divisor;
        // 将结果格式化为两位小数
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(res));
    }

    // 均值计算
    public double calculateMean(double[] data) {
        double sum = 0;

        for (double value: data) {
            sum += value;
        }

        return sum / data.length;
    }

    // 标准差计算
    public double calculateStdDev(double[] data, double mean) {
        double sumOfSquaredDifferences = 0.0;

        for (double value : data) {
            double difference = value - mean;
            sumOfSquaredDifferences += difference * difference;
        }

        double variance = sumOfSquaredDifferences / data.length;

        return Math.sqrt(variance);
    }

    // 方差计算
    // σ^2 = 1/m * Σ(x - μ)^2
    public double calculateVariance(double[] data, double mean) {
        double sum = 0.0;
        for (double value: data) {
            sum += Math.pow(value - mean, 2);
        }
        return sum / data.length;

    }


}
