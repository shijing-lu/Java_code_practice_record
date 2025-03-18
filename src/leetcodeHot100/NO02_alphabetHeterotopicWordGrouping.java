/*
 *  Copyright (c)
 *   Author:姚宇航
 *   Time:2025-3-18 JiangsuUniversity
 */

package leetcodeHot100;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
* 题目描述
* 给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
* 字母异位词是由重新排列源单词的所有字母得到的一个新单词。
*
* 示例：
* 输入：strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
* 输出：[["bat"],["nat","tan"], ["ate","eat","tea"]]
* */
public class NO02_alphabetHeterotopicWordGrouping {
    public static List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> res = new ArrayList<>();
        List<Character> chars = new ArrayList<>();
        HashMap<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            for (int i = 0; i < str.length(); i++) {
                chars.add(str.charAt(i));
            }
            chars.sort(Character::compareTo);

            if(map.containsKey(chars.toString())){
                map.get(chars.toString()).add(str);
            }else{
                map.put(chars.toString(),new ArrayList<>(Arrays.asList(str)));
            }
            chars.clear();
            }
            for(List<String> key:map.values()){
                res.add(key);
            }
            return res;
    }

    public static void main(String[] args) {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        System.out.println(groupAnagrams(strs));

    }
}
