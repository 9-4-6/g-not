package com.example.gnote.algorithm;

import java.util.Arrays;

/**
 * @author guozhong
 * @date 2026/1/27
 *
 * 算法名称	快速排序（Quick Sort）
 * 逻辑：
 * 步骤一 ：选出一个数作为基数，作为对比基数，把它小的放左边，比它大放右边，临界点 左边等于或者大于右边结束
 *        注意点 不是左比较一下，右比较一下，交替比较，比如：左比较一下，直到条件不满足，才轮到右比较
 * 步骤二 ：一次类推，进行递归
 *
 * 分步骤实现思路：
 *        一阶段实现 步骤一
 *        二阶段实现 在步骤一的基础上实现步骤二 步骤二的临节点 只有一个元素
 *
 * 拓展：应用场景 arrays.sort
 *
 *
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = {5, 2, 9, 3, 7, 6, 1, 8, 4};

        //思考 步骤一实现（这个是思考的过程，可不看）
       /* int pivotValue = arr[arr.length - 1];
        //定义左移指针 与 右移指针
        int left =0, right =arr.length - 1;
        // 这个索引就是基数索引
        int i = moveLeft(arr, pivotValue, left, right);
        System.out.println("步骤一:"+Arrays.toString(arr));*/
        //步骤一:[1, 2, 3, 4, 7, 6, 9, 8, 5]

        //----------------------------------------------------------------------------------------------
        // 最终答案（必看）
        quickSort(arr, 0, arr.length - 1);
        System.out.println("最终答案:"+Arrays.toString(arr));

    }

    public static void quickSort(int[] arr, int low, int high) {
        //这个递归结束标识
        if (low >= high) {
            return;
        }
        //基准数索引
        int pivotIdx = partition(arr, low, high);

        //左递归
        quickSort(arr, low, pivotIdx - 1);
        //右递归
        quickSort(arr, pivotIdx + 1, high);
    }

    private static int partition(int[] arr, int low, int high) {
        // 选尾元素做基准（也可随机选基准优化,头元素,中间元素） 5, 2, 9, 3, 7, 6, 1, 8, 4
        int pivot = arr[high];
        // 小于基准的区域指针
        int left = low - 1;
        for (int right = low; right < high;right++) {
            if (arr[right] <= pivot) {
                left++;
                // 交换元素，扩大小于基准的区域
                int temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
            }
        }
        // 基准元素归位
        int temp = arr[left + 1];
        arr[left + 1] = arr[high];
        arr[high] = temp;
        return left + 1;
    }







    private static int moveLeft(int[] arr, int pivotValue, int left, int right) {
        if(left>=right){
            arr[left] = pivotValue;
            return left;
        }
        if(arr[left]>pivotValue){
            //如果大于基数，就把该值移动最右边
            arr[right] = arr[left];
            right--;
            moveRight(arr,pivotValue,left,right);
        }else {
            left++;
            //再左移
            moveLeft(arr,pivotValue,left,right);
        }

        return left;
    }

    private static int moveRight(int[] arr, int pivotValue, int left, int right) {
        if(left>=right){
            arr[left] = pivotValue;
            return left;
        }
        //右移
        if(arr[right]<pivotValue){
            //如果大于基数，就把该值移动最左边
            arr[left] = arr[right];
            left++;
            //左移
            moveLeft(arr,pivotValue,left,right);
        }else{
            right--;
            //再右移
            moveRight(arr,pivotValue,left,right);
        }
        return left;
    }



}
