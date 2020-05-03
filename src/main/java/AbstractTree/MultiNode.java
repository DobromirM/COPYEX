package AbstractTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class MultiNode <T> extends Node
{
    private T value;
    private List<Node<T>> nodes;
    
    public MultiNode(T value)
    {
        this.value = value;
        nodes = new ArrayList<>();
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
        return nodes;
    }
    
    /**
     * Add child node.
     *
     * @param node - Node to add.
     */
    public void addNode(Node node)
    {
        nodes.add(node);
    }
    
    /**
     * Create string representation of the node and its child nodes recursively.
     *
     * @param prefix - Prefix to be used for the node.
     * @param isTail - Is the node a tail node.
     *
     * @return - String representation of the node.
     */
    @Override
    String stringRepresentation(String prefix, boolean isTail)
    {
        StringBuilder string = new StringBuilder(prefix + (isTail ? "└── " : "├── ") + this.value.toString());
        
        for (int i = 0; i < nodes.size(); i++)
        {
            if (nodes.get(i) != null)
            {
                string.append("\n").append(nodes.get(i).stringRepresentation(prefix + (isTail ? "    " : "│   "), (i + 1 >= nodes.size())));
            }
        }
        return string.toString();
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
