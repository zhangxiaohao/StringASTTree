package cn.edu.fudan.baseast.structure;

/**
 * Created by zhangxiaohao on 2016/10/2.
 */
public class TreeNode extends Node{
    public static final TreeNode NULL = new TreeNode();
    public TreeNode left, right, father;
    public int size;
    public int effectiveNode;

    public TreeNode() {
        super();
        this.size = 0;
        this.effectiveNode = 0;
        this.left = this.right = this;
    }

    /**
     * 构造一个包含插入操作的树节点
     * @param str
     * @param operation
     */
    public TreeNode(StringBuilder str, Operation operation, TreeNode father) {
        super(str, operation);
        this.left = TreeNode.NULL;
        this.right = TreeNode.NULL;
        this.father = father;
        this.size = 1;
        this.effectiveNode = (this.isEffect ? 1 : 0);
    }

    /**
     * 深拷贝构造函数
     * @param treeNode
     */
    public TreeNode(TreeNode treeNode) {
        super(treeNode);
        this.left = treeNode.left;
        this.right = treeNode.right;
        this.father = treeNode.father;
        this.size = treeNode.size;
        this.effectiveNode = treeNode.effectiveNode;
    }

    /**
     * 节点大小信息更新
     */
    public void push() {
        this.size = this.left.size + this.right.size + 1;
        this.effectiveNode = this.left.effectiveNode + this.right.effectiveNode + (this.isEffect ? 1 : 0);
    }
}
