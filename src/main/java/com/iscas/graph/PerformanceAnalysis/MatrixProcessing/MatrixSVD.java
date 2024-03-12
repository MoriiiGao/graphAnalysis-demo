package com.iscas.graph.PerformanceAnalysis.MatrixProcessing;
import com.iscas.graph.PerformanceAnalysis.NumCalFunc.NonLinearFunction;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;

import java.util.Arrays;

public class MatrixSVD {

//    private String WeightingMethod; // 权重计算方法
//
//    public MatrixSVD(String WeightingMethod) {
//        this.WeightingMethod = WeightingMethod;
//    }

    public static double[][] convertRealMatrix(RealMatrix matrix) {
        double[][] result = matrix.getData();
        return result;
    }

    public double[] IndicatorsWeight(double[][] matrix) {

        // 计算每一行指标的权重
        double[] rowWeights = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < matrix[0].length; j++) {
                sum += Math.abs(matrix[i][j]);
            }
            rowWeights[i] = sum;
        }

        return rowWeights;
    }

    public double[] IndicatorsWeight1(double[][] matrix) {

        // 计算每一行指标的权重
        double[] rowWeights = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            double sum = 0.0;
            for (int j = 0; j < matrix[0].length; j++) {
                sum += Math.abs(matrix[i][j]);
            }
            rowWeights[i] = sum;
        }
        return rowWeights;
    }

    // 矩阵稠密化
    public double[] SVD1(double[][] matrixArray) {

        // 转换为RealMatrix对象
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixArray);

        // 进行SVD分解
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix U = svd.getU();

        // 将RealMatrix类型的矩阵 转为double [][] 类型矩阵
        double[][] MatrixU = MatrixSVD.convertRealMatrix(U);

        // 非线性映射 将做奇异矩阵映射到[0, 1]空间内
        double[][] nonlinearMatrix = new double[MatrixU.length][MatrixU[0].length];
        NonLinearFunction sigmoid = new NonLinearFunction();
        for (int i = 0; i < MatrixU.length; i ++) {
            for (int j = 0; j < MatrixU[i].length; j ++) {
                nonlinearMatrix[i][j] = sigmoid.Sigmoid(MatrixU[i][j]);
            }
        }

        for (int i =0; i < MatrixU.length; i ++) {
            System.out.println(Arrays.toString(nonlinearMatrix[i]));
        }

        // 初始化权重矩阵
        double[] rowWeights = new double[MatrixU.length];

//        if (WeightingMethod.equals("Additive")) {
//            // 指标权重计算--加性计算 Additive
//            IndicatorsAdditive indicatorsAdditive = new IndicatorsAdditive();
//            rowWeights = indicatorsAdditive.IndicatorsWeight(MatrixU);
//        } else if (WeightingMethod.equals("Entropy")) {
//            // 指标权重计算--熵值计算 Entropy
//            IndicatorsEntropy indicatorsEntropy = new IndicatorsEntropy();
//            rowWeights = indicatorsEntropy.calculateEntropies(nonlinearMatrix);
//        } else if (WeightingMethod.equals("PCA")) {
//            // 指标权重计算--主成分分析
//            IndicatorsPCA indicatorsPCA = new IndicatorsPCA();
//            indicatorsPCA.PCA(nonlinearMatrix);
//        }
        return rowWeights;
    }

    public double[][] SVD(double[][] matrixArray) {
        // 转换为RealMatrix对象
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixArray);

        // 进行SVD分解
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        RealMatrix U = svd.getU();

        // 将RealMatrix类型的矩阵 转为double [][] 类型矩阵
        double[][] MatrixU = MatrixSVD.convertRealMatrix(U);

        return MatrixU;
    }

    public static void main(String[] args) {
        // 指标矩阵
        double[][] matrixArray = {
                {8.0, 6.0, 0.0, 100.0, 35.0, 0.0},
                {0.0, 0.0, 3.0, 0.0, 0.0, 60.0},
                {0.0, 30.0, 11.0, 12.0, 0.0, 55.0},
                {0.0, 0.0, 0.0, 0.0, 0.0, 40.0},
                {0.0, 0.0, 5.0, 0.0, 0.0, 45.0},
                {6.0, 0.0, 0.0, 0.0, 5.0, 0.0},
                {4.0, 0.0, 0.0, 12.0, 0.0, 10.0},
                {60.0, 0.0, 0.0, 0.0, 75.0, 0.0},
                {0.0, 0.0, 0.0, 0.0, 46.0, 0.0},
                {0.0, 45.7, 17.0, 0.0, 3.0, 0.0}
        };

        MatrixSVD matrixSVD = new MatrixSVD();
        double[][] row = matrixSVD.SVD(matrixArray);

    }
}
