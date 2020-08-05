package cn.ann;

import cn.hutool.core.collection.ConcurrentHashSet;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

/**
 * Description：面试题目，经测试，我的电脑上使用 fun() 性能最好
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
        Set<Integer> result;
        Instant start = Instant.now();
//        for (int i = 0; i < 1000; i++) {
            result = fun(nums);
//        }
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end).toMillis());
        System.out.println("result.size = " + result.size());
    }

    private static Set<Integer> fun(int[] nums) {
        Set<Integer> ret = new HashSet<>();
        Random random = new Random();
        int length = nums.length;
        while (ret.size() < 100000) {
            int index = random.nextInt(length);
            ret.add(nums[index]);
        }
        return ret;
    }

    private static List<Integer> fun2(int[] nums) {
        List<Integer> numList = Arrays.stream(nums).boxed().collect(Collectors.toList());
        Collections.shuffle(numList);
        return numList.subList(0, 100000);
    }
}

class DemoTask extends RecursiveAction {
    private final int[] NUMS;
    private final int START_INDEX;
    private final int END_INDEX;

    private static final int TASK_COUNT = 1000;
    private static final int NUM_COUNT = 100000;
    public static final Set<Integer> RESULT = new ConcurrentHashSet<>();

    public DemoTask(int[] nums) {
        this.NUMS = nums;
        this.START_INDEX = 0;
        this.END_INDEX = nums.length;
    }

    private DemoTask(int[] nums, int startIndex, int endIndex) {
        this.NUMS = nums;
        this.START_INDEX = startIndex;
        this.END_INDEX = endIndex;
    }

    @Override
    protected void compute() {
        List<DemoTask> tasks = new ArrayList<>();
        Random random = new Random();
        int subLength = NUMS.length / TASK_COUNT;
        if (END_INDEX - START_INDEX < subLength) {
            for (int i = 0; i < NUM_COUNT / TASK_COUNT; i++) {
                int index = random.nextInt(subLength) + START_INDEX;
                if (RESULT.contains(NUMS[index])) {
                    i--;
                    continue;
                }
                RESULT.add(NUMS[index]);
            }
        } else {
            for (int i = 0; i < TASK_COUNT; i++) {
                tasks.add(new DemoTask(NUMS, i * subLength, (i + 1) * subLength - 1));
            }
        }
        if (tasks.size() > 0) {
            Collection<DemoTask> demoTasks = invokeAll(tasks);
            demoTasks.forEach(ForkJoinTask::join);
        }
    }
}
