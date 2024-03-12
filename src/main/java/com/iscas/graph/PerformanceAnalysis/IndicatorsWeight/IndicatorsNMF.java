package com.iscas.graph.PerformanceAnalysis.IndicatorsWeight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class IndicatorsNMF {
    public static void main(String[] args) {
        double[][] data = {
                {8.0, 6.0, 0.0, 14.0},
                {0.0, 0.0, 3.0, 0.0},
                {0.0, 12.0, 11.0, 12.0},
                {0.0, 0.0, 5.0, 0.0},
                {6.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 12.0},
                {4.0, 0.0, 0.0, 12.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(data);
        int numRows = matrix.getRowDimension();
        int numCols = matrix.getColumnDimension();
        int k = 2;  // 特征维度

        // 随机初始化 W 和 H 矩阵
        RealMatrix wMatrix = new Array2DRowRealMatrix(numRows, k);
        RealMatrix hMatrix = new Array2DRowRealMatrix(k, numCols);
        wMatrix.setEntry(0, 0, 1.0);
        wMatrix.setEntry(1, 1, 1.0);
        hMatrix.setEntry(0, 0, 1.0);
        hMatrix.setEntry(1, 1, 1.0);

        // 迭代优化 W 和 H
        double epsilon = 1e-5;  // 迭代停止的阈值
        int maxIterations = 100;  // 最大迭代次数

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            RealMatrix whMatrix = wMatrix.multiply(hMatrix);
            RealMatrix diffMatrix = matrix.subtract(whMatrix);

            // 更新 W 矩阵
            RealMatrix numeratorW = matrix.multiply(hMatrix.transpose());
            RealMatrix denominatorW = whMatrix.multiply(hMatrix.transpose());
            wMatrix = wMatrix.multiply(numeratorW.add(denominatorW));

            // 更新 H 矩阵
            RealMatrix numeratorH = wMatrix.transpose().multiply(matrix);
            RealMatrix denominatorH = wMatrix.transpose().multiply(whMatrix);
            hMatrix = hMatrix.multiply(numeratorH.add(denominatorH));

            // 计算误差，如果小于阈值则停止迭代
            RealMatrix reconstructedMatrix = wMatrix.multiply(hMatrix);
            double error = diffMatrix.getFrobeniusNorm();
            if (error < epsilon) {
                break;
            }
        }

        // 打印 W 矩阵
        for (int i = 0; i < wMatrix.getRowDimension(); i++) {
            System.out.print("W 第 " + (i + 1) + " 行: ");
            for (int j = 0; j < wMatrix.getColumnDimension(); j++) {
                System.out.print(wMatrix.getEntry(i, j) + " ");
            }
            System.out.println();
        }
    }

}
