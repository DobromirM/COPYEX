package gui;

import javax.swing.*;
import java.io.OutputStream;

/**
 * @author DMarinov
 * Created on: 01/Mar/2019
 */
public class CopyexOutputStream extends OutputStream
{
    private JTextArea textArea;
    
    /**
     * Create custom output stream that write to output area.
     *
     * @param textArea - Output area to write to.
     */
    CopyexOutputStream(JTextArea textArea)
    {
        this.textArea = textArea;
    }
    
    /**
     * Write the character to the output area.
     *
     * @param character - Character int value
     */
    @Override
    public void write(int character)
    {
        textArea.setText(textArea.getText() + String.valueOf((char) character));
    }
}
