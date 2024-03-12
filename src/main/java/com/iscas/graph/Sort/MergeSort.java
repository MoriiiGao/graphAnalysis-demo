package com.iscas.graph.Sort;

/*
* 归并排序
*
* 归并排序是递归算法的一个实例，这个算法中基本的操作是合并两个已排序的数组，取两已排序的数组
* */
public class MergeSort {

    // 生成n个元素的随机数组 每个元素的取值范围是[rangeL, rangeR]
    public static Integer[] generateRandomArray(int n, int rangeL, int rangeR){

        assert rangeL <= rangeR;

        Integer[] arr = new Integer[n];

        for (int i = 0; i < n; i ++)
            arr[i] = new Integer((int)(Math.random() * (rangeR - rangeL + 1) + rangeL));
        return arr;
    }

    

    public static void main(String[] args) {

        int N = 100;
        int rangeL = 0;
        int rangeR = 100;
        Integer[] arr = MergeSort.generateRandomArray(N, rangeL, rangeR);

    }

}
