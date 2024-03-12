package com.iscas.graph.PerformanceAnalysis.DataNormlization;

import com.iscas.graph.PerformanceAnalysis.NumCalFunc.LinearFunction;

import java.util.Arrays;

public class Softmax {


    public double[] softmax(double[] scores) {

        // 计算指数
        double[] expScores = new double[scores.length];
        for (int i = 0; i < scores.length; i ++) {
            expScores[i] = Math.exp(scores[i]);
        }

        // 计算总和
        double sum = 0;
        for (double score: expScores) {
            sum += score;
        }

        // 计算概率
        LinearFunction linearFunction = new LinearFunction();
        double[] probabilities = new double[scores.length];
        for (int i = 0; i < scores.length; i ++) {
            probabilities[i] = linearFunction.divide(expScores[i], sum) * 100;
        }

        return probabilities;
    }

    public static void main(String[] args) {
        double[] score = {123.07877802686446, 116.95177619717239, 51.35830664857592, 346.8401272314101, 255.30410493752834, 276.1335465217296};

        Softmax softmax = new Softmax();
        double[] probabilities = softmax.softmax(score);

        System.out.println(Arrays.toString(probabilities));

    }
}
