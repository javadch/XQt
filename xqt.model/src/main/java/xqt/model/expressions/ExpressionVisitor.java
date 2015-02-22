package xqt.model.expressions;

public interface ExpressionVisitor {

    //public void visit(Expression expr);
    public void reset();
    
    public void visit(BinaryExpression expr);
    public void visit(FunctionExpression expr);
    public void visit(InvalidExpression expr);
    public void visit(MemberExpression expr);
    public void visit(ParameterExpression expr);
    public void visit(UnaryExpression expr);
    public void visit(ValueExpression expr);



 /*
    public void visit(DateTimeLiteral expr);

    public void visit(DecimalLiteral expr);


    public void visit(GuidLiteral expr);

    public void visit(ByteLiteral expr);

    public void visit(DoubleLiteral expr);

    public void visit(IntegralLiteral expr);

    public void visit(Int64Literal expr);

    public void visit(StringLiteral expr);
    */
}