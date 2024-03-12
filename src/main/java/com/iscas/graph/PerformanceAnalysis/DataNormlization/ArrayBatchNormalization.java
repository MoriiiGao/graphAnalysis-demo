package com.iscas.graph.PerformanceAnalysis.DataNormlization;

import com.iscas.graph.PerformanceAnalysis.MatrixProcessing.NegativeMatrixValidator;
import com.iscas.graph.PerformanceAnalysis.NumCalFunc.NonLinearFunction;

import java.util.Arrays;

public class ArrayBatchNormalization {
    /**
     * 矩阵归一化
     * */
    public double[][] normalize(double[][] data) {

        // 计算每列的均值
        double[] means = new double[data.length];
        for (int i = 0; i < data.length; i ++) {
            double sum = 0.0;
            for (int j = 0; j < data[i].length; j ++) {
                sum += data[i][j];
            }
            means[i] = sum / data[i].length;
        }

        // 计算每行的标准差
        NonLinearFunction nonLinearFunction = new NonLinearFunction();
        double[] stdDevs = new double[data.length];
        for (int i = 0; i < data.length; i ++) {
            double sumSquares = 0.0;
            for (int j = 0; j < data[i].length; j++) {
                sumSquares += nonLinearFunction.Pow(data[i][j] - means[i], 2);
            }
            stdDevs[i] = nonLinearFunction.Sqrt(sumSquares / data[i].length);
        }

        // 归一化矩阵
        double[][] normalizedData = new double[data.length][data[0].length];
        for (int i = 0; i < data.length; i ++) {
            for (int j = 0; j < data[i].length; j ++) {
                normalizedData[i][j] = (data[i][j] - means[i]) / stdDevs[i];
            }
        }

        return normalizedData;
    }


    public static void main(String[] args) {
        double[][] data = {
                {8.0, 6.0, 0.0, 100.0, 35.0, 0.0},
                {0.0, 0.0, 3.0, 0.0, 0.0, 60.0},
                {0.0, 30.0, 11.0, 12.0, 0.0, 55.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 40.0},
                {0.0, 0.0, 5.0, 0.0, 0.0, 45.0},
                {6.0, 0.0, 0.0, 0.0, 5.0, 0.0},
                {0.0, 0.0, 0.0, 120.0, 0.0, 0.0},
                {4.0, 0.0, 0.0, 12.0, 0.0, 10.0},
                {60.0, 0.0, 0.0, 0.0, 75.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 46.0, 0.0},
                {0.0, 45.7, 17.0, 0.0, 3.0, 0.0}
        };
        // 归一化
        ArrayBatchNormalization arrayBatchNormalization = new ArrayBatchNormalization();
        double[][] normzlizedData = arrayBatchNormalization.normalize(data);
        NegativeMatrixValidator negativeMatrixValidator = new NegativeMatrixValidator();
        if (negativeMatrixValidator.NegativeMatrix(normzlizedData)) {
            System.out.println("================================负矩阵============================================");
            System.out.println("================================矩阵非线性映射=====================================");
            // 非线性映射 将做奇异矩阵映射到[0, 1]空间内
//                double[][] nonlinearMatrix = new double[indiactorsMatrix.length][indiactorsMatrix[0].length];
            NonLinearFunction sigmoid = new NonLinearFunction();
            for (int i = 0; i < normzlizedData.length; i ++) {
                for (int j = 0; j < normzlizedData[i].length; j ++) {
                    normzlizedData[i][j] = sigmoid.Sigmoid(normzlizedData[i][j]);
                }
            }

            for (double[] row: normzlizedData) {
                System.out.println(Arrays.toString(row));
            }
        }





    }

}
