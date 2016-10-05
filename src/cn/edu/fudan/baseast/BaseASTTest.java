package cn.edu.fudan.baseast;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.test.Executor;
import cn.edu.fudan.baseast.test.Generator;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/10/2.
 */
public class BaseASTTest {
    public static ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
    public static ArrayList<Executor> executors = new ArrayList<Executor>();
    public static ArrayList<Generator> generators = new ArrayList<Generator>();
    public static final int SITE = 10;
    public static final int OPNUM = 2000;

    public static void run() throws InterruptedException {
        for(int i=0; i<SITE; i++) algorithms.add(new Algorithm(i, SITE));
        for(int i=0; i<SITE; i++) generators.add(new Generator(i, algorithms.get(i), algorithms, OPNUM));
        for(int i=0; i<SITE; i++) executors.add(new Executor(algorithms.get(i)));
        Thread.sleep(160000);
        for(int i=0; i<SITE; i++) executors.get(i).stop();
        long sum = 0;
        for(int i=0; i<SITE; i++) {
//            algorithms.get(i).printModel();
            algorithms.get(i).print();
            sum += algorithms.get(i).timeConsume;
        }
        System.out.println("time: " + sum / SITE / SITE / OPNUM);
    }

    public static void main(String [] args) {
        try {
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
