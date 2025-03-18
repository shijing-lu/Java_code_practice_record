package leetcodeHot100;

import java.util.HashMap;
import java.util.Scanner;

/*题目描述：
* 给定一个整数数组nums 和一个整数目标值target，
* 请你在该数组中找出和为目标值target的那两个整数，并返回它们的数组下标。
* 你可以假设每种输入只会对应一个答案，并且你不能使用两次相同的元素。
* 你可以按任意顺序返回答案。
*
* 示例 1：
* 输入：nums = [2,7,11,15], target = 9
* 输出：[0,1]
* 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
* */
public class NO01_sumOfTwoNumbers {


    public static void main(String[] args) {
        int[] nums =new int[]{2,7,11,15} ;
        int target = 9;
        NO01_sumOfTwoNumbers a = new NO01_sumOfTwoNumbers();
        int[] result = a.twoSum(nums, target);
        System.out.println(result[0] + " " + result[1]);
    }
        public int[] twoSum(int[] nums, int target) {
                // 创建一个HashMap，用于存储数组中的元素及其对应的索引
                HashMap<Integer, Integer> map = new HashMap<>();
                // 遍历数组，将元素及其索引存入HashMap中
                for (int i = 0; i < nums.length; i++) {
                    map.put(nums[i], i);
                }
                // 再次遍历数组，寻找与当前元素相加等于target的元素
                for (int j = 0; j < nums.length; j++) {
                    int temp = target - nums[j];
                    // 如果HashMap中包含与当前元素相加等于target的元素
                    if (map.containsKey(temp) ) {
                        // 如果该元素不是当前元素本身
                        if(map.get(temp)!=j){
                            // 返回两个元素的索引
                            return new int[]{j, map.get(temp)};
                        }
                    }
                }
                // 如果没有找到符合条件的元素，返回null
                return null;
            }
}
