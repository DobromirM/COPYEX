package gui;

import AbstractTree.AbstractSyntaxTree;
import copyex.Copyex;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static gui.FileActionListener.readFile;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class ExecutionActionListener implements ActionListener
{
    /**
     * Define action for GUI button.
     * <p>
     * Compile      - Compile the code from the input area.
     * Show Tree    - Show / Hide the abstract syntax tree in a new frame.
     * Compile File - Compile a .copyex file into .py file.
     *
     * @param event - Action to be performed.
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
        String action = event.getActionCommand();
        
        if (action.equals("Compile"))
        {
            compile();
        }
        if (action.equals("Show Tree"))
        {
            CopyexGUI.treeFrame.setVisible(!CopyexGUI.treeFrame.isVisible());
        }
        if (action.equals("Compile File"))
        {
            JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
            int response = fileChooser.showOpenDialog(null);
            
            if (response == JFileChooser.APPROVE_OPTION)
            {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                
                readFile(file);
                
                compile();
                
                try
                {
                    String path = file.getParent();
                    String name = file.getName();
                    name = getFileName(name) + ".py";
                    Path outputPath = Paths.get(path, name);
                    
                    File output = new File(outputPath.toString());
                    FileWriter fileWriter = new FileWriter(output, false);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    
                    bufferedWriter.write(CopyexGUI.outputArea.getText());
                    bufferedWriter.flush();
                    bufferedWriter.close();
                }
                catch (Exception evt)
                {
                    JOptionPane.showMessageDialog(CopyexGUI.frame, evt.getMessage());
                }
            }
        }
    }
    
    /**
     * Get the name of a file without the extension.
     *
     * @param name - Name of a file with extension.
     *
     * @return - Name of a file without extension.
     */
    private String getFileName(String name)
    {
        if (name.indexOf(".") > 0)
        {
            return name.substring(0, name.lastIndexOf("."));
        }
        else
        {
            return name;
        }
    }
    
    /**
     * Compile the abstract syntax tree and populate the GUI text areas.
     */
    private void compile()
    {
        CopyexGUI.outputArea.setText("");
        CopyexGUI.treeArea.setText("");
        String code = CopyexGUI.inputArea.getText();
        AbstractSyntaxTree tree = CopyexGUI.copyex.parse(code);
        
        if (tree != null)
        {
            String compiledCode = CopyexGUI.copyex.compile();
            
            if (compiledCode != null)
            {
                CopyexGUI.outputArea.setText(compiledCode);
                CopyexGUI.treeArea.setText(tree.toString());
            }
        }
    }
}
