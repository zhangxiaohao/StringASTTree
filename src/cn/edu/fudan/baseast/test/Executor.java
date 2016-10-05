package cn.edu.fudan.baseast.test;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.structure.Operation;

/**
 * Created by zhangxiaohao on 2016/10/3.
 */
public class Executor extends Thread{
    Algorithm algorithm;

    public Executor(Algorithm algorithm) {
        this.algorithm = algorithm;
        this.start();
        //System.out.println("Site Executor " + algorithm.timeStamp.siteNumber + " has run!");
    }

    public void executeOperation() {
        while(algorithm.local.size() > 0) {
            Operation op = algorithm.local.poll();
            algorithm.execute(op, false);
        }
        while(algorithm.remote.size() > 0) {
            Operation op = algorithm.remote.poll();
            if(algorithm.isCausalReady(op)) algorithm.execute(op, true);
            else algorithm.remote.add(op);
        }
    }

    public void run() {
        while(true) {
            try {
                Thread.sleep(10);
                executeOperation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
