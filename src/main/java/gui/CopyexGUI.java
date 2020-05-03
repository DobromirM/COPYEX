package gui;

import copyex.Copyex;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class CopyexGUI extends JFrame
{
    static JFrame frame;
    static JFrame treeFrame;
    static JTextArea inputArea;
    static JTextArea outputArea;
    static JTextArea treeArea;
    static Copyex copyex;
    
    // Window Size
    private final Integer WINDOW_HEIGHT = 600;
    private final Integer WINDOW_WIDTH = 1200;
    
    /**
     * Create Swing GUI for the application.
     */
    public CopyexGUI()
    {
        
        FlowLayout flowLayout = new FlowLayout();
        
        //Define JFrames
        frame = new JFrame("Copyex");
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);
        
        treeFrame = new JFrame("Abstract Syntax Tree");
        treeFrame.setLayout(new FlowLayout());
        treeFrame.setResizable(false);
        
        //Define Input area.
        inputArea = new JTextArea(30, 53);
        inputArea.setLineWrap(true);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        
        //Define output area.
        outputArea = new JTextArea(30, 53);
        outputArea.setLineWrap(true);
        outputArea.setEditable(false);
        PrintStream printStream = new PrintStream(new CopyexOutputStream(outputArea));
        System.setOut(printStream);
        System.setErr(printStream);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        
        //Define tree area.
        treeArea = new JTextArea(30, 53);
        treeArea.setLineWrap(true);
        treeArea.setEditable(false);
        JScrollPane treeScrollPane = new JScrollPane(treeArea);
        
        //Define Menu bar.
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item1 = new JMenuItem("Open");
        JMenuItem item2 = new JMenuItem("Save Output");
        item1.addActionListener(new FileActionListener());
        item2.addActionListener(new FileActionListener());
        menu.add(item1);
        menu.add(item2);
        bar.add(menu);
        
        //Define GUI buttons.
        JButton buttonRun = new JButton("Compile");
        JButton buttonTree = new JButton("Show Tree");
        JButton compileFile = new JButton("Compile File");
        
        buttonTree.addActionListener(new ExecutionActionListener());
        buttonRun.addActionListener(new ExecutionActionListener());
        compileFile.addActionListener(new ExecutionActionListener());
        
        //Add everything to the frame.
        frame.setLayout(flowLayout);
        copyex = new Copyex();
        frame.setJMenuBar(bar);
        frame.add(inputScrollPane);
        frame.add(outputScrollPane);
        frame.add(buttonRun);
        frame.add(buttonTree);
        frame.add(compileFile);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setVisible(true);
        
        treeFrame.add(treeScrollPane);
        treeFrame.setVisible(false);
        treeFrame.setSize(WINDOW_WIDTH / 2, WINDOW_HEIGHT);
        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}