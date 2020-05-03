package AbstractTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class BinaryNode <T> extends Node
{
    private T value;
    private Node<T> left;
    private Node<T> right;
    
    public BinaryNode(T value)
    {
        this.value = value;
        this.left = null;
        this.right = null;
    }
    
    /**
     * Return the value of the node.
     *
     * @return - Node's value.
     */
    public T getValue()
    {
        return value;
    }
    
    /**
     * Return a list of all child nodes.
     *
     * @return - List of nodes.
     */
    public List<Node<T>> getNodes()
    {
        ArrayList<Node<T>> nodes = new ArrayList();
        
        if (left != null)
        {
            nodes.add(left);
        }
        
        if (right != null)
        {
            nodes.add(right);
        }
        
        return nodes;
    }
    
    /**
     * Add a node as a left child.
     *
     * @param left - Node to add.
     */
    public void setLeft(Node<T> left)
    {
        this.left = left;
    }
    
    /**
     * Add a node as a right child.
     *
     * @param right - Node to add.
     */
    public void setRight(Node<T> right)
    {
        this.right = right;
    }
    
    /**
     * Create string representation of the node and its child nodes recursively.
     *
     * @param prefix - Prefix to be used for the node.
     * @param isTail - Is the node a tail node.
     *
     * @return - String representation of the node.
     */
    String stringRepresentation(String prefix, boolean isTail)
    {
        String string = prefix + (isTail ? "└── " : "├── ") + this.value.toString();
        
        if (left != null)
        {
            string = string + "\n" + left.stringRepresentation(prefix + (isTail ? "    " : "│   "), (right == null));
        }
        
        if (right != null)
        {
            string = string + "\n" + right.stringRepresentation(prefix + (isTail ? "    " : "│   "), true);
        }
        
        return string;
    }
    
    /**
     * Create string representation of the node.
     *
     * @return - String representation of the node.
     */
    @Override
    public String toString()
    {
        return stringRepresentation("", true);
    }
}