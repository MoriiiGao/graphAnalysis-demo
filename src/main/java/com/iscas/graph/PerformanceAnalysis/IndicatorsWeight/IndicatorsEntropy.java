package com.iscas.graph.PerformanceAnalysis.IndicatorsWeight;

import java.util.ArrayList;
import java.util.List;

public class IndicatorsEntropy {

    public double[] calculateEntropies(double[][] normzlizedScores) {
        // 项目个数
        int numProjects = normzlizedScores.length;
        double[] projectEntropies = new double[numProjects];

        for (int i = 0; i < numProjects; i ++) {

            final int index = i;

            // 获取单个项目性能指标归一化后的分数
            double[] rowValues = normzlizedScores[index];
            // 初始化熵值
            double entropy = 0.0;
            // 过滤出大于0的值
            List<Double> filteredValues = new ArrayList<>();
            for (double value : rowValues) {
                if (value > 0) {
                    filteredValues.add(value);
                }
            }

            // 计算熵
            for (double value : rowValues) {
                double logValue = Math.log(value);
                entropy -= value * logValue;
            }
            projectEntropies[i] = entropy;
        }
        return projectEntropies;
    }
}
