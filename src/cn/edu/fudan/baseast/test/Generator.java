package cn.edu.fudan.baseast.test;

import cn.edu.fudan.baseast.algorithm.Algorithm;
import cn.edu.fudan.baseast.structure.Operation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhangxiaohao on 2016/10/3.
 */
public class Generator extends Thread{
    public int siteNumber;
    public Algorithm algorithm;
    public ArrayList<Algorithm> algorithms;
    public Integer number;
    private String alpha = "abcdefghijklmnopqrstuvwxyz";

    public Generator(int siteNumber, Algorithm algorithm, ArrayList<Algorithm> algorithms, Integer number) {
        this.siteNumber = siteNumber;
        this.algorithm = algorithm;
        this.algorithms = algorithms;
        this.number = number;
        this.start();
        //System.out.println("Site Generator " + siteNumber + " has run!");
    }

    private void generateOperation() {
        //System.out.println("Operation has been execute!");
        int type = Operation.INSERT;
        if(this.algorithm.document.head.effectiveNode > 0) {
            type = Math.random() > 0.1 ? Operation.INSERT : Operation.DELETE;
        }
        int position = 0;
        if(type == Operation.INSERT) {
            position = new Random().nextInt(algorithm.document.head.effectiveNode + 1);
        }else if(type == Operation.DELETE) {
            position = new Random().nextInt(algorithm.document.head.effectiveNode) + 1;
        }
        int pos = new Random().nextInt(26);
        algorithm.timeStamp.timeStamp.set(algorithm.timeStamp.siteNumber, algorithm.timeStamp.timeStamp.get(algorithm.timeStamp.siteNumber) + 1);
        Operation operation = new Operation(algorithm.timeStamp, type, new StringBuilder(alpha.substring(pos, pos + 1)), position);
        //operation.print();
        algorithm.local.add(operation);
        for(int i=0; i<algorithms.size(); i++) if(i != siteNumber) {
            try {
                Thread.sleep(5);
                algorithms.get(i).remote.add(operation);
            } catch (InterruptedException e) {}
        }
    }

    public void run() {
        for(int i=0; i<number; i++) {
            generateOperation();
            try {
                Thread.sleep(new Random().nextInt(15));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
