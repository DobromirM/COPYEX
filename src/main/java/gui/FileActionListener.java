package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class FileActionListener implements ActionListener
{
    /**
     * Define actions for the menu bar buttons.
     * <p>
     * Open - Creates a dialog window for selecting a file. The selected file is then loaded into the input area.
     * Save Output - Creates a dialog window for specifying a file. The output area is then saved to that file.
     *
     * @param event - Action to be performed.
     */
    @Override
    public void actionPerformed(ActionEvent event)
    {
        String action = event.getActionCommand();
        
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        int response = fileChooser.showOpenDialog(null);
        
        if (response == JFileChooser.APPROVE_OPTION)
        {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            
            if (action.equals("Open"))
            {
                readFile(file);
            }
            
            if (action.equals("Save Output"))
            {
                try
                {
                    FileWriter fileWriter;
                    fileWriter = new FileWriter(file, false);
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
     * Read a file from path and populate the editor with the code.
     *
     * @param file - File with code.
     */
    static void readFile(File file)
    {
        try
        {
            String content;
            
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder(bufferedReader.readLine());
            
            while ((content = bufferedReader.readLine()) != null)
            {
                stringBuilder.append("\n").append(content);
            }
            
            CopyexGUI.inputArea.setText(stringBuilder.toString());
        }
        catch (Exception evt)
        {
            JOptionPane.showMessageDialog(CopyexGUI.frame, evt.getMessage());
        }
    }
}
