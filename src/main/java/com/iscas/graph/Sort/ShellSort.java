package com.iscas.graph.Sort;

/*
* 希尔排序
* */
public class ShellSort {

    // 生成n个元素的随机数组 每个元素的取值范围是[rangeL, rangeR]
    public static Integer[] generateRandomArray(int n, int rangeL, int rangeR){
        assert rangeL <= rangeR;

        Integer[] arr = new Integer[n];

        for (int i = 0; i < n; i ++)
            arr[i] = new Integer((int)(Math.random() * (rangeR - rangeL + 1) + rangeL));
        return arr;
    }

    // 希尔排序
    public static void sort(Comparable[] arr){
        int j;
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for ( int i = gap; i < arr.length; i ++) {
                // 第gap个元素
                Comparable tmp = arr[i];
                // 将第j个元素和第j-gap个元素进行比
                for (j = i; j >= gap && tmp.compareTo(arr[j - gap]) < 0; j -= gap) {
                    // j 初始位置在i 处于gap
                    // 如果 tmp 小于 arr[j - gap] 将第j-gap的元素 填充到 第j个位置； 进入下一个循环 j 更新为 j - gap
                    // 如果 tmp 大于 arr[j - gap] 跳出循环；将tmp（初始j位置的值） 赋值给当前j的位置
                    arr[j] = arr[j - gap];
                }
                //j = j - gap
                arr[j] = tmp;
            }
        }
    }

    public static void sort1(Comparable[] arr){
        int j;
        for (int gap = arr.length / 2; gap > 0; gap /= 2) {
            for ( int i = gap; i < arr.length; i ++) {
                Comparable tmp = arr[i];
                for (j = i; j >= gap && tmp.compareTo(arr[j - gap]) < 0; j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = tmp;
            }
        }
    }

    public static void main(String[] args){
        int N = 10;
        int rangeL = 0;
        int rangeR = 10;
        Integer[] arr = ShellSort.generateRandomArray(N, rangeL, rangeR);

        ShellSort.sort(arr);
        for (int i = 0; i < arr.length; i ++){
            System.out.println(arr[i]);
            System.out.println(" ");
        }
    }


}
