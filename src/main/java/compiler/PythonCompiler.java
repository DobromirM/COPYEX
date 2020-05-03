package compiler;

import AbstractTree.AbstractSyntaxTree;
import AbstractTree.Node;
import AbstractTree.NodeType;

import java.util.List;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class PythonCompiler
{
    private Integer indentLevel = 0;
    
    /**
     * Compile abstract tree into python code.
     *
     * @param tree - Abstract syntax tree.
     *
     * @return - Compiled python.
     */
    public String compilePython(AbstractSyntaxTree tree)
    {
        return walk(tree.getRoot());
    }
    
    /**
     * Recursively walk the abstract tree and compile nodes into code.
     *
     * @param node - Node to be compiled.
     *
     * @return - Compiled python code.
     */
    private String walk(Node node)
    {
        if (node == null)
        {
            return "";
        }
        
        StringBuilder code = new StringBuilder();
        List<Node> nodes = node.getNodes();
        
        if (node.getType() == NodeType.LEAF)
        {
            return walkLeaf(node, code);
        }
        else if (node.getType() == NodeType.ASSIGNMENT)
        {
            return walkAssignment(node, code, nodes);
        }
        else if (node.getType() == NodeType.NEGATION)
        {
            return walkNegation(node, code, nodes);
        }
        else if (node.getType() == NodeType.RETURN)
        {
            return walkReturn(node, code, nodes);
        }
        else if (node.getType() == NodeType.INFIXOP)
        {
            return walkInfixop(node, code, nodes);
        }
        else if (node.getType() == NodeType.PRINT)
        {
            return walkPrint(code, nodes);
        }
        else if (node.getType() == NodeType.NAME)
        {
            return walkName(node, code);
        }
        else if (node.getType() == NodeType.ARG)
        {
            return walkArgs(node, code);
        }
        else if (node.getType() == NodeType.CONDITION)
        {
            return walkConditional(code, nodes);
        }
        else if (node.getType() == NodeType.LOOP)
        {
            return walkLoop(code, nodes);
        }
        else if (node.getType() == NodeType.FUNCTION)
        {
            return walkFunction(code, nodes);
        }
        else
        {
            for (Node n : nodes)
            {
                code.append(walk(n));
            }
            
            return code.toString();
        }
    }
    
    /**
     * Walk function nodes and compile them into python.
     *
     * @param code  - String builder holding compiled code.
     * @param nodes - List of function nodes.
     *
     * @return - Compiled python code.
     */
    private String walkFunction(StringBuilder code, List<Node> nodes)
    {
        code.append("def ");
        
        List<Node> signature = nodes.get(0).getNodes();
        code.append(walk(signature.get(0)));
        
        code.append("(");
        for (int i = 1; i < signature.size(); i++)
        {
            code.append(walk(signature.get(i)));
            
            if (i < signature.size() - 1)
            {
                code.append(",");
            }
        }
        code.append("):\n");
        
        indentLevel = indentLevel + 1;
        
        List<Node> body = nodes.get(1).getNodes();
        
        walkLines(code, body);
        
        indentLevel = indentLevel - 1;
        code.append("\n");
        return code.toString();
    }
    
    /**
     * Walk loop nodes and compile them into python.
     *
     * @param code  - String builder holding compiled code.
     * @param nodes - List of loop nodes.
     *
     * @return - Compiled python code.
     */
    private String walkLoop(StringBuilder code, List<Node> nodes)
    {
        code.append("while ");
        code.append(walk(nodes.get(0)));
        code.append(":\n");
        
        indentLevel = indentLevel + 1;
        
        List<Node> body = nodes.get(1).getNodes();
        
        walkLines(code, body);
        
        indentLevel = indentLevel - 1;
        
        return code.toString();
    }
    
    /**
     * Walk arguments node and compile it into python.
     *
     * @param node -  Argument node.
     * @param code -  String builder holding compiled code.
     *
     * @return - Compiled python code.
     */
    private String walkArgs(Node node, StringBuilder code)
    {
        List<Node> name = node.getNodes();
        code.append(name.get(0).getValue());
        return code.toString();
    }
    
    /**
     * Walk name node and compile it into python.
     *
     * @param node -  Name node.
     * @param code -  String builder holding compiled code.
     *
     * @return - Compiled python code.
     */
    private String walkName(Node node, StringBuilder code)
    {
        List<Node> name = node.getNodes();
        code.append(name.get(0).getValue());
        return code.toString();
    }
    
    /**
     * Walk print nodes and compile them into python.
     *
     * @param code  -  String builder holding compiled code.
     * @param nodes -  List of print nodes.
     *
     * @return - Compiled python code.
     */
    private String walkPrint(StringBuilder code, List<Node> nodes)
    {
        code.append("print(");
        code.append(walk(nodes.get(0)));
        code.append(")\n");
        return code.toString();
    }
    
    /**
     * Walk infix operator node and compile it into python.
     *
     * @param node  - Infix operator node.
     * @param code  - String builder holding compiled code.
     * @param nodes - List of nodes for body of infix operator.
     *
     * @return - Compiled python code.
     */
    private String walkInfixop(Node node, StringBuilder code, List<Node> nodes)
    {
        code.append(walk(nodes.get(0)));
        code.append(" ");
        code.append(node.getValue()).append(" ");
        node.getType();
        code.append(walk(nodes.get(1)));
        
        return code.toString();
    }
    
    /**
     * Walk return node and compile it into python.
     *
     * @param node  - Return node.
     * @param code  - String builder holding compiled code.
     * @param nodes - List of nodes for body of return operator.
     *
     * @return - Compiled python code.
     */
    private String walkReturn(Node node, StringBuilder code, List<Node> nodes)
    {
        code.append(node.getValue());
        code.append(" ");
        code.append(walk(nodes.get(0)));
        
        return code.toString();
    }
    
    /**
     * Walk conditional node and compile it into python.
     *
     * @param code  - String builder holding compiled code.
     * @param nodes - List of nodes for the conditional operator.
     *
     * @return - Compiled python code.
     */
    private String walkConditional(StringBuilder code, List<Node> nodes)
    {
        code.append("if ");
        
        code.append(walk(nodes.get(0)));
        code.append(":");
        code.append("\n");
        
        indentLevel = indentLevel + 1;
        
        List<Node> ifBody = nodes.get(1).getNodes();
        
        walkLines(code, ifBody);
        
        indentLevel = indentLevel - 1;
        
        if (nodes.size() == 3)
        {
            for (int i = 0; i < indentLevel * 4; i++)
            {
                code.append(" ");
            }
            code.append("else:\n");
            
            indentLevel = indentLevel + 1;
            
            List<Node> elseBody = nodes.get(2).getNodes();
            
            walkLines(code, elseBody);
            
            indentLevel = indentLevel - 1;
        }
        
        return code.toString();
    }
    
    /**
     * Walk negation node and compile it into python.
     *
     * @param node  - Negation node.
     * @param code  - String builder holding compiled code.
     * @param nodes - List of nodes for body of negation node.
     *
     * @return - Compiled python code.
     */
    private String walkNegation(Node node, StringBuilder code, List<Node> nodes)
    {
        code.append(node.getValue());
        code.append(walk(nodes.get(0)));
        
        return code.toString();
    }
    
    /**
     * Walk assignment node and compile it into python.
     *
     * @param node  - Assignment node.
     * @param code  - String builder holding compiled code.
     * @param nodes - List of nodes for body of assignment node.
     *
     * @return - Compiled python code.
     */
    private String walkAssignment(Node node, StringBuilder code, List<Node> nodes)
    {
        code.append(walk(nodes.get(0)));
        code.append(" ").append(node.getValue()).append(" ");
        node.getType();
        code.append(walk(nodes.get(1)));
        code.append("\n");
        
        return code.toString();
    }
    
    /**
     * Walk leaf node and compile it into python.
     *
     * @param node - Leaf node.
     * @param code - String builder holding compiled code.
     *
     * @return - Compiled python code.
     */
    private String walkLeaf(Node node, StringBuilder code)
    {
        code.append(node.getValue());
        return code.toString();
    }
    
    /**
     * Walk lines node and compile them into python.
     *
     * @param code   - String builder holding compiled code.
     * @param ifBody - List of line nodes.
     */
    private void walkLines(StringBuilder code, List<Node> ifBody)
    {
        for (Node n : ifBody)
        {
            if (n.getType() == NodeType.LINES)
            {
                List<Node> lines = n.getNodes();
                
                for (Node l : lines)
                {
                    if (l != null)
                    {
                        indent(code, l);
                    }
                }
            }
            else
            {
                indent(code, n);
            }
        }
    }
    
    /**
     * Add lines by adding indent.
     *
     * @param code - String builder holding compiled code.
     * @param l    - Lines to walk with indent.
     */
    private void indent(StringBuilder code, Node l)
    {
        for (int i = 0; i < indentLevel * 4; i++)
        {
            code.append(" ");
        }
        code.append(walk(l));
    }
}
