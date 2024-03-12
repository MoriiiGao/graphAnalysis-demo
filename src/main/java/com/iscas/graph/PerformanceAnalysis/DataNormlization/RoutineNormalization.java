package com.iscas.graph.PerformanceAnalysis.DataNormlization;

import com.iscas.graph.PerformanceAnalysis.NumCalFunc.LinearFunction;

import java.util.*;

public class RoutineNormalization {
    /**
     * 常规 归一化函数
     *
     * */
    public double[] routineNormalizationArray(double[] scores) {
        // 计算总和
        double sum = 0;
        for (double score: scores) {
            sum += score;
        }

        // 归一化
        LinearFunction linearFunction = new LinearFunction();
        double[] norm = new double[scores.length];
        for (int i = 0; i < scores.length; i ++) {
            norm[i] = linearFunction.divide(scores[i], sum);
        }

        return norm;
    }

    public Map<String, Double> routineNormalizationMap(Map<String, Double> scores) {
        // 计算总和
        double sum = 0;
        for (Map.Entry<String, Double> score: scores.entrySet()) {
            sum += score.getValue();
        }

        // 归一化
        Map<String, Double> newScores = new HashMap<>();
        LinearFunction linearFunction = new LinearFunction();
        for (Map.Entry<String, Double> score: scores.entrySet()) {
            newScores.put(score.getKey(), linearFunction.divide(score.getValue(), sum));
        }

        return newScores;
    }

    public static void main(String[] args) {

        double[] score = {123.07877802686446, 116.95177619717239, 51.35830664857592, 346.8401272314101, 255.30410493752834, 276.1335465217296};

        RoutineNormalization routineNormalization = new RoutineNormalization();
        double[] Norm = routineNormalization.routineNormalizationArray(score);
        System.out.println(Arrays.toString(Norm));
        for (int i = 0; i < Norm.length; i ++) {
            System.out.println(Norm[i] * 100 + "%");
        }

    }

}
