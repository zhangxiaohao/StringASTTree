package cn.edu.fudan.stringast.structure;

import cn.edu.fudan.baseast.structure.History;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by zhangxiaohao on 2016/10/6.
 */
public class S_History {
    ArrayList<S_Operation> [] s_history;
    ArrayList<ArrayList<S_TreeNode>> [] s_treeNodelist;
    public int SITE;
    LinkedList<Integer> linkedList;

    /**
     * 构造一个新的history
     * @param SITE
     */
    public S_History(int SITE) {
        this.SITE = SITE;
        s_history = new ArrayList[SITE];
        s_treeNodelist = new ArrayList[SITE];
        for(int i=0; i<SITE; i++) s_history[i] = new ArrayList<S_Operation>();
        for(int i=0;i <SITE; i++) s_treeNodelist[i] = new ArrayList<>();
    }

    /**
     * 添加一个历史操作
     * @param s_operation
     * @param s_treeNode
     */
    public void addOperation(S_Operation s_operation, S_TreeNode s_treeNode) {
        int siteNum = s_operation.getS_timeStamp().getSiteNumber();
        s_history[siteNum].add(s_operation);
        s_treeNodelist[siteNum].add(new ArrayList<S_TreeNode>());
        s_treeNodelist[siteNum].get(s_treeNodelist[siteNum].size() - 1).add(s_treeNode);
    }

    /**
     * 增加一个分裂后的节点指针
     * @param s_operation
     * @param s_treeNode
     */
    public void addSplitNode(S_Operation s_operation, S_TreeNode s_treeNode) {
        int siteNum = s_operation.getS_timeStamp().getSiteNumber();
        int number = s_operation.getS_timeStamp().timeStamp.get(siteNum);
        s_treeNodelist[siteNum].get(number - 1).add(s_treeNode);
    }

    /**
     * 获取一个操作的并发操作的所有头指针
     * @param s_operation
     * @return
     */
    public ArrayList<S_TreeNode> getConcurrentOperationsNodeList(S_Operation s_operation) {
        ArrayList<S_TreeNode> nodeList = new ArrayList<S_TreeNode>();
        for(int i=0; i<SITE; i++) {
            for(int j=s_history[i].size() - 1; j>=0; j--) {
                S_Operation op = s_history[i].get(j);
                if(op.getOperationRelationShip(s_operation) == S_Operation.CONCURRENT)
                    nodeList.addAll(s_treeNodelist[i].get(j));
                else break;
            }
        }
        return nodeList;
    }

    /**
     * 创建一个删除操作的列表
     * @param s_operation
     */
    public void addDelOperation(S_Operation s_operation) {
        int siteNum = s_operation.getS_timeStamp().getSiteNumber();
        s_history[siteNum].add(s_operation);
        s_treeNodelist[siteNum].add(new ArrayList<S_TreeNode>());
    }
}
