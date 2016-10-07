package cn.edu.fudan.stringast.structure;

import cn.edu.fudan.baseast.structure.History;
import cn.edu.fudan.baseast.structure.Operation;
import cn.edu.fudan.baseast.structure.TreeNode;

/**
 * Created by zhangxiaohao on 2016/10/2.
 */
public class SBT {
    public S_TreeNode head;

    public SBT() {
        head = S_TreeNode.NULL;
    }

    /**
     * 右旋
     * @param t
     */
    public S_TreeNode rightRotate(S_TreeNode t) {
        S_TreeNode k = t.left;
        S_TreeNode t_father = t.father;
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
    public S_TreeNode leftRotate(S_TreeNode t) {
        S_TreeNode k = t.right;
        S_TreeNode t_father = t.father;
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
    public S_TreeNode maintain(S_TreeNode t, boolean flag) {
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

    public S_TreeNode findSmall(S_TreeNode t) {
        S_TreeNode p = t;
        if(p.right != S_TreeNode.NULL) {
            p = p.right;
            while(p.left != S_TreeNode.NULL) p = p.left;
        }
        return p;
    }

    /**
     * 找到一个节点的后继 O(logn)
     * @param t
     * @return
     */
    public S_TreeNode findNext(S_TreeNode t) {
        S_TreeNode p = t;
        while(p != S_TreeNode.NULL) {
            if(p.right != S_TreeNode.NULL) return findSmall(p);
            else {
                S_TreeNode q;
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
    public void isEffect(S_TreeNode treeNode, S_Operation operation) {
        treeNode.isEffect(operation);
        S_TreeNode p = treeNode;
        while(p != S_TreeNode.NULL) {
            p.push();
            p = p.father;
        }
    }

    /**
     * 将节点回溯到最新状态
     * @param treeNode
     */
    public void isEffect(S_TreeNode treeNode) {
        treeNode.isEffect();
        S_TreeNode p = treeNode;
        while(p != S_TreeNode.NULL) {
            p.push();
            p = p.father;
        }
    }

    /**
     * 给定一个树节点取得排名
     * @param treeNode
     * @return
     */
    public int getRank(S_TreeNode treeNode) {
        int rank = 0;
        S_TreeNode p = treeNode;
        while(treeNode != S_TreeNode.NULL) {
            if(treeNode.left != p) rank += treeNode.left.size + 1;
            p = treeNode;
            treeNode = treeNode.father;
        }
        return rank - 1;
    }

    /**
     * 在rank位置插入由operation创建的节点
     * @param t
     * @param rank
     * @param operation
     * @param trank
     * @param father
     * @param history
     * @return
     */
    private S_TreeNode insert(S_TreeNode t, int rank, S_Operation operation, int trank, S_TreeNode father, S_History history) {
        if(t == S_TreeNode.NULL) {
            S_TreeNode node = new S_TreeNode(operation.getOperationString(), operation, father);
            history.addOperation(operation, node);
            return node;
        }else {
            t.size += 1;
            S_TreeNode x = S_TreeNode.NULL;
            if(rank <= trank + t.left.size) {
                x = insert(t.left, rank, operation, trank, t, history);
                t.left = x;
            }else {
                x = insert(t.right, rank, operation, t.left.size + 1 + trank, t, history);
                t.right = x;
            }
        }
        t.push();
        t = maintain(t, rank > (trank + t.left.size + 1));
        t.push();
        return t;
    }

    /**
     * 在rank位置插入node节点
     * @param t
     * @param rank
     * @param node
     * @param trank
     * @param father
     * @return
     */
    private S_TreeNode insert(S_TreeNode t, int rank, S_TreeNode node, int trank, S_TreeNode father) {
        if(t == S_TreeNode.NULL) {
            node.father = father;
            return node;
        }else {
            t.size += 1;
            S_TreeNode x = S_TreeNode.NULL;
            if(rank <= trank + t.left.size) {
                x = insert(t.left, rank, node, trank, t);
                t.left = x;
            }else {
                x = insert(t.right, rank, node, t.left.size + 1 + trank, t);
                t.right = x;
            }
        }
        t.push();
        t = maintain(t, rank > (trank + t.left.size + 1));
        t.push();
        return t;

    }

    /**
     * 在第k个位置插入操作新建的节点
     * @param rank
     * @param operation
     */
    public void insert(int rank, S_Operation operation, S_History history) {
        head = insert(head, rank, operation, 0, S_TreeNode.NULL, history);
    }

    /**
     * 在第k个位置插入新的节点
     * @param rank
     * @param s_treeNode
     */
    public void insert(int rank, S_TreeNode s_treeNode) {
        head = insert(head, rank, s_treeNode, 0, S_TreeNode.NULL);
    }

    /**
     * 查找插入位置
     * @param treeNode
     * @param operation
     * @return */
    public S_TreeNode findOperationPosition(S_TreeNode treeNode, S_Operation operation, S_TreeNode father, int teff, int [] shift) {
        if(treeNode == S_TreeNode.NULL) {
            shift[0] = 0;
            return father;
        }
        if(operation.position > treeNode.left.effectiveNode + teff && treeNode.isEffect &&
          operation.position <= teff + treeNode.left.effectiveNode + treeNode.operationString.length()) {
            shift[0] = operation.position - teff - treeNode.left.effectiveNode;
            return treeNode;
        }
        if(operation.position <= teff + treeNode.left.effectiveNode)
            return findOperationPosition(treeNode.left, operation, treeNode, teff, shift);
        else
            return findOperationPosition(treeNode.right, operation, treeNode, teff + treeNode.left.effectiveNode + (treeNode.isEffect?treeNode.operationString.length():0), shift);
    }

    /**
     * 打印一棵树，并返回深度
     * @param t
     * @param str
     * @param dep
     * @return
     */
    public int printModel(S_TreeNode t, StringBuilder str, int dep) {
        if(t == S_TreeNode.NULL) return dep;
        int l = printModel(t.left, new StringBuilder(str.toString() + "\t"), dep + 1);
        System.out.println(str.toString() + t.operationString + " | " + t.isEffect + " | " + t.effectiveNode);
        int r = printModel(t.right, new StringBuilder(str.toString() + "\t"), dep + 1);
        return Math.max(l, r);
    }

    public void print(S_TreeNode t) {
        if (t == S_TreeNode.NULL) return ;
        print(t.left);
        if(t.isEffect)System.out.print(t.operationString);
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
