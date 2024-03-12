package com.iscas.graph.PerformanceAnalysis.DataNormlization;

import com.iscas.graph.PerformanceAnalysis.NumCalFunc.NonLinearFunction;

import java.util.*;

public class ZScoreNormalization {
    /**
     * Z-Score：根据原始数据的均值和标准差将数据转换为正态分布
     * 公式：（X-mean）/std
     *
     * */
    // 计算Z-Score分数
    public double[] calculateZScore(double[] data){

        ZScoreNormalization zScoreCalculator = new ZScoreNormalization();
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
        double[] data1 = {0.0, 1000.0, 0.4, 200.0, 30.0, 7.1, 100.0, 0.0};

        ZScoreNormalization zScoreCalculator = new ZScoreNormalization();
        double[] zScores = zScoreCalculator.calculateZScore(data1);

        NonLinearFunction sigmoid = new NonLinearFunction();
        Map<Double, Double> Statistics = new HashMap<>();
        List<Double> statisitcs = new ArrayList<>();
        for (int i = 0; i < zScores.length; i ++) {
//            System.out.println("old:" + data1[i]);
//            System.out.println("new:" + sigmoid.sigmoid(zScores[i]));
//            System.out.println("==============");

            Statistics.put(data1[i], sigmoid.SubSigmoid(zScores[i]));
//            Statistics.put(data1[i], zScores[i]);
            statisitcs.add(zScores[i]);
        }

        // 将Map的键值对转换为 List
        List<Map.Entry<Double, Double>> list = new ArrayList<>(Statistics.entrySet());

        // 根据值进行排序
        Collections.sort(list, new Comparator<Map.Entry<Double, Double>>() {
            @Override
            public int compare(Map.Entry<Double, Double> entry1, Map.Entry<Double, Double> entry2) {
                return entry1.getValue().compareTo(entry2.getValue());
            }
        });

        // 创建一个有序的 LinkedHashMap
        Map<Double, Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Double, Double> entry: list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        // 打印排序后的Mao
        for (Map.Entry<Double, Double> entry: sortedMap.entrySet()) {
            System.out.println(entry.getKey() + "||" + entry.getValue());
        }
    }
}
