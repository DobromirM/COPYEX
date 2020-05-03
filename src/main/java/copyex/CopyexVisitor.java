package copyex;

import AbstractTree.BinaryNode;
import AbstractTree.MultiNode;
import AbstractTree.Node;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class CopyexVisitor extends CopyexParserBaseVisitor<Node>
{
    
    private Map<String, Map<String, List<String>>> scope = new HashMap<>();
    Map<String, String> declaredFunctions = new HashMap<>();
    private String currentScope;
    
    /**
     * Visitor Constructor.
     * Create scopes and assign them.
     */
    public CopyexVisitor()
    {
        Map<String, List<String>> global = new HashMap();
        
        List<String> declaredNum = new ArrayList<>();
        List<String> declaredBool = new ArrayList<>();
        List<String> assignedNum = new ArrayList<>();
        List<String> assignedBool = new ArrayList<>();
        
        global.put("declaredNum", declaredNum);
        global.put("declaredBool", declaredBool);
        global.put("assignedNum", assignedNum);
        global.put("assignedBool", assignedBool);
        
        scope.put("global", global);
        
        currentScope = "global";
    }
    
    /**
     * Visit file and create node.
     *
     * @param context - File context.
     *
     * @return - File node.
     */
    @Override
    public Node visitFile(CopyexParser.FileContext context)
    {
        MultiNode node = new MultiNode("file");
        
        for (int i = 0; i < context.fileBlock().size(); i++)
        {
            node.addNode(this.visit(context.fileBlock(i)));
        }
        
        return node;
    }
    
    /**
     * Visit code block and create node.
     *
     * @param context - Code block context.
     *
     * @return - Code block node.
     */
    @Override
    public Node visitCodeBlock(CopyexParser.CodeBlockContext context)
    {
        MultiNode node = new MultiNode("block");
        for (int i = 0; i < context.block().size(); i++)
        {
            node.addNode(this.visit(context.block(i)));
        }
        
        return node;
    }
    
    /**
     * Visit lines and create node.
     *
     * @param context - Lines context.
     *
     * @return - Lines node.
     */
    @Override
    public Node visitLines(CopyexParser.LinesContext context)
    {
        MultiNode node = new MultiNode("lines");
        for (int i = 0; i < context.line().size(); i++)
        {
            node.addNode(this.visit(context.line(i)));
        }
        
        return node;
    }
    
    /**
     * Visit line and create node.
     *
     * @param context - Line context.
     *
     * @return - Line node.
     */
    @Override
    public Node visitLine(CopyexParser.LineContext context)
    {
        if (context.statement() != null)
        {
            Node node = this.visit(context.statement());
            return node;
        }
        else
        {
            return null;
        }
    }
    
    /**
     * Visit number initialisation and create node.
     *
     * @param context - Number initialisation context.
     *
     * @return - Number initialisation node.
     */
    @Override
    public Node visitInitNum(CopyexParser.InitNumContext context)
    {
        String var = context.name.getText();
        
        if (!scope.get(currentScope).get("declaredNum").contains(var))
        {
            scope.get(currentScope).get("declaredNum").add(var);
        }
        
        if (!scope.get(currentScope).get("assignedNum").contains(var))
        {
            scope.get(currentScope).get("assignedNum").add(var);
        }
        
        scope.get(currentScope).get("declaredBool").remove(var);
        scope.get(currentScope).get("assignedBool").remove(var);
        
        BinaryNode node = new BinaryNode<>(context.assign.getText());
        node.setLeft(new BinaryNode<>(context.name.getText()));
        node.setRight(this.visit(context.value));
        
        return node;
    }
    
    /**
     * Visit number declaration and create node.
     *
     * @param context - Number declaration context.
     *
     * @return - Number declaration node.
     */
    @Override
    public Node visitDeclarationNum(CopyexParser.DeclarationNumContext context)
    {
        String var = context.name.getText();
        
        if (!scope.get(currentScope).get("declaredNum").contains(var))
        {
            scope.get(currentScope).get("declaredNum").add(context.name.getText());
        }
        
        scope.get(currentScope).get("declaredBool").remove(var);
        scope.get(currentScope).get("assignedBool").remove(var);
        scope.get(currentScope).get("assignedNum").remove(var);
        
        return null;
    }
    
    /**
     * Visit number assignment and create node.
     *
     * @param context - Number assignment context.
     *
     * @return - Number assignment node.
     */
    @Override
    public Node visitAssignmentNum(CopyexParser.AssignmentNumContext context)
    {
        String var = context.name.getText();
        
        if (checkScopes("declaredNum", var))
        {
            BinaryNode node = new BinaryNode<>(context.assign.getText());
            node.setLeft(new BinaryNode<>(var));
            node.setRight(this.visit(context.value));
            
            if (!checkScopes("assignedNum", var))
            {
                scope.get(currentScope).get("assignedNum").add(context.name.getText());
            }
            
            return node;
        }
        else
        {
            if (scope.get(currentScope).get("declaredBool").contains(var))
            {
                String location = location(context);
                throw new CopyexException("Compile error!\n'" + var + "' is not of type number!\nLocation " + location);
            }
            else
            {
                String location = location(context);
                throw new CopyexException("Compile error!\n'" + var + "' is not a defined variable!\nLocation " + location);
            }
        }
    }
    
    /**
     * Visit boolean initialisation and create node.
     *
     * @param context - Boolean initialisation context.
     *
     * @return - Boolean initialisation node.
     */
    @Override
    public Node visitInitBool(CopyexParser.InitBoolContext context)
    {
        
        String var = context.name.getText();
        
        try
        {
            ((CopyexParser.ComparisonOperationContext) context.value).logicalOp();
        }
        catch (Exception e)
        {
            String location = location(context);
            throw new CopyexException("Compile error!\n'" + var + "' is not of type boolean!\nLocation " + location);
        }
        
        if (!scope.get(currentScope).get("declaredBool").contains(var))
        {
            scope.get(currentScope).get("declaredBool").add(var);
        }
        
        if (!scope.get(currentScope).get("assignedBool").contains(var))
        {
            scope.get(currentScope).get("assignedBool").add(var);
        }
        
        scope.get(currentScope).get("declaredNum").remove(var);
        scope.get(currentScope).get("assignedNum").remove(var);
        
        BinaryNode node = new BinaryNode<>(context.assign.getText());
        node.setLeft(new BinaryNode<>(context.name.getText()));
        node.setRight(this.visit(context.value));
        
        return node;
    }
    
    /**
     * Visit boolean declaration and create node.
     *
     * @param context - Boolean declaration context.
     *
     * @return - Boolean declaration node.
     */
    @Override
    public Node visitDeclarationBool(CopyexParser.DeclarationBoolContext context)
    {
        String var = context.name.getText();
        
        if (!scope.get(currentScope).get("declaredBool").contains(var))
        {
            scope.get(currentScope).get("declaredBool").add(context.name.getText());
        }
        
        scope.get(currentScope).get("declaredNum").remove(var);
        scope.get(currentScope).get("assignedNum").remove(var);
        scope.get(currentScope).get("assignedBool").remove(var);
        
        return null;
    }
    
    /**
     * Visit boolean assignment and create node.
     *
     * @param context - Boolean assignment context.
     *
     * @return - Boolean assignment node.
     */
    @Override
    public Node visitAssignmentBool(CopyexParser.AssignmentBoolContext context)
    {
        String var = context.name.getText();
        
        if (checkScopes("declaredBool", var))
        {
            BinaryNode node = new BinaryNode<>(context.assign.getText());
            node.setLeft(new BinaryNode<>(var));
            node.setRight(this.visit(context.value));
            
            if (!checkScopes("assignedBool", var))
            {
                scope.get(currentScope).get("assignedBool").add(context.name.getText());
            }
            
            return node;
        }
        else
        {
            if (scope.get(currentScope).get("declaredNum").contains(var))
            {
                String location = location(context);
                throw new CopyexException("Compile error!\n'" + var + "' is not of type boolean!\nLocation " + location);
            }
            else
            {
                String location = location(context);
                throw new CopyexException("Compile error!\n'" + var + "' is not a defined variable!\nLocation " + location);
            }
        }
    }
    
    /**
     * Visit function call and create node.
     *
     * @param context - Function call context.
     *
     * @return - Function call node.
     */
    @Override
    public Node visitFunctionCall(CopyexParser.FunctionCallContext context)
    {
        String funcName = context.value.getText();
        if (declaredFunctions.containsKey(funcName))
        {
            BinaryNode node = new BinaryNode<>(context.getText() + "\n");
            
            return node;
        }
        else
        {
            String location = location(context);
            throw new CopyexException("Compile error!\n'" + funcName + "'() is not a defined function!\nLocation " + location);
        }
    }
    
    /**
     * Visit function assignment and create node.
     *
     * @param context - Function assignment context.
     *
     * @return - Function assignment node.
     */
    @Override
    public Node visitAssignmentFunction(CopyexParser.AssignmentFunctionContext context)
    {
        String var = context.name.getText();
        String funcName = context.value.getText();
        
        if (declaredFunctions.containsKey(funcName))
        {
            String funcType = declaredFunctions.get(funcName);
            
            if (funcType.equals("bool"))
            {
                if (checkScopes("declaredBool", var))
                {
                    BinaryNode node = new BinaryNode<>(context.assign.getText());
                    node.setLeft(new BinaryNode<>(var));
                    
                    StringBuilder parameters = new StringBuilder();
                    if (context.parameters() != null)
                    {
                        for (ParseTree para : context.parameters().children)
                        {
                            parameters.append(para.getText());
                        }
                    }
                    
                    node.setRight(new BinaryNode<>(funcName + "(" + parameters + ")"));
                    
                    if (!checkScopes("assignedBool", var))
                    {
                        scope.get(currentScope).get("assignedBool").add(context.name.getText());
                    }
                    
                    return node;
                }
                else
                {
                    if (checkScopes("declaredNum", var))
                    {
                        String location = location(context);
                        throw new CopyexException("Compile error!\n'" + var + "' is not of type boolean!\nLocation " + location);
                    }
                    else
                    {
                        String location = location(context);
                        throw new CopyexException("Compile error!\n'" + var + "' is not a defined variable!\nLocation " + location);
                    }
                }
            }
            
            if (funcType.equals("num"))
            {
                if (checkScopes("declaredNum", var))
                {
                    BinaryNode node = new BinaryNode<>(context.assign.getText());
                    node.setLeft(new BinaryNode<>(var));
                    
                    StringBuilder parameters = new StringBuilder();
                    
                    if (context.parameters() != null)
                    {
                        for (ParseTree para : context.parameters().children)
                        {
                            parameters.append(para.getText());
                        }
                    }
                    
                    node.setRight(new BinaryNode<>(funcName + "(" + parameters + ")"));
                    
                    if (!checkScopes("assignedNum", var))
                    {
                        scope.get(currentScope).get("assignedNum").add(context.name.getText());
                    }
                    
                    return node;
                }
                else
                {
                    if (checkScopes("declaredBool", var))
                    {
                        String location = location(context);
                        throw new CopyexException("Compile error!\n'" + var + "' is not of type number!\nLocation " + location);
                    }
                    else
                    {
                        String location = location(context);
                        throw new CopyexException("Compile error!\n'" + var + "' is not a defined variable!\nLocation " + location);
                    }
                }
            }
            
            String location = location(context);
            throw new CopyexException("Compile error!\n'" + funcName + "'() does not return a value!\nLocation " + location);
        }
        else
        {
            String location = location(context);
            throw new CopyexException("Compile error!\n'" + funcName + "'() is not a defined function!\nLocation " + location);
        }
    }
    
    /**
     * Visit augmented assignment and create node.
     *
     * @param context - Augmented assignment context.
     *
     * @return - Augmented assignment node.
     */
    @Override
    public Node visitAugmented(CopyexParser.AugmentedContext context)
    {
        String variable = context.name.getText();
        if (checkScopes("declaredNum", variable))
        {
            if (checkScopes("assignedNum", variable))
            {
                BinaryNode node = new BinaryNode<>(context.operator.getText() + context.assign.getText());
                node.setLeft(new BinaryNode<>(variable));
                node.setRight(this.visit(context.value));
                return node;
            }
            else
            {
                String location = location(context);
                throw new CopyexException("Initialization error!\n'" + variable + "' has not been initialized yet!\nLocation: " + location);
            }
        }
        else
        {
            String location = location(context);
            throw new CopyexException("Compile error!\n'" + variable + "' is not a defined variable!\nLocation " + location);
        }
    }
    
    /**
     * Visit evaluation and create node.
     *
     * @param context - Evaluation context.
     *
     * @return - Evaluation node.
     */
    @Override
    public Node visitEvaluation(CopyexParser.EvaluationContext context)
    {
        BinaryNode node = new BinaryNode<>(context.op.getText());
        node.setLeft(this.visit(context.arithmetic()));
        return node;
    }
    
    /**
     * Visit conditional and create node.
     *
     * @param context - Conditional context.
     *
     * @return - Conditional node.
     */
    @Override
    public Node visitConditional(CopyexParser.ConditionalContext context)
    {
        MultiNode<String> node = new MultiNode<>("conditional");
        
        node.addNode(this.visit(context.condition));
        
        MultiNode<String> conditionTrue = new MultiNode<>("if");
        
        for (int i = 0; i < context.ifBlock.block().size(); i++)
        {
            conditionTrue.addNode(this.visit(context.ifBlock.block(i)));
        }
        
        node.addNode(conditionTrue);
        
        if (context.elseBlock != null)
        {
            MultiNode<String> conditionFalse = new MultiNode<>(context.otherwise.getText());
            
            for (int i = 0; i < context.elseBlock.block().size(); i++)
            {
                conditionFalse.addNode(this.visit(context.elseBlock.block(i)));
            }
            
            node.addNode(conditionFalse);
        }
        
        return node;
    }
    
    /**
     * Visit loop and create node.
     *
     * @param context - Loop context.
     *
     * @return - Loop node.
     */
    @Override
    public Node visitLoop(CopyexParser.LoopContext context)
    {
        BinaryNode<String> node = new BinaryNode<>("loop");
        node.setLeft(this.visit(context.condition));
        
        MultiNode<String> body = new MultiNode<>("body");
        
        for (int i = 0; i < context.whileBlock.block().size(); i++)
        {
            body.addNode(this.visit(context.whileBlock.block(i)));
        }
        
        node.setRight(body);
        
        return node;
    }
    
    /**
     * Visit function and create node.
     *
     * @param context - Function context.
     *
     * @return - Function node.
     */
    @Override
    public Node visitFunction(CopyexParser.FunctionContext context)
    {
        BinaryNode<String> node = new BinaryNode<>("function");
        MultiNode<String> signature = new MultiNode<>("signature");
        
        BinaryNode<String> funcName = new BinaryNode<>("name");
        funcName.setLeft(new BinaryNode<>(context.name.getText()));
        signature.addNode(funcName);
        
        Map<String, List<String>> functionScope = new HashMap();
        
        List<String> declaredNum = new ArrayList<>();
        List<String> declaredBool = new ArrayList<>();
        List<String> assignedNum = new ArrayList<>();
        List<String> assignedBool = new ArrayList<>();
        
        functionScope.put("declaredNum", declaredNum);
        functionScope.put("declaredBool", declaredBool);
        functionScope.put("assignedNum", assignedNum);
        functionScope.put("assignedBool", assignedBool);
        
        scope.put(context.name.getText(), functionScope);
        
        declaredFunctions.put(context.name.getText(), context.type.getText());
        
        currentScope = context.name.getText();
        if (context.arguments() != null)
        {
            for (int i = 0; i < context.arguments().ID().size(); i++)
            {
                String type = context.arguments().varType(i).getText();
                
                if (type.equals("num"))
                {
                    scope.get(currentScope).get("declaredNum").add(context.arguments().ID(i).getText());
                    scope.get(currentScope).get("assignedNum").add(context.arguments().ID(i).getText());
                }
                
                if (type.equals("bool"))
                {
                    scope.get(currentScope).get("declaredBool").add(context.arguments().ID(i).getText());
                    scope.get(currentScope).get("assignedBool").add(context.arguments().ID(i).getText());
                }
                
                BinaryNode<String> arg = new BinaryNode<>("arg");
                arg.setLeft(new BinaryNode<>(context.arguments().ID(i).getText()));
                signature.addNode(arg);
            }
        }
        MultiNode<String> body = new MultiNode<>("body");
        
        for (int i = 0; i < context.funcBlock.block().size(); i++)
        {
            body.addNode(this.visit(context.funcBlock.block(i)));
        }
        
        if (context.returnNum() != null)
        {
            BinaryNode<String> returnNode = new BinaryNode<>("return");
            returnNode.setLeft(this.visit(context.returnNum().arithmetic()));
            body.addNode(returnNode);
        }
        
        if (context.returnBool() != null)
        {
            BinaryNode<String> returnNode = new BinaryNode<>("return");
            returnNode.setLeft(this.visit(context.returnBool().logical()));
            body.addNode(returnNode);
        }
        
        node.setLeft(signature);
        node.setRight(body);
        
        currentScope = "global";
        
        return node;
    }
    
    /**
     * Visit binary operation and create node.
     *
     * @param context - Binary operation context.
     *
     * @return - Binary operation node.
     */
    @Override
    public Node visitBinaryOperation(CopyexParser.BinaryOperationContext context)
    {
        BinaryNode node = new BinaryNode<>(context.operator.getText());
        node.setLeft(this.visit(context.left));
        node.setRight(this.visit(context.right));
        
        return node;
    }
    
    /**
     * Visit grouping negation and create node.
     *
     * @param context - Grouping negation context.
     *
     * @return - Grouping negation node.
     */
    @Override
    public Node visitGroupingNegation(CopyexParser.GroupingNegationContext context)
    {
        BinaryNode node = new BinaryNode(context.operator.getText());
        node.setLeft(this.visit(context.grouping()));
        
        return node;
    }
    
    /**
     * Visit value negation and create node.
     *
     * @param context - Value negation context.
     *
     * @return - Value negation node.
     */
    @Override
    public Node visitValueNegation(CopyexParser.ValueNegationContext context)
    {
        BinaryNode node = new BinaryNode(context.operator.getText());
        node.setLeft(new BinaryNode<>(context.value.getText()));
        
        return node;
    }
    
    /**
     * Visit nested negation and create node.
     *
     * @param context - Nested negation context.
     *
     * @return - Nested negation node.
     */
    @Override
    public Node visitNestedNegation(CopyexParser.NestedNegationContext context)
    {
        BinaryNode node = new BinaryNode(context.operator.getText());
        node.setLeft(this.visit(context.negation()));
        
        return node;
    }
    
    /**
     * Visit grouping and create node.
     *
     * @param context - Grouping negation context.
     *
     * @return - Grouping negation node.
     */
    @Override
    public Node visitGrouping(CopyexParser.GroupingContext context)
    {
        return this.visit(context.arithmetic());
    }
    
    /**
     * Visit number and create node.
     *
     * @param context - Number context.
     *
     * @return - Number node.
     */
    @Override
    public Node visitNumber(CopyexParser.NumberContext context)
    {
        return new BinaryNode<>(context.getText());
    }
    
    /**
     * Visit variable and create node.
     *
     * @param context - Variable context.
     *
     * @return - Variable node.
     */
    @Override
    public Node visitVariable(CopyexParser.VariableContext context)
    {
        String variable = context.getText();
        if (checkScopes("assignedNum", variable) || checkScopes("assignedBool", variable))
        {
            return new BinaryNode<>(context.getText());
        }
        else
        {
            String location = location(context);
            throw new CopyexException("Initialization error!\n'" + variable + "' has not been initialized yet!\nLocation: " + location);
        }
    }
    
    /**
     * Visit comparison operator and create node.
     *
     * @param context - Comparison operator context.
     *
     * @return - Comparison operator node.
     */
    @Override
    public Node visitComparisonOperation(CopyexParser.ComparisonOperationContext context)
    {
        BinaryNode node = new BinaryNode<>(context.operator.getText());
        node.setLeft(this.visit(context.left));
        node.setRight(this.visit(context.right));
        
        return node;
    }
    
    /**
     * Visit not operator and create node.
     *
     * @param context - Not operator context.
     *
     * @return - Not operator node.
     */
    @Override
    public Node visitNotOperation(CopyexParser.NotOperationContext context)
    {
        BinaryNode node = new BinaryNode<>(context.operator.getText());
        node.setLeft(this.visit(context.value));
        
        return node;
    }
    
    /**
     * Visit binary logic operation and create node.
     *
     * @param context - Binary logic operation context.
     *
     * @return - Binary logic operation node.
     */
    @Override
    public Node visitBinaryLogicOperation(CopyexParser.BinaryLogicOperationContext context)
    {
        BinaryNode node = new BinaryNode<>(context.operator.getText());
        node.setLeft(this.visit(context.left));
        node.setRight(this.visit(context.right));
        
        return node;
    }
    
    /**
     * Find the location of an element from the context.
     *
     * @param context - Context of the element to locate.
     *
     * @return - String - Line and position description.
     */
    private String location(ParserRuleContext context)
    {
        String line = String.valueOf(context.start.getLine());
        String position = String.valueOf(context.start.getCharPositionInLine());
        return "line " + line + ":" + position;
    }
    
    /**
     * Check if a variable exist in any of the scopes for a specific register.
     *
     * @param register - Register to check.
     * @param variable - Variable to check for.
     *
     * @return - Whether or not the variable exist in the specific register.
     */
    private Boolean checkScopes(String register, String variable)
    {
        Boolean inCurrentScope = scope.get(currentScope).get(register).contains(variable);
        
        if (!inCurrentScope)
        {
            return scope.get("global").get(register).contains(variable);
        }
        else
        {
            return true;
        }
    }
}
