package cn.edu.fudan.baseast.structure;

import java.sql.Time;

/**
 * Created by zhangxiaohao on 2016/10/2.
 */
public class Operation {
    public static final int INSERT = 0;
    public static final int DELETE = 1;
    public TimeStamp timeStamp;
    public int type;
    public StringBuilder str;
    public int pos;

    /**
     * 构造一个新的操作
     * @param timeStamp
     * @param type
     * @param str
     * @param pos
     */
    public Operation(TimeStamp timeStamp, int type, StringBuilder str, int pos) {
        this.timeStamp = new TimeStamp(timeStamp);
        this.type = type;
        this.str = new StringBuilder(str);
        this.pos = pos;
    }

    /**
     * 深拷贝构造函数
     * @param operation
     */
    public Operation(Operation operation) {
        this.timeStamp = new TimeStamp(operation.timeStamp);
        this.type = operation.type;
        this.str = new StringBuilder(operation.str);
        this.pos = operation.pos;
    }

    /**
     * 判断本操作是否因果先序于operation
     * @param operation
     * @return
     */
    public boolean isCausalOrdering(Operation operation) {
        return this.timeStamp.isCausalOrdering(operation.timeStamp);
    }

    /**
     * 打印操作
     */
    public void print() {
        System.out.print("timestamp: ");
        timeStamp.print();
        System.out.println("type: " + (this.type == Operation.INSERT ? "Insert" : "Delete") + " pos: " + this.pos + " str: " + this.str);
    }
}
