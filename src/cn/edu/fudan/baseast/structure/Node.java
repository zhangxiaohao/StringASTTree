package cn.edu.fudan.baseast.structure;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/10/2.
 */
public class Node {
    public ArrayList<Operation> operations;
    public StringBuilder str;
    public boolean isEffect;

    public Node() {
        this.isEffect = false;
    }

    /**
     * 根据插入操作构造一个新的节点
     * @param str
     * @param operation
     */
    public Node(StringBuilder str, Operation operation) {
        this.str = new StringBuilder(str);
        this.operations = new ArrayList<Operation>();
        this.operations.add(operation);
        this.isEffect = true;
    }

    /**
     * 深拷贝构造函数
     * @param node
     */
    public Node(Node node) {
        this.str = new StringBuilder(node.str);
        this.operations = new ArrayList<Operation>();
        for(Operation operation : node.operations) {
            this.operations.add(operation);
        }
        this.isEffect = node.isEffect;
    }

    /**
     * 增加一个删除操作
     * @param operation
     */
    public void addDelete(Operation operation) {
        this.operations.add(operation);
    }

    /**
     * 根据操作operation重新更新节点的有效值
     * @param operation
     */
    public void isEffect(Operation operation) {
        isEffect = operations.get(0).isCausalOrdering(operation);
        for(int i=1; i<operations.size(); i++) {
            if(operations.get(i).isCausalOrdering(operation)) {
                isEffect = false;
            }
        }
    }

    /**
     * 将文本回溯到最新状态
     */
    public void isEffect() {
        isEffect = (operations.size() == 1);
    }
}
