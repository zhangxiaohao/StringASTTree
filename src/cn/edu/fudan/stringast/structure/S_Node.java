package cn.edu.fudan.stringast.structure;

import java.util.ArrayList;

/**
 * Created by zhangxiaohao on 2016/9/21.
 */
public class S_Node {
    public StringBuilder operationString;
    public ArrayList<S_Operation> operations;
    public boolean isEffect;

    public S_Node() {
        this.isEffect = false;
    }

    /**
     * 深拷贝构造函数
     * @param operationString
     * @param operations
     */
    public S_Node(StringBuilder operationString, ArrayList<S_Operation> operations) {
        this.operationString = new StringBuilder(operationString);
        this.operations = new ArrayList<S_Operation>();
        for(S_Operation s_operation : operations) {
            this.operations.add(new S_Operation(s_operation));
        }
    }

    /**
     * 深拷贝构造函数 ，针对创建一个新节点
     * @param operationString
     * @param op
     */
    public S_Node(StringBuilder operationString, S_Operation op) {
        this.operationString = new StringBuilder(operationString);
        operations = new ArrayList<S_Operation>();
        operations.add(new S_Operation(op));
        this.isEffect = true;
    }

    /**
     * 深拷贝构造函数2
     * @param s_node
     */
    public S_Node(S_Node s_node) {
        this.operationString = new StringBuilder(s_node.operationString);
        this.operations = new ArrayList<S_Operation>();
        for(int i=0; i<s_node.operations.size(); i++) {
            this.operations.add(new S_Operation(s_node.operations.get(i)));
        }
        this.isEffect = s_node.isEffect;
    }

    /**
     * 获取节点插入操作
     * @return operaton 节点中的插入操作
     */
    public S_Operation getInsertOperation() {
        return operations.get(0);
    }

    /**
     * 增加节点的删除操作
     * @param op
     */
    public void addDelete(S_Operation op) {
        operations.add(op);
    }

    /**
     * 打印节点中的操作信息
     */
    public void print() {
        System.out.println("-----Node-----");
        for(S_Operation operation : operations) {
            operation.print();
        }
        System.out.println("--------------");
    }

    /**
     * 根据操作s_operation重新更新节点的有效值
     * @param s_operation
     */
    public void isEffect(S_Operation s_operation) {
        isEffect = (operations.get(0).getOperationRelationShip(s_operation) == S_Operation.CAUSAL);
        for(int i=1; i<operations.size(); i++) {
            if(operations.get(i).getOperationRelationShip(s_operation) == S_Operation.CAUSAL) {
                isEffect = false;
            }
        }
    }

    /**
     * 最新状态时候的有效值
     */
    public void isEffect() {
        isEffect = (operations.size() == 1);
    }

}
