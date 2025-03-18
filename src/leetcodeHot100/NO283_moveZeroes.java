/*
 *  Copyright (c)
 *   Author:姚宇航
 *   Time:2025-3-18 JiangsuUniversity
 */

package leetcodeHot100;

/*
* 给定一个数组nums，编写一个函数将所有0移动到数组的末尾，同时保持非零元素的相对顺序。
* 请注意，必须在不复制数组的情况下原地对数组进行操作。
*
* 示例1:
输入：nums=[0,1,0,3,12]
输出：[1,3,12,0,0]
* */
public class NO283_moveZeroes {
    public static void moveZeroes(int[] nums) {
//        暴力解法
        int n=0;
        int k=0;
        while(k<nums.length){
            if (nums[k] == 0) {
                n++;
                for (int j = k + 1; j < nums.length; j++) {
                    nums[j - 1] = nums[j];
                }
                nums[nums.length-n]=Integer.MIN_VALUE;
            }else{
                k++;
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if(nums[i]==Integer.MIN_VALUE){
                nums[i]=0;
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {0, 1, 0, 3, 12};
        moveZeroes(nums);
        for (int i : nums) {
            System.out.print(i+" ");
        }

    }
}
