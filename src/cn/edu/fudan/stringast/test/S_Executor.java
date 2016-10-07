package cn.edu.fudan.stringast.test;

import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;

/**
 * Created by zhangxiaohao on 2016/10/6.
 */
public class S_Executor extends Thread{
    S_Algorithm s_algorithm;

    public S_Executor(S_Algorithm s_algorithm) {
        this.s_algorithm = s_algorithm;
        this.start();
    }

    public void executeOperation() {
        int sa = s_algorithm.local.size(), sb = s_algorithm.remote.size();
        if(sa + sb > 0)
            System.out.println(s_algorithm.s_timeStamp.getSiteNumber() + " has " + sa + " + " + sb + " operations!");
        while(s_algorithm.local.size() > 0) {
            S_Operation op = s_algorithm.local.poll();
            s_algorithm.execute(op, false);
        }
        while(s_algorithm.remote.size() > 0) {
            S_Operation op = s_algorithm.remote.poll();
            if(s_algorithm.isCausalReady(op)) s_algorithm.execute(op, true);
            else {
                s_algorithm.remote.add(op);
                break;
            }
        }
    }

    public void run() {
        int i = 0;
        while(true) {
            try {
                Thread.sleep(10);
                executeOperation();
                i ++;
                System.out.println("ALIVE!!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
