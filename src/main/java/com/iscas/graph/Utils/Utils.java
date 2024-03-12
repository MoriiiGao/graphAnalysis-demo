package com.iscas.graph.Utils;
import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.tomcat.util.digester.ArrayStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     *
     * 生成能力指标重要度数组，满足Dirichlet分布
     *
     * @param n 能力指标个数
     * @RETURN 能力指标重要度数组
     *
     * 问题1：打印完整的数组Arrays.toString(ArrayList<>())
     *
     *  List<>列表可以直接打印 数组不可以
     *
     * */
    public double[] randomizedGeneratingCapacityIndicatorWeights(int n) {
        // 生成n个Beta分布随机数
        double[] betaValues = new double[n];
        double sum = 0;
        BetaDistribution betaDistribution = new BetaDistribution(1, n-1);

        for (int i = 0; i < n ; i ++) {;
            betaValues[i] = betaDistribution.sample();
            sum += betaValues[i];
        }
        //归一化 使得和为0
        for (int i = 0; i < n; i ++) {
            betaValues[i] /= sum;
        }

        return betaValues;
    }


    /**
     * 随机生成能力满足度数组
     *
     * @param n 数组大小
     *
     *
     * */
    public double[] generationAbilityFulfillment(int n) {
        double low = 0.5;
        double high = 0.9;

        double[] abilityFulfillment = generateUniformRandomNumbers(n, low, high);

        return abilityFulfillment;
    }

    /**
     * 生成指定范围内的Double行随机数组
     *
     * */
    public double[] generateUniformRandomNumbers(int n, double low, double high) {
        double[] randomNumbers = new double[n];
//        List<Double> randomNumbers = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < n; i ++) {
            double randomNum = low + (high - low) * random.nextDouble();
//            System.out.println(randomNum);
            randomNumbers[i] =randomNum ;
//            randomNumbers.add(randomNum);
        }

        return randomNumbers;
    }

    // 随机打乱数据元素
    public void shuffleArray(double[] array) {
        Random random = new Random();
        int n = array.length;
        for (int i = 0; i < n; i ++) {
            int j = random.nextInt(n);
            double temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

    }

    // 正则提取浮点数
    public void extractDouble() {
        List<String> strings = new ArrayList<>();
        strings.add("10.0KM");
        strings.add("100kg");
        strings.add("200公里");

        List<Number> numbers = new ArrayList<>();
        // 匹配浮点数和整数
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        for (String s: strings) {
            Matcher matcher = pattern.matcher(s);
            while (matcher.find()) {
                String matchedNumber = matcher.group();
                // 判断整形还是浮点型
                if (matchedNumber.contains(".")) {
                    numbers.add(Double.parseDouble(matchedNumber));
                } else {
                    numbers.add(Integer.parseInt(matchedNumber));
                }
            }
        }

        for (Number number : numbers) {
            if (number instanceof Double) {
                System.out.println(number + " floating-point");
            } else {
                System.out.println(number + " is an integer");
            }
        }
    }


    public static void main(String[] args) {
        Utils utils = new Utils();
//        double[] values = utils.randomizedGeneratingCapacityIndicatorWeights(10);
//        System.out.println(Arrays.toString(values));
//        double[] randomNumbers = utils.generateUniformRandomNumbers(10, 0.1, 0.8);
//        System.out.println(Arrays.toString(randomNumbers));

        utils.extractDouble();
    }

}
