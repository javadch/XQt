/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.containers;

import xqt.model.expressions.MemberExpression;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class JoinedContainer extends DataContainer {
    public enum JoinOperator {
        EQ, NotEQ, GT, GTEQ,  LT, LTEQ, EqString, NotEqString
    }
    
    public enum JoinType {
        InnerJoin, OuterJoin, LeftOuterJoin, RightOuterJoin
    }
    
    private JoinOperator joinOperator = JoinOperator.EQ;
    private JoinType joinType = JoinType.InnerJoin;
    private DataContainer leftContainer;
    private DataContainer rightContainer;
    private MemberExpression leftKey; // butter to be an attribute pointer
    private MemberExpression rightKey;
    
    public JoinedContainer(){
        this.dataContainerType = DataContainerType.Joined;
    }

    public JoinOperator getJoinOperator() {
        return joinOperator;
    }

    public void setJoinOperator(JoinOperator joinOperator) {
        this.joinOperator = joinOperator;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public void setJoinType(JoinType joinType) {
        this.joinType = joinType;
    }

    public DataContainer getLeftContainer() {
        return leftContainer;
    }

    public void setLeftContainer(DataContainer leftContainer) {
        this.leftContainer = leftContainer;
    }

    public DataContainer getRightContainer() {
        return rightContainer;
    }

    public void setRightContainer(DataContainer rightContainer) {
        this.rightContainer = rightContainer;
    }

    public MemberExpression getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(MemberExpression leftKey) {
        this.leftKey = leftKey;
    }

    public MemberExpression getRightKey() {
        return rightKey;
    }

    public void setRightKey(MemberExpression rightLey) {
        this.rightKey = rightLey;
    }

    
}
