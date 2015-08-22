/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xqt.model.statements.query;

import xqt.model.data.Resultset;
import xqt.model.data.Variable;
import xqt.model.execution.ExecutionInfo;
import xqt.model.statements.StatementVisitor;

/**
 *
 * @author Javad Chamanara <chamanara@gmail.com>
 */
public class JoinedSelectDescriptor extends SelectDescriptor {
    private SelectDescriptor leftStatement;
    private SelectDescriptor rightStatement;

    public JoinedSelectDescriptor (SelectDescriptor select, SelectDescriptor left, SelectDescriptor right){
        
    }
    
    @Override
    public void accept(StatementVisitor visitor) { //execute the left, right and main
        Resultset result = visitor.visit(this);
        ExecutionInfo exInfo = this.getExecutionInfo();
        exInfo.setExecuted(true);
        if(result != null && this.getTargetClause() != null && this.getTargetClause().getContainer().getId() /*.getVariableName() */!= null){
            Variable var = new Variable();
            var.setExecutionInfo(exInfo);
            var.setName(this.getTargetClause().getContainer().getId() /*.getVariableName() */);
            var.setResult(result);
            exInfo.setVariable(var);
        }
    }

    @Override
    public void prepare(StatementVisitor visitor) { // prepare left, right and main
        visitor.prepare(this);
        
    }
}
