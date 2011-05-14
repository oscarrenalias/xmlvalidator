package net.renalias.xmlvalidator.ui.components;

import net.renalias.file.FileHelper;
import net.renalias.xmlvalidator.support.Assert;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EditorTab extends JPanel {

    RSyntaxTextArea messageData;
    SearchToolBar searchToolbar;
    File file;
    // whether the editor has unsaved content
    boolean saved = false;
    JFileChooser fileChooser;

    public EditorTab() {
        super(new BorderLayout());
        add(buildSourceTab());
        searchToolbar = new SearchToolBar(messageData);
        searchToolbar.setVisible(false);
        add(searchToolbar, BorderLayout.PAGE_START);
    }

    private Component buildSourceTab() {
        messageData = new XMLTextArea(25, 90);
        RTextScrollPane messageDataScrollPane = new RTextScrollPane(messageData);
        messageDataScrollPane.setLineNumbersEnabled(true);
        return (messageDataScrollPane);
    }

    public String getText() {
        return (messageData.getText());
    }

    public void setText(String text) {
        messageData.setText(text);
    }

    public RSyntaxTextArea getEditor() {
        return (messageData);
    }

    public void toogleSearchToolbar() {
        searchToolbar.setVisible(!searchToolbar.isVisible());
        messageData.clearMarkAllHighlights();
    }

    public void toggleWordWrap() {
        messageData.setLineWrap(!messageData.getLineWrap());
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void saveContents() throws IOException {
        if(getFile() == null) {
            // no file has been selected so far, show the selection dialog
            saveContentsAs();
            saved = true;
        }
        else {
            // save the current file
            FileHelper.write(file.getAbsolutePath(), getText());
        }
    }

    public void saveContentsAs() throws IOException {
        File file = CustomFileSaveDialog.showSaveAsDialog(this, getFileChooser(), getText());
        if(file != null) {
            FileHelper.write(file.getAbsolutePath(), getText());
            setFile(file);
            saved = true;
        }
    }

    public boolean hasUnsavedContent() {
        return(!saved);
    }

    public JFileChooser getFileChooser() {
        Assert.notNull(fileChooser);
        return fileChooser;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    /**
     * Provides the dialog with an opportunity to offer the user to save the file, usually used
     * before shutting down the application
     */
    public void destroy() {
        if(hasUnsavedContent()) {
            int doSave = JOptionPane.showConfirmDialog(
                    null,
                    "File " + getFile().getName() + " has unsaved content. Would you like to save?",
                    "Success", JOptionPane.YES_NO_OPTION
            );
            if(doSave == JOptionPane.YES_OPTION) {
                // user wants to save
                try {
                    saveContents();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "File cannot be saved", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }
}