/*
 *  Copyright (c)
 *   Author:姚宇航
 *   Time:2025-3-18 JiangsuUniversity
 */

package leetcodeHot100;

import java.util.Arrays;

/*
* 给定一个未排序的整数数组nums，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
请你设计并实现时间复杂度为0（n）的算法解决此问题。
*
示例1:
输入:nums =[100,4,200,1,3,2]
输出：4
解释：最长数字连续序列是[1，2，3，4]。它的长度为4。
* */
public class NO128_longestContinuousSequence {

    //思路：排序后遍历，时间复杂度O(nlogn)
    public static int longestConsecutive(int[] nums) {
        Arrays.sort(nums);
        int left= 0, right = 1, max = 1,current = 1;
        if(nums.length==0){
            return 0;
        }
        while(right<nums.length){
            if(nums[right] == nums[right-1]){
                right++;
                continue;
            }
            if(nums[right] == nums[right-1]+1){
                right++;
                current++;
            }else{
                left = right;
                right++;
                current = 1;
            }
            max = Math.max(max,current);

        }
        return max;
      }


    public static void main(String[] args) {
        int[] nums = {100, 4, 200, 1, 3, 2};
        System.out.println(longestConsecutive(nums));

    }
}
