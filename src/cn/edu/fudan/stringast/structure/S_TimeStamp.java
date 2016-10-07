package cn.edu.fudan.stringast.structure;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/9/21.
 */
public class S_TimeStamp {
    private Integer siteNumber;
    public ArrayList<Integer> timeStamp;

    public S_TimeStamp(int siteNumber, int Num) {
        this.siteNumber = siteNumber;
        timeStamp = new ArrayList<Integer>(Num);
        while(Num -- != 0) timeStamp.add(0);
    }

    /**
     * 深拷贝函数
     * @param ts
     */
    public S_TimeStamp(S_TimeStamp ts) {
        this.siteNumber = ts.getSiteNumber();
        this.timeStamp = new ArrayList<Integer>();
        for(Integer i : ts.timeStamp) {
            this.timeStamp.add(i);
        }
    }

    public Integer getSiteNumber() {
        return siteNumber;
    }

    public void setSiteNumber(Integer siteNumber) {
        this.siteNumber = siteNumber;
    }

    /**
     * 获取操作之间的关系
     * @param s_timeStamp
     * @return CAUSAL CONCURRENT
     */
    public int getTimeStampRelationship(S_TimeStamp s_timeStamp) {
        for(int i=0; i<this.timeStamp.size(); i++) {
            if(this.timeStamp.get(i) > s_timeStamp.timeStamp.get(i)) return S_Operation.CONCURRENT;
        }
        return S_Operation.CAUSAL;
    }

    /**
     * 获取操作的Torder值
     * @return
     */
    public int getTotalOrder() {
        int totalOrder = 0;
        for(int i=0; i<timeStamp.size(); i++) {
            totalOrder += timeStamp.get(i);
        }
        return totalOrder;
    }

    /**
     * 获取时间戳之间的全序关系
     * @param s_timeStamp
     * @return 0 小于 1 大于
     */
    public int getTotalOrderRelationship(S_TimeStamp s_timeStamp) {
        if(this.getTotalOrder() < s_timeStamp.getTotalOrder()) return 0;
        else if(this.getTotalOrder() == s_timeStamp.getTotalOrder()) {
            if(this.getSiteNumber() < s_timeStamp.getSiteNumber()) return 0;
        }
        return 1;
    }

    /**
     * 打印时间戳
     */
    public void print() {
        System.out.print("( ");
        for(int i=0; i<timeStamp.size(); i++) {
            System.out.print(timeStamp.get(i) + " ");
        }
        System.out.println(") " + siteNumber);
    }
}
