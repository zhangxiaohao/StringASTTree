package cn.edu.fudan.stringast.algorithm;

import cn.edu.fudan.Timer;
import cn.edu.fudan.baseast.structure.TreeNode;
import cn.edu.fudan.stringast.structure.SBT;
import cn.edu.fudan.stringast.structure.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by zhangxiaohao on 2016/9/21.
 */
public class S_Algorithm {
    public S_TimeStamp s_timeStamp;
    public SBT document;
    public ConcurrentLinkedQueue<S_Operation> local, remote;
    public S_History s_history;

    public long timeConsume;

    public S_Algorithm(int siteNum, int SITE) {
        local = new ConcurrentLinkedQueue<S_Operation>();
        remote = new ConcurrentLinkedQueue<S_Operation>();
        document = new SBT();
        s_timeStamp = new S_TimeStamp(siteNum, SITE);
        timeConsume = 0;
        s_history = new S_History(SITE);
    }

    /**
     * 将文档回溯到s_operation状态
     * @param s_operation
     */
    public void retracing(S_Operation s_operation) {
        ArrayList<S_TreeNode> nodes = this.s_history.getConcurrentOperationsNodeList(s_operation);
        for(S_TreeNode node : nodes) {
            document.isEffect(node, s_operation);
        }
    }

    /**
     * 将文档从s_operation状态下回溯回来
     * @param s_operation
     */
    public void retracingback(S_Operation s_operation) {
        ArrayList<S_TreeNode> nodes = this.s_history.getConcurrentOperationsNodeList(s_operation);
        for(S_TreeNode node : nodes) {
            document.isEffect(node);
        }
    }

    /**
     * rangescan 考虑几种情况
     * 在一个有效节点之间的插入
     * 在有效节点的边界插入，后面可能包含无效节点
     * @param s_treeNode
     * @param shift
     * @param s_operation
     */
    public void rangescan(S_TreeNode s_treeNode, int shift, S_Operation s_operation) {
        S_TreeNode p = s_treeNode, q = S_TreeNode.NULL;
        if(shift > 0 && shift != p.operationString.length()) {
            S_TreeNode c_node = s_treeNode.split(shift, s_history);
            int k = document.getRank(s_treeNode);
            document.insert(k + 1, c_node);
            document.insert(k + 1, s_operation, s_history);
            return ;
        }
        if(shift != 0)
            p = document.findNext(p);
        while(p != S_TreeNode.NULL) {
            if(p.isEffect) {
                if(q == S_TreeNode.NULL) q = p;
                break;
            }
            if(p.getInsertOperation().getOperationRelationShip(s_operation) == S_Operation.CAUSAL) {
                if(q == S_TreeNode.NULL) q = p;
                break;
            }else {
                if(q == S_TreeNode.NULL && p.getInsertOperation().getS_timeStamp().getTotalOrderRelationship(s_operation.getS_timeStamp()) == 1) {
                    q = p;
                }
                if(q != S_TreeNode.NULL && p.getInsertOperation().getS_timeStamp().getTotalOrderRelationship(s_operation.getS_timeStamp()) == 0
                   && p.getInsertOperation().getOperationRelationShip(q.getInsertOperation()) == S_Operation.CAUSAL) {
                    q = S_TreeNode.NULL;
                }
            }
            p = document.findNext(p);
        }
        int k;
        if(q == S_TreeNode.NULL) k = document.head.size;
        else k = document.getRank(q);
        document.insert(k, s_operation, s_history);
    }

    /**
     * 删除函数 首先找到删除范围的边界
     * 检查是否需要分裂，分裂完毕后对操作执行范围内的所有有效节点添加删除操作。
     * @param s_treeNode
     * @param shift
     * @param s_operation
     */
    public void delete(S_TreeNode s_treeNode, int shift, S_Operation s_operation) {
        S_TreeNode p = s_treeNode, tp = s_treeNode;
        if(shift != 1) {
            if(shift == 0 && this.s_timeStamp.getSiteNumber() == 0) {
                System.out.println("____________________________");
                s_operation.print();
                document.printModel();
            }
            S_TreeNode copy_node = s_treeNode.split(shift - 1, s_history);
            int k = document.getRank(s_treeNode);
            document.insert(k + 1, copy_node);
            p = copy_node;
            tp = copy_node;
        }
        int cnt = 0;
        while(p != S_TreeNode.NULL) {
            if(p.isEffect == false) {
                p = document.findNext(p);
                continue;
            }
            cnt += p.operationString.length();
            if(cnt >= s_operation.getLength()) {
                int tpos = p.operationString.length() - (cnt - s_operation.getLength());
                if(tpos != p.operationString.length()) {
                    S_TreeNode c_node = p.split(tpos, s_history);
                    int k = document.getRank(p);
                    document.insert(k + 1, c_node);
                }
                break;
            }
            p = document.findNext(p);
        }
        s_history.addDelOperation(s_operation);
        cnt = 0;
        while(tp != S_TreeNode.NULL) {
            if(tp.isEffect == false) {
                tp = document.findNext(tp);
                continue;
            }
            cnt += tp.operationString.length();
            if(cnt > s_operation.getLength()) break;
            S_Operation nop = new S_Operation(s_operation);
            nop.getOperationString().replace(0, nop.getOperationString().length(), tp.operationString.toString());
            tp.addDelete(nop);
            s_history.addSplitNode(s_operation, tp);
            document.isEffect(tp);
            tp = document.findNext(tp);
        }
    }

    /**
     * 执行操作函数
     * @param s_operation
     */
    public void execute(S_Operation s_operation, boolean isremote) {
        long start = Timer.now();
        retracing(s_operation);
        int shift [] = new int[1];
        S_TreeNode s_treeNode = document.findOperationPosition(document.head, s_operation, S_TreeNode.NULL, 0, shift);
        if(s_operation.getOperationType() == s_operation.DELETE) {
            delete(s_treeNode, shift[0], s_operation);
        }else if(s_operation.getOperationType() == s_operation.INSERT) {
            rangescan(s_treeNode, shift[0], s_operation);
        }
        if(isremote) s_timeStamp.timeStamp.set(s_operation.getS_timeStamp().getSiteNumber(), s_timeStamp.timeStamp.get(s_operation.getS_timeStamp().getSiteNumber()) + 1);
        retracingback(s_operation);
        timeConsume += Timer.now() - start;
        //System.out.println("------------------------------------");
        //s_operation.print();
        //document.printModel();
    }

    /**
     * 返回一个远程操作是否在当前站点上因果就绪
     * @param s_operation
     * @return true or false
     */
    public boolean isCausalReady(S_Operation s_operation) {
        for(int i=0; i<s_operation.getS_timeStamp().timeStamp.size(); i++) {
            if(i == s_operation.getS_timeStamp().getSiteNumber()) {
                if(s_operation.getS_timeStamp().timeStamp.get(i) != s_timeStamp.timeStamp.get(i) + 1)
                    return false;
            }else {
                if(s_operation.getS_timeStamp().timeStamp.get(i) > s_timeStamp.timeStamp.get(i))
                    return false;
            }
        }
        return true;
    }

    /**
     * 打印模型的详细信息
     */
    public void printModelinDetail() {
        document.printModel();
    }

    /**
     * 打印模型
     */
    public void printModel() {
        document.print();
    }
}
