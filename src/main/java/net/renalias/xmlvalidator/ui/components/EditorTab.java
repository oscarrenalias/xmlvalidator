package net.renalias.xmlvalidator.ui.components;

import net.renalias.file.FileHelper;
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

    public void saveContents(JFileChooser fileChooser) throws IOException {
        if(getFile() == null) {
            // no file has been selected so far, show the selection dialog
            saveContentsAs(fileChooser);
        }
        else {
            // save the current file
            FileHelper.write(file.getAbsolutePath(), getText());
        }
    }

    public void saveContentsAs(JFileChooser fileChooser) throws IOException {
        File file = CustomFileSaveDialog.showSaveAsDialog(this, fileChooser, getText());
        if(file != null) {
            FileHelper.write(file.getAbsolutePath(), getText());
            setFile(file);
        }
    }

    public void saveContents() throws IOException {
        saveContents(new JFileChooser());
    }
}