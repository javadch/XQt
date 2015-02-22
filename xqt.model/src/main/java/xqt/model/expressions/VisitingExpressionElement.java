/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.expressions;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public interface VisitingExpressionElement {
    public void accept(ExpressionVisitor visitor);
}
