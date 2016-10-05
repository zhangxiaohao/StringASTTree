package cn.edu.fudan.baseast.structure;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/10/2.
 */
public class TimeStamp {
    public ArrayList<Integer> timeStamp;
    public int siteNumber;

    /**
     * 构造一个新站点
     * @param siteNumber
     */
    public TimeStamp(int siteNumber, int SITE) {
        this.siteNumber = siteNumber;
        this.timeStamp = new ArrayList<Integer>();
        for(int i=0; i<SITE; i++) this.timeStamp.add(0);
    }

    /**
     * 深拷贝构造函数
     * @param timeStamp
     */
    public TimeStamp(TimeStamp timeStamp) {
        this.siteNumber = timeStamp.siteNumber;
        this.timeStamp = new ArrayList<Integer>();
        for(Integer i : timeStamp.timeStamp) {
            this.timeStamp.add(i);
        }
    }

    /**
     * 计算全序值
     * @return
     */
    public int TOrder() {
        int TOrder = 0;
        for(int i=0; i<timeStamp.size(); i++)
            TOrder += timeStamp.get(i);
        return TOrder;
    }

    /**
     * 返回当前向量是否因果先序timestamp
     * @param timeStamp
     * @return true 是 false 否
     */
    public boolean isCausalOrdering(TimeStamp timeStamp) {
        for(int i=0; i<this.timeStamp.size(); i++) {
            if(this.timeStamp.get(i) > timeStamp.timeStamp.get(i)) return false;
        }
        return true;
    }

    /**
     * 返回当前时间戳TOrder是否小于timeStamp
     * @param timeStamp
     * @return
     */
    public boolean TOrderRelationship(TimeStamp timeStamp) {
        int t1 = this.TOrder(), t2 = timeStamp.TOrder();
        if(t1 == t2) return this.siteNumber < timeStamp.siteNumber;
        else return t1 < t2;
    }

    /**
     * 打印时间戳
     */
    public void print() {
        System.out.print(siteNumber + " [");
        for(Integer i : timeStamp) {
            System.out.print(" " + i);
        }
        System.out.println(" ]");
    }
}
