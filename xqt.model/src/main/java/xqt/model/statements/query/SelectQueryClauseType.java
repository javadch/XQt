/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements.query;

/**
 *
 * @author standard
 */
public enum SelectQueryClauseType {
    Qualifier,
    Projection,
    Source,
    Target,
    Anchor,
    Filter,
    Order,
    Limit,
    Group,
}

// use SelectClauseType.valueOf("string") and SelectClauseType.Source.toString() for conversions