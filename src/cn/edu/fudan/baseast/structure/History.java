package cn.edu.fudan.baseast.structure;

import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/10/3.
 */
public class History {
    ArrayList<Operation> [] history;
    ArrayList<TreeNode> [] treeNodelist;
    public int SITE;

    /**
     * 构造一个新的history
     * @param SITE
     */
    public History(int SITE) {
        this.SITE = SITE;
        history = new ArrayList[SITE];
        treeNodelist = new ArrayList[SITE];
        for(int i=0; i<SITE; i++) history[i] = new ArrayList<Operation>();
        for(int i=0; i<SITE; i++) treeNodelist[i] = new ArrayList<TreeNode>();
    }

    /**
     * 添加一个历史操作
     * @param operation
     */
    public void addOperation(Operation operation, TreeNode treeNode) {
        history[operation.timeStamp.siteNumber].add(operation);
        treeNodelist[operation.timeStamp.siteNumber].add(treeNode);
    }

    /**
     * 获取一个操作的并发操作所在的节点列表
     * @param operation
     * @return
     */
    public ArrayList<TreeNode> getConcurrentOperationsNodeList(Operation operation) {
        ArrayList<TreeNode> nodeList = new ArrayList<TreeNode>();
        for(int i=0; i<SITE; i++) {
            for(int j = history[i].size() - 1; j >= 0; j--) {
                Operation op = history[i].get(j);
                if(op.isCausalOrdering(operation) == false) nodeList.add(treeNodelist[i].get(j));
                else break;
            }
        }
        return nodeList;
    }
}
