package com.iscas.graph.IndicatorSystem;
import java.util.Arrays;

public class EntropyWeightMethod {
    /**
     *
     * 计算指标权重
     * 利用信息熵来衡量各个指标的信息含量，通过计算信息熵来确定权重
     * 信息熵越大 指标被认为具有更多的信息含量 权重越小
     *
     * */

    public static void main(String[] args) {
        // 输入数据: 三个项目，每个项目在A、B、C三个指标上的得分
        double[][] scores = {
                {80, 90, 75},
                {70, 85, 80},
                {75, 80, 85}
        };

        EntropyWeightMethod entropyWeightMethod = new EntropyWeightMethod();
        entropyWeightMethod.EntropyWeightAnalysis(scores);
    }

    public void EntropyWeightAnalysis(double[][] scores) {
        // 步骤1：归一化
        double[][] normalizedScores = normalize(scores);
        // 步骤2：计算信息熵
        double[] entropies = calculateEntropies(normalizedScores);
        // 步骤3：计算权重
        double[] weights = calculateWeights(entropies);
        System.out.println(Arrays.toString(weights));
        // 步骤4：求和计算
    }

    // 步骤1：归一化
    private static double[][] normalize(double[][] scores) {
        double[][] normalizedScores = new double[scores.length][scores[0].length];
        for (int i = 0; i < scores.length; i++) {
            for (int j = 0; j < scores[i].length; j++) {
                // 将j复制到effectively final的变量 index
                final int index = j;
                /**
                 * Arrays.stream(scores): 将二维数组 scores 转换为流(stream)
                 * .mapToDouble(row -> row[index]) 对于每一行 提取指定列index的值 将其转为double类型
                 * .min().orElse(0):计算该列中的最大值 如果如果没有值，默认为0
                 * */
                double min = Arrays.stream(scores).mapToDouble(row -> row[index]).min().orElse(0);
                double max = Arrays.stream(scores).mapToDouble(row -> row[index]).max().orElse(0);
                normalizedScores[i][j] = (scores[i][j] - min) / (max - min);
            }
        }

        return normalizedScores;
    }

    // 步骤2:计算信息熵
    private static double[] calculateEntropies(double[][] normalizedScores) {
        // 指标个数
        int numIndicators = normalizedScores[0].length;
        double[] entropies = new double[numIndicators];

        for (int i = 0; i < numIndicators; i ++) {
            /**
             * [1.0, 1.0, 0.0]
             * [0.0, 0.5, 0.5]
             * [0.5, 0.0, 1.0]
             *
             * */
            // 将 i 复制到effectively final 的变量
            final int index = i;
            // 特定列所有的值
            double[] column = Arrays.stream(normalizedScores).mapToDouble(row -> row[index]).toArray();
            /**
             * [1.0, 0.0, 0.5]
             * [1.0, 0.5, 0.0]
             * [0.0, 0.5, 1.0]
             * */
            // 计算熵值
            /**
             * Arrays.stream(column) 转为流
             * filter(value -> value -> 0) 过滤掉小于等0的元素
             * map(value -> value * Math.log(value)) 对流中的每个value进行value * Math.log(value)计算
             * */
            double entropy = -Arrays.stream(column)
                    .filter(value -> value > 0)
                    .map(value -> value * Math.log(value)).sum();
            System.out.println(entropy);
            entropies[i] = entropy;
        }
        return entropies;
    }

    // 步骤3：计算权重
    private static double[] calculateWeights(double[] entropies) {
        double totalEntropy = Arrays.stream(entropies).sum();
        return Arrays.stream(entropies)
                .map(entropy -> 1 - entropy / totalEntropy)
                .toArray();
    }

}
