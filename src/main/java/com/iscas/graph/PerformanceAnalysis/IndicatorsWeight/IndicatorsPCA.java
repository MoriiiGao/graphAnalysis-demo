package com.iscas.graph.PerformanceAnalysis.IndicatorsWeight;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;

import java.util.Arrays;
import java.util.Comparator;

public class IndicatorsPCA {
    // 主成分分析 计算性能指标权重
    public void PCA(double[][] matrix) {
        // 转置数据矩阵
        RealMatrix dataMatrix = MatrixUtils.createRealMatrix(matrix).transpose();
        // 计算协方差矩阵
        Covariance covariance = new Covariance(dataMatrix);
        RealMatrix covMatrix = covariance.getCovarianceMatrix();
        // 计算特征值和特征向量
        EigenDecomposition eigenDecomposition = new EigenDecomposition(covMatrix);
        // 获取特征值
        double[] eigenvalues = eigenDecomposition.getRealEigenvalues();
        // 对特征值进行排序
        Integer[] sortedIndices = new Integer[eigenvalues.length];
        for (int i = 0; i < sortedIndices.length; i ++) {
            sortedIndices[i] = i;
        }
        // 创建一个比较器
        Comparator<Integer> eigenvalueComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return -Double.compare(eigenvalues[i1], eigenvalues[i2]);
            }
        };
        // 使用比较器进行排序
        Arrays.sort(sortedIndices, eigenvalueComparator);
        //计算每个指标的重要程度
        double sumEigrnvalues = Arrays.stream(eigenvalues).sum();
        double[] importancePercentages = new double[eigenvalues.length];
        for (int i = 0; i < importancePercentages.length; i++) {
            importancePercentages[i] = (eigenvalues[sortedIndices[i]] / sumEigrnvalues) * 100;
        }

        for (int i = 0; i < importancePercentages.length; i++) {
            System.out.printf("指标%d的重要程度：%.2f%%\n", i+1, importancePercentages[i]);
        }
    }

    public static void main(String[] args){

        // 指标矩阵
        double[][] matrixArray = {
                {0.32297267169572597, 0.5070011266329084, 0.6572424679692226, 0.4786386260747624, 0.4982287689255015, 0.5133833778498194},
                {0.4805972910303316, 0.3681234625186049, 0.46201241163602724, 0.4394388297733754, 0.5098423820323703, 0.5242667496318948},
                {0.4602460297987559, 0.36139051809866896, 0.49413746729667474, 0.5776263041367606, 0.48837258540850126, 0.3993490989220169},
                {0.48717081291549574, 0.4114346845562732, 0.47473200854333253, 0.4566129889328677, 0.5065451677014843, 0.3932269969605594},
                {0.48529415247319796, 0.3993539583626651, 0.471369574267844, 0.4584701860598499, 0.5074054280874959, 0.6809923817951964},
                {0.48891675434610493, 0.5047354254378709, 0.4862828778608832, 0.5000287865011208, 0.47871956055312254, 0.5005321225045682},
                {0.4784045408703124, 0.47757168395447414, 0.5150594967143434, 0.4831081856884854, 0.4575464781186932, 0.49229437846311014},
                {0.35866415216961645, 0.5606561151144889, 0.3360875045483319, 0.5017946605099312, 0.41187693938372455, 0.4987489871189868},
                {0.4369655568468934, 0.525004323989508, 0.43959659677063945, 0.5027724894824872, 0.7133597162911011, 0.4879094311045471},
                {0.4858171749117792, 0.47440012140727567, 0.5002588551596134, 0.7066216413665914, 0.5039755298654686, 0.5581200836508207}
        };

        IndicatorsPCA indicatorsPCA = new IndicatorsPCA();
        indicatorsPCA.PCA(matrixArray);
    }
}
