package cn.edu.fudan.stringast;

import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;
import cn.edu.fudan.stringast.test.S_Executor;
import cn.edu.fudan.stringast.test.S_Generator;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/10/6.
 */
public class StringASTTest {
    public static ArrayList<S_Algorithm> s_algorithms = new ArrayList<S_Algorithm>();
    public static ArrayList<S_Executor> s_executors = new ArrayList<S_Executor>();
    public static ArrayList<S_Generator> s_generators = new ArrayList<S_Generator>();
    public static final int SITE = 3;
    public static final int OPNUM = 20000;

    public static void run() throws InterruptedException {
        for(int i=0; i<SITE; i++) s_algorithms.add(new S_Algorithm(i, SITE));
        for(int i=0; i<SITE; i++) s_generators.add(new S_Generator(i, s_algorithms.get(i), s_algorithms, OPNUM));
        for(int i=0; i<SITE; i++) s_executors.add(new S_Executor(s_algorithms.get(i)));
        Thread.sleep(50000);
        for(int i=0; i<SITE; i++) s_executors.get(i).stop();
        long sum = 0;
        for(int i=0; i<SITE; i++) {
            //s_algorithms.get(i).printModelinDetail();
            sum += s_algorithms.get(i).timeConsume;
        }
        System.out.println("time: " + sum / SITE / SITE / OPNUM);
        for(int i=0; i<SITE; i++) {
            s_algorithms.get(i).printModel();
        }
    }

    public static void main(String [] args) {
        try {
            run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
