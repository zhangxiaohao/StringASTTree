package cn.edu.fudan.baseast.algorithm;

import cn.edu.fudan.Timer;
import cn.edu.fudan.baseast.structure.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangxiaohao on 2016/10/3.
 */
public class Algorithm {
    public TimeStamp timeStamp;
    public SBT document;
    public History history;
    public ConcurrentLinkedQueue<Operation> local, remote;

    public long timeConsume;

    public Algorithm(int siteNumber, int SITE) {
        document = new SBT();
        history = new History(SITE);
        local = new ConcurrentLinkedQueue<Operation>();
        remote = new ConcurrentLinkedQueue<Operation>();
        timeStamp = new TimeStamp(siteNumber, SITE);
        timeConsume = 0;
    }

    /**
     * 将文档状态回溯到operation状态
     * @param operation
     */
    public void retracing(Operation operation) {
        ArrayList<TreeNode> nodes = this.history.getConcurrentOperationsNodeList(operation);
        for(TreeNode node : nodes) {
            document.isEffect(node, operation);
        }
    }

    /**
     * 将文档状态从operation状态下回溯回来
     * @param operation
     */
    public void retracingback(Operation operation) {
        ArrayList<TreeNode> nodes = this.history.getConcurrentOperationsNodeList(operation);
        for(TreeNode node : nodes) {
            document.isEffect(node);
        }
    }

    /**
     * rangescan 算法
     * @param node
     * @param operation
     * @return
     */
    public void rangescan(TreeNode node, Operation operation) {
        TreeNode p = TreeNode.NULL;
        while(node != TreeNode.NULL) {
            if(node.isEffect) {
                if(p == TreeNode.NULL) p = node;
                break;
            }
            if(node.operations.get(0).isCausalOrdering(operation)) {
                if(p == TreeNode.NULL) p = node;
                break;
            }else {
                if (p == TreeNode.NULL && operation.timeStamp.TOrderRelationship(node.operations.get(0).timeStamp)) {
                    p = node;
                }
                if (p != TreeNode.NULL && operation.timeStamp.TOrderRelationship(node.operations.get(0).timeStamp) == false &&
                        node.operations.get(0).isCausalOrdering(p.operations.get(0))) {
                    p = TreeNode.NULL;
                }
            }
            node = document.findNext(node);
        }
        int k;
        if(p == TreeNode.NULL) k = document.head.size;
        else k = document.getRank(p);
        document.insert(k, operation, history);
    }

    /**
     * 执行操作operation
     * @param operation
     */
    public void execute(Operation operation, boolean isremote) {
        long start = Timer.now();
        retracing(operation);
        TreeNode node = document.findOperationPosition(document.head, operation, TreeNode.NULL, 0);
        if(operation.type == operation.DELETE) {
            node.addDelete(operation);
            document.isEffect(node);
            history.addOperation(operation, node);
        }else if(operation.type == operation.INSERT)  {
            if(operation.pos > 0) node = document.findNext(node);
            rangescan(node, operation);
        }
        if(isremote) timeStamp.timeStamp.set(operation.timeStamp.siteNumber, timeStamp.timeStamp.get(operation.timeStamp.siteNumber) + 1);
        retracingback(operation);
        timeConsume += Timer.now() - start;
    }

    /**
     * 判断操作operation是不是因果就绪
     * @param operation
     * @return
     */
    public boolean isCausalReady(Operation operation) {
        ArrayList<Integer> t = operation.timeStamp.timeStamp;
        for(int i = 0; i < t.size(); i++) {
            if(i != operation.timeStamp.siteNumber && t.get(i) > timeStamp.timeStamp.get(i)) return false;
            if(i == operation.timeStamp.siteNumber && t.get(i) != timeStamp.timeStamp.get(i) + 1) return false;
        }
        return true;
    }

    public void printModel() {
        document.printModel();
    }

    public void print() {
        document.print();
    }
}
