package io.yzecho.netdisk;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author yzecho
 * @desc
 * @date 15/01/2021 13:34
 */
public class ForkJoinTest {

    static Random random = new Random(0);

    static long random() {
        return random.nextInt(10000);
    }

    public static void main(String[] args) {
        long[] array = new long[2000];
        long expectedSum = 0;

        for (long ele : array) {
            ele = random();
            expectedSum += ele;
        }
        System.out.println("Expected Sum: " + expectedSum);

        // fork/join
        ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
        long startTime = System.currentTimeMillis();
        Long result = ForkJoinPool.commonPool().invoke(task);
        long endTime = System.currentTimeMillis();
        System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
    }
}

class SumTask extends RecursiveTask<Long> {
    static final int THRESHOLD = 500;
    long[] array;
    int start;
    int end;

    SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start <= THRESHOLD) {
            // 如果任务足够小，直接计算
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += this.array[i];
                try {
                    // 故意放慢计算速度
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return sum;
        }

        // 任务太大，一分为二
        int middle = (end + start) / 2;
        System.out.printf("split %d~%d ==> %d~%d, %d~%d%n", start, end, start, middle, middle, end);
        SumTask subTask1 = new SumTask(this.array, start, middle);
        SumTask subTask2 = new SumTask(this.array, middle, end);
        invokeAll(subTask1, subTask2);
        Long subResult1 = subTask1.compute();
        Long subResult2 = subTask2.compute();
        Long result = subResult1 + subResult2;
        System.out.println("result = " + subResult1 + " + " + subResult2 + " ==> " + result);
        return result;
    }
}
