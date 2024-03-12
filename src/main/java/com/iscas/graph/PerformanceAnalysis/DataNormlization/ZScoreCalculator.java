package com.iscas.graph.PerformanceAnalysis.DataNormlization;

import com.iscas.graph.PerformanceAnalysis.NumCalFunc.Sigmoid;

public class ZScoreCalculator {

    // 计算Z-Score分数
    public double[] calculateZScore(double[] data){

        ZScoreCalculator zScoreCalculator = new ZScoreCalculator();
        // 计算数据均值
        double mean = zScoreCalculator.calculateMean(data);
        // 计算数据标准差
        double stdDev = zScoreCalculator.calculateStdDev(data, mean);

        double[] zScores = new double[data.length];

        for (int i = 0; i < data.length; i ++) {
            zScores[i] = (data[i] - mean) / stdDev;
        }
        return zScores;
    }

    // 均值
    public double calculateMean(double[] data) {
        double sum = 0;

        for (double value: data) {
            sum += value;
        }

        return sum / data.length;
    }

    // 标准差
    public double calculateStdDev(double[] data, double mean) {
        double sumOfSquaredDifferences = 0.0;

        for (double value : data) {
            double difference = value - mean;
            sumOfSquaredDifferences += difference * difference;
        }

        double variance = sumOfSquaredDifferences / data.length;

        return Math.sqrt(variance);

    }

    public static void main(String[] args){
        double[] data = {5.2, 7.9, 6.5, 8.2, 6.8, 7.1};
        double[] data1 = {0.0, 1000.0, 0.4, 200.0, 30.0, 7.1, 100.0};

        ZScoreCalculator zScoreCalculator = new ZScoreCalculator();
        double[] zScores = zScoreCalculator.calculateZScore(data1);

        Sigmoid sigmoid = new Sigmoid();
        for (int i = 0; i < zScores.length; i ++) {
            System.out.println("old:" + data1[i]);
            System.out.println("new:" + sigmoid.sigmoid(zScores[i]));
            System.out.println("==============");
        }


    }
}
