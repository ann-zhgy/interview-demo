package cn.ann;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Description：面试题目
 * <p>
 * Date: 2020-8-5 13:53
 *
 * @author 88475
 * @version v1.0
 */
public class Demo {
    public static void main(String[] args) {
        int[] nums = new int[500000];
        for (int i = 0; i < nums.length; i++) {
            nums[i] = i;
        }
        long start = new Date().getTime();
        for (int i = 0; i < 1000; i++) {
            Set<Integer> result = fun(nums);
        }
        long end = new Date().getTime();
        System.out.println(end - start);
    }

    private static Set<Integer> fun(int[] nums) {
        Set<Integer> ret = new HashSet<>();
        Random random = new Random();
        int length = nums.length;
        while (ret.size() <= 100000) {
            int index = random.nextInt(length);
            ret.add(nums[index]);
        }
        return ret;
    }
}
