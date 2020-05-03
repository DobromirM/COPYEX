package AbstractTree;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class AbstractSyntaxTree
{
    private Node root;
    
    public AbstractSyntaxTree(Node root)
    {
        this.root = root;
    }
    
    /**
     * Return the root of the tree.
     *
     * @return - Tree's root.
     */
    public Node getRoot()
    {
        return root;
    }
    
    /**
     * Create string representation of the tree.
     *
     * @return - String representation of the tree.
     */
    @Override
    public String toString()
    {
        return root.toString();
    }
}

