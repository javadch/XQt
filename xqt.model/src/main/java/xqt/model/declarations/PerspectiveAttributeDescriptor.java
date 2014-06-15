package xqt.model.declarations;

import xqt.model.PhraseDescriptor;
import xqt.model.expressions.Expression;

/**
 *
 * @author jfd
 */
public class PerspectiveAttributeDescriptor extends PhraseDescriptor {
    
    private PerspectiveDescriptor perspective;
    private String dataType;
    private String semanticKey;
    private Expression forwardExpression;
    private Expression reverseExpression;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getSemanticKey() {
        return semanticKey;
    }

    public void setSemanticKey(String semanticKey) {
        this.semanticKey = semanticKey;
    }

    public Expression getForwardExpression() {
        return forwardExpression;
    }

    public void setForwardExpression(Expression forwardExpression) {
        this.forwardExpression = forwardExpression;
    }

    public Expression getReverseExpression() {
        return reverseExpression;
    }

    public void setReverseExpression(Expression reverseExpression) {
        this.reverseExpression = reverseExpression;
    }

    public PerspectiveDescriptor getPerspective() {
        return perspective;
    }

    public void setPerspective(PerspectiveDescriptor perspective) {
        this.perspective = perspective;
    }
}
