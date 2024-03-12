package com.iscas.graph.PerformanceAnalysis.NumCalFunc;

public class NonLinearFunction {

    // 指数函数
    public double Exp(double data) {
        return Math.exp(data);
    }

    // 对数变换
    public double LogTrans(double data) {
        // 添加1 避免对0取对数
        return Math.log(Math.abs(data) + 1);
    }

    // 幂次变换
    public double Pow(double data, double power) {
        /**
         * power : 次幂值
         * */
        return Math.pow(data, power);
    }

    // 反正弦变换
    public double Asin(double data) {
        return Math.asin(data);
    }

    public double Sigmoid(double x) {
        return (1 / (1 + Math.exp(x)));
    }

    public double Sqrt(double x) {
        return Math.sqrt(x);
    }

    //
    public double SubSigmoid(double x) {
        return 1 - (1 / (1 + Math.exp(x)));
    }

}
