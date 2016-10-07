package cn.edu.fudan.stringast.structure;

import cn.edu.fudan.baseast.structure.TreeNode;

/**
 * Created by zhangxiaohao on 2016/10/6.
 */
public class S_TreeNode extends S_Node{
    public static final S_TreeNode NULL = new S_TreeNode();
    public S_TreeNode left, right, father;
    public int size;
    public int effectiveNode;

    /**
     * 用于构造NULL的构造函数
     */
    public S_TreeNode() {
        super();
        this.size = 0;
        this.effectiveNode= 0;
        this.left = this.right = this;
    }

    /**
     * 包含一个插入操作的树节点
     * @param str
     * @param s_operation
     * @param s_father
     */
    public S_TreeNode(StringBuilder str, S_Operation s_operation, S_TreeNode s_father) {
        super(str, s_operation);
        this.left = S_TreeNode.NULL;
        this.right = S_TreeNode.NULL;
        this.father = s_father;
        this.size = 1;
        this.effectiveNode = this.isEffect ? str.length() : 0;
    }

    /**
     * 构造一个新的节点所有树指针都是空，Node域复制过来
     * 为split函数提供
     * @param s_treeNode
     */
    public S_TreeNode(S_TreeNode s_treeNode) {
        super(s_treeNode);
        this.left = S_TreeNode.NULL;
        this.right = S_TreeNode.NULL;
        this.father = S_TreeNode.NULL;
        this.size = 1;
        this.effectiveNode = isEffect ? this.operationString.length() : 0;
    }

    /**
     * 维护节点信息
     */
    public void push() {
        this.size = this.left.size + this.right.size + 1;
        this.effectiveNode = this.left.effectiveNode + this.right.effectiveNode + (this.isEffect ? operationString.length() : 0);
    }

    /**
     * 将一个节点在position位置分裂成两个
     * 原来的节点做为原始节点，新建立的节点做为后继节点然后返回出来
     * @param position
     * @return
     */
    public S_TreeNode split(int position, S_History s_history) {
        S_TreeNode s_treeNode = new S_TreeNode(this);
        int length = operationString.length();
        if(position != length) operationString.delete(position, length);
        for(int i=0; i<operations.size(); i++) {
            if(position != length) operations.get(i).operationString.delete(position, length);
        }
        push();
        if(position != 0) s_treeNode.operationString.delete(0, position);
        for(int i=0; i<operations.size(); i++) {
            if(position != length) s_treeNode.operations.get(i).operationString.delete(0, position);
            s_history.addSplitNode(operations.get(i), s_treeNode);
        }
        s_treeNode.push();
        return s_treeNode;
    }
}
