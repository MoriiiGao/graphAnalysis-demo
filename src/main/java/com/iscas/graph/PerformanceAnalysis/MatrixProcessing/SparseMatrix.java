package com.iscas.graph.PerformanceAnalysis.MatrixProcessing;


/**
 *

 * 阈值判定：根据具体的定义和需求，可以设置一个阈值，用于确定稀疏矩阵的非零元素数量的上限。
 * 例如，可以将非零元素数量小于某个阈值的矩阵定义为稀疏矩阵。
 *
 * 零元素的特点：稀疏矩阵通常具有大量的零元素，并且零元素相对集中分布在矩阵的某些区域或具有某种规律性。
 * 通过观察矩阵中零元素的分布情况，可以初步判断矩阵是否是稀疏的。
 *
 * 压缩稀疏表示：使用稀疏矩阵的压缩表示方式可以进一步确认矩阵是否为稀疏。
 * 例如，使用压缩稀疏矩阵表示方法（如CSR、CSC等），可以将稀疏矩阵进行压缩存储，减少非零元素的存储空间。如果在压缩表示下，矩阵的存储需求大幅减少，那么可以确认矩阵是稀疏的。
 * */

public class SparseMatrix {

    // 计算矩阵密度
    public double calculateDensity(double[][] matrix) {
        /**
         * 密度计算：稀疏矩阵的密度（Density）较低，可以通过计算非零元素的比例来确定密度。
         * 密度越低，矩阵越稀疏。常见的定义是，矩阵的密度等于非零元素的数量除以总元素的数量。如果密度远低于1，则可以将矩阵视为稀疏矩阵。
         *
         * */
        // 元素个数
        int totalElements = matrix.length * matrix[0].length;
        // 非零元素个数次
        int nonZeroElements = 0;
        // 统计非零元素个数
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j ++) {
                if (matrix[i][j] != 0) {
                    nonZeroElements++;
                }
            }
        }
        // 计算稀疏矩阵的密度
        double density = (double) nonZeroElements / totalElements;

        return density;
    }


    public static void main(String[] args){
        // 实例矩阵
        double[][] matrix = {
                {1.5, 0.0, 0.0, 0.0},
                {0.0, 0.0, 3.2, 0.0},
                {0.0, 0.0, 0.0, 0.0},
                {0.0, 5.7, 0.0, 0.0}
        };

        SparseMatrix sparseMatrix = new SparseMatrix();
        double density = sparseMatrix.calculateDensity(matrix);
        System.out.println(density);

    }
}
