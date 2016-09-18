package AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory.tree;

public class ANDNode extends Node {
    private Node LeftNode;
    private Node RightNode;

    public Node getLeftNode() {
        return LeftNode;
    }

    public void setLeftNode(Node leftNode) {
        LeftNode = leftNode;
    }

    public Node getRightNode() {
        return RightNode;
    }

    public void setRightNode(Node rightNode) {
        RightNode = rightNode;
    }

    public ANDNode(Node leftNode, Node rightNode) {
        LeftNode = leftNode;
        RightNode = rightNode;
    }

    @Override
    public String inverseExpression() {
        return "("+RightNode.inverseExpression()+"."+LeftNode.inverseExpression()+")";
    }
}
