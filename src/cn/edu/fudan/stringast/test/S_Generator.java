package cn.edu.fudan.stringast.test;

import cn.edu.fudan.stringast.algorithm.S_Algorithm;
import cn.edu.fudan.stringast.structure.S_Operation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhangxiaohao on 2016/10/6.
 */
public class S_Generator extends Thread{
    public int siteNumber;
    public S_Algorithm s_algorithm;
    public ArrayList<S_Algorithm> s_algorithms;
    public Integer number;
    private String alpha = "abcdefghijklnmopqrstuvwxyz";

    public S_Generator(int siteNumber, S_Algorithm s_algorithm, ArrayList<S_Algorithm> s_algorithms, Integer number) {
        this.siteNumber = siteNumber;
        this.s_algorithm = s_algorithm;
        this.s_algorithms = s_algorithms;
        this.number = number;
        this.start();
    }

    public StringBuilder getStr(int len) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<len; i++) {
            int pos = new Random().nextInt(26);
            sb.append(alpha.substring(pos, pos + 1));
        }
        return sb;
    }

    public void generateOperation() {
        int type = S_Operation.INSERT;
        if(this.s_algorithm.document.head.effectiveNode > 0) {
            type = Math.random() > 0.1 ? S_Operation.INSERT : S_Operation.DELETE;
        }
        int position = 0, length = 1;
        if(type == S_Operation.INSERT) {
            position = new Random().nextInt(this.s_algorithm.document.head.effectiveNode + 1);
            length = new Random().nextInt(1) + 1;
        }else if (type == S_Operation.DELETE) {
            position = 1;//new Random().nextInt(this.s_algorithm.document.head.effectiveNode) + 1;
            position = Math.max(position, 1);
            int t = new Random().nextInt(this.s_algorithm.document.head.effectiveNode - position + 1) + 1;
            length = Math.min(1, t);
            //new Random().nextInt(this.s_algorithm.document.head.effectiveNode - position + 1) + 1;
        }
        number -= length;
        int siteNum = s_algorithm.s_timeStamp.getSiteNumber();
        s_algorithm.s_timeStamp.timeStamp.set(siteNum, s_algorithm.s_timeStamp.timeStamp.get(siteNum) + 1);
        S_Operation s_operation = new S_Operation(s_algorithm.s_timeStamp, getStr(length), type, position, length);
        s_algorithm.local.add(s_operation);
        for(int i=0; i<s_algorithms.size(); i++) if(i != siteNum){
            try {
                Thread.sleep(0);
                //System.out.println("Send " + siteNum + " " + i);
                s_algorithms.get(i).remote.add(s_operation);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //s_operation.print();
    }

    public void run() {
        while(number > 0) {
            generateOperation();
            try {
                Thread.sleep(new Random().nextInt(1) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
