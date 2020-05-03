package AbstractTree;

import java.util.List;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public abstract class Node <T>
{
    /**
     * Create string representation of the node and its child nodes recursively.
     *
     * @param prefix - Prefix to be used for the node.
     * @param isTail - Is the node a tail node.
     *
     * @return - String representation of the node.
     */
    abstract String stringRepresentation(String prefix, boolean isTail);
    
    /**
     * Return the value of the node.
     *
     * @return - Node's value.
     */
    abstract public T getValue();
    
    /**
     * Return a list of all child nodes.
     *
     * @return - List of nodes.
     */
    abstract public List<Node<T>> getNodes();
    
    /**
     * Return the node type based on its value.
     *
     * @return - Type of the node.
     */
    public NodeType getType()
    {
        if (getValue().equals("print"))
        {
            return NodeType.PRINT;
        }
        
        if (getValue().equals("="))
        {
            return NodeType.ASSIGNMENT;
        }
    
        if (getValue().equals("+="))
        {
            return NodeType.ASSIGNMENT;
        }
    
        if (getValue().equals("-="))
        {
            return NodeType.ASSIGNMENT;
        }
    
        if (getValue().equals("*="))
        {
            return NodeType.ASSIGNMENT;
        }
    
        if (getValue().equals("/="))
        {
            return NodeType.ASSIGNMENT;
        }
    
        if (getValue().equals("%="))
        {
            return NodeType.ASSIGNMENT;
        }
        
        if (getValue().equals("loop"))
        {
            return NodeType.LOOP;
        }
        
        if (getValue().equals("conditional"))
        {
            return NodeType.CONDITION;
        }
        
        if (getValue().equals("file"))
        {
            return NodeType.FILE;
        }
        
        if (getValue().equals("block"))
        {
            return NodeType.BLOCK;
        }
        
        if (getValue().equals("lines"))
        {
            return NodeType.LINES;
        }
        
        if (getValue().equals("body"))
        {
            return NodeType.BODY;
        }
        
        if (getValue().equals("if"))
        {
            return NodeType.IF;
        }
        
        if (getValue().equals("else"))
        {
            return NodeType.ELSE;
        }
    
        if (getValue().equals("function"))
        {
            return NodeType.FUNCTION;
        }
    
        if (getValue().equals("signature"))
        {
            return NodeType.SIGNATURE;
        }
    
        if (getValue().equals("name"))
        {
            return NodeType.NAME;
        }
    
        if (getValue().equals("return"))
        {
            return NodeType.RETURN;
        }
    
        if (getValue().equals("arg"))
        {
            return NodeType.ARG;
        }
        
        if (getNodes().size() == 0)
        {
            return NodeType.LEAF;
        }
        
        if (getNodes().size() == 1 && getValue().equals("-"))
        {
            return NodeType.NEGATION;
        }
        
        return NodeType.INFIXOP;
    }
}
