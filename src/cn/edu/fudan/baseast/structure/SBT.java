package cn.edu.fudan.baseast.structure;

/**
 * Created by zhangxiaohao on 2016/10/2.
 */
public class SBT {
    public TreeNode head;

    public SBT() {
        head = TreeNode.NULL;
    }

    /**
     * 右旋
     * @param t
     */
    public TreeNode rightRotate(TreeNode t) {
        TreeNode k = t.left;
        TreeNode t_father = t.father;
        k.right.father = t;
        t.left = k.right;
        t.father = k;
        k.right = t;
        k.size = t.size;
        k.effectiveNode = t.effectiveNode;
        t.push();
        k.father = t_father;
        return k;
    }

    /**
     * 左旋
     * @param t
     */
    public TreeNode leftRotate(TreeNode t) {
        TreeNode k = t.right;
        TreeNode t_father = t.father;
        k.left.father = t;
        t.right = k.left;
        t.father = k;
        k.left = t;
        k.size = t.size;
        k.effectiveNode = t.effectiveNode;
        t.push();
        k.father = t_father;
        return k;
    }

    /**
     * maintain函数
     * @param t
     * @param flag
     */
    public TreeNode maintain(TreeNode t, boolean flag) {
        if(flag == false) {
            if(t.left.left.size > t.right.size) t = rightRotate(t);
            else if (t.left.right.size > t.right.size) {
                t.left = leftRotate(t.left);
                t = rightRotate(t);
            }else return t;
        }else {
            if(t.right.right.size > t.left.size) t = leftRotate(t);
            else if (t.right.left.size > t.left.size) {
                t.right = rightRotate(t.right);
                t = leftRotate(t);
            }else return t;
        }
        t.left = maintain(t.left, false);
        t.right = maintain(t.right, true);
        t = maintain(t, false);
        t = maintain(t, true);
        return t;
    }

    public TreeNode findSmall(TreeNode t) {
        TreeNode p = t;
        if(p.right != TreeNode.NULL) {
            p = p.right;
            while(p.left != TreeNode.NULL) p = p.left;
        }
        return p;
    }

    /**
     * 找到一个节点的后继 O(logn)
     * @param t
     * @return
     */
    public TreeNode findNext(TreeNode t) {
        TreeNode p = t;
        while(p != TreeNode.NULL) {
            if(p.right != TreeNode.NULL) return findSmall(p);
            else {
                TreeNode q;
                do{
                    q = p;
                    p = p.father;
                }while(p.right == q);
                return p;
            }
        }
        return p;
    }

    /**
     * 首先更新当前node的有效值，然后更新祖先节点的有效值
     * @param treeNode
     * @param operation
     */
    public void isEffect(TreeNode treeNode, Operation operation) {
        treeNode.isEffect(operation);
        TreeNode p = treeNode;
        while(p != TreeNode.NULL) {
            p.push();
            p = p.father;
        }
    }

    /**
     * 将节点回溯到最新状态
     * @param treeNode
     */
    public void isEffect(TreeNode treeNode) {
        treeNode.isEffect();
        TreeNode p = treeNode;
        while(p != TreeNode.NULL) {
            p.push();
            p = p.father;
        }
    }

    /**
     * 给定一个树节点取得排名
     * @param treeNode
     * @return
     */
    public int getRank(TreeNode treeNode) {
        int rank = 0;
        TreeNode p = treeNode;
        while(treeNode != TreeNode.NULL) {
            if(treeNode.left != p) rank += treeNode.left.size + 1;
            p = treeNode;
            treeNode = treeNode.father;
        }
        return rank - 1;
    }

    /**
     * 在rank位置插入由operation创建的节点
     * @param rank
     * @param operation
     */
    private TreeNode insert(TreeNode t, int rank, Operation operation, int trank, TreeNode father, History history) {
        if(t == TreeNode.NULL) {
            TreeNode node = new TreeNode(operation.str, operation, father);
            history.addOperation(operation, node);
            return node;
        }else {
            t.size += 1;
            TreeNode x = TreeNode.NULL;
            if(rank <= trank + t.left.size) {
                x = insert(t.left, rank, operation, trank, t, history);
                t.left = x;
            }else {
                x = insert(t.right, rank, operation, t.left.size + 1 + trank, t, history);
                t.right = x;
            }
        }
        t = maintain(t, rank > (trank + t.left.size + 1));
        t.push();
        return t;
    }

    /**
     * 在第k个位置插入
     * @param rank
     * @param operation
     */
    public void insert(int rank, Operation operation, History history) {
        head = insert(head, rank, operation, 0, TreeNode.NULL, history);
    }

    /**
     * 查找插入位置
     * @param treeNode
     * @param operation
     * @return
     */
    public TreeNode findOperationPosition(TreeNode treeNode, Operation operation, TreeNode father, int teff) {
        if(treeNode == TreeNode.NULL) return father;
        if(operation.pos == teff + treeNode.left.effectiveNode + 1 && treeNode.isEffect) return treeNode;
        if(operation.pos < teff + treeNode.left.effectiveNode + 1) return findOperationPosition(treeNode.left, operation, treeNode, teff);
        else return findOperationPosition(treeNode.right, operation, treeNode, teff + treeNode.left.effectiveNode + (treeNode.isEffect?1:0));
    }

    /**
     * 打印一棵树，并返回深度
     * @param t
     * @param str
     * @param dep
     * @return
     */
    public int printModel(TreeNode t, StringBuilder str, int dep) {
        if(t == TreeNode.NULL) return dep;
        int l = printModel(t.left, new StringBuilder(str.toString() + "\t"), dep + 1);
        System.out.println(str.toString() + t.str + " | " + t.isEffect + " | " + t.effectiveNode);
        int r = printModel(t.right, new StringBuilder(str.toString() + "\t"), dep + 1);
        return Math.max(l, r);
    }

    public void print(TreeNode t) {
        if (t == TreeNode.NULL) return ;
        print(t.left);
        if(t.isEffect)System.out.print(t.str);
        print(t.right);
    }
    /**
     * 打印详细模型
     */
    public void printModel() {
        int dep = printModel(head, new StringBuilder(""), 0);
        System.out.println();
        System.out.println("depth: " + dep);
    }

    /**
     * 打印文本
     */
    public void print() {
        print(head);
        System.out.println();
    }
}
