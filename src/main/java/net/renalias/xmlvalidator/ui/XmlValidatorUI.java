package net.renalias.xmlvalidator.ui;

import net.renalias.file.FileHelper;
import net.renalias.xmlvalidator.core.ValidationError;
import net.renalias.xmlvalidator.core.XmlValidator;
import net.renalias.xmlvalidator.ui.components.*;
import net.renalias.xmlvalidator.ui.components.MenuBar;
import net.renalias.xmlvalidator.ui.components.SchemaFileChooser.SchemaFileChooserReturnValue;
import net.renalias.xmlvalidator.ui.support.CaretHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class XmlValidatorUI extends JFrame implements ElementDoubleClickedListener, MenuItemClickedListener {
	static private final String newline = "\n";
	JTabbedPane tabbedPane;
	protected ValidationDataTab validationDataTab;
	protected EditorTab[] editorTabs = {new EditorTab(), new SchemaTab()};
	private JPanel panel;
	private MenuBar menubar;
	JFileChooser fileChooser = new JFileChooser();
	SchemaFileChooser schemaFileChooser;

    protected static String WINDOW_TITLE = "Simple Editor for XML";

	public XmlValidatorUI() {
		super(WINDOW_TITLE);
		panel = new JPanel(new BorderLayout());
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Source", null, editorTabs[0], "Source XML file");
		tabbedPane.addTab("Schema", null, editorTabs[1], "Schema data");

        // shared file chooser, so that paths are remembers across invocations of the chooser
        editorTabs[0].setFileChooser(fileChooser);
        editorTabs[1].setFileChooser(fileChooser);

		validationDataTab = new ValidationDataTab();
		tabbedPane.addTab("Validation", null, validationDataTab, "Validation error list");
		panel.add(tabbedPane, BorderLayout.CENTER);
		add(panel);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menubar = new MenuBar();
		menubar.addMenuItemClickedListener(this);
		setJMenuBar(menubar);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent keyEvent) {
				if (keyEvent.getKeyCode() == KeyEvent.VK_F && keyEvent.getModifiers() == KeyEvent.CTRL_MASK) {
					System.out.println("Ctrl+f!");
					getCurrentTab().toogleSearchToolbar();
					return (true);
				}
				return (false);
			}
		});

		validationDataTab.addElementDoubleClickedListener(this);

		schemaFileChooser = new SchemaFileChooser(this);
	}

	protected EditorTab getCurrentTab() {
		return (editorTabs[tabbedPane.getSelectedIndex()]);
	}

	protected void validateXML() {
		// TODO: move this to a separate thread so that the UI is not blocked
		XmlValidator validator = new XmlValidator();
		boolean result = validator.validateContent(editorTabs[1].getText(), editorTabs[0].getText());
		handleXMLValidation(validator, result);
	}

	protected void validateXMLFromSchemaFile(String file) {
		// TODO: move this to a separate thread so that the UI is not blocked
		XmlValidator validator = new XmlValidator();
		boolean result = validator.validateSchemaFile(file, editorTabs[0].getText());
		handleXMLValidation(validator, result);
	}

	protected void handleXMLValidation(XmlValidator validator, boolean result) {
		if (!result) {
			// switch the tab to the validation error list tab
			validationDataTab.setValidationErrors(validator.getValidationErrors());
			tabbedPane.setSelectedIndex(2);
		} else {
			validationDataTab.clearValidationErrors();
			JOptionPane.showMessageDialog(null, "No validation errors were found", "Success", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void onElementDoubleClicked(ValidationError element) {
		// jump to the right place in the text field and activate the tab
		CaretHelper.goToLineColumm(editorTabs[0].getEditor(), element.getLine(), element.getColumn());
		tabbedPane.setSelectedIndex(0);
	}

	public void onMenuItemClicked(ActionEvent actionEvent) {
		JMenuItem source = (JMenuItem) actionEvent.getSource();
		System.out.println("Command:" + source.getName());
		if (source.getName().equals(MenuBar.MENUBAR_COMMAND_OPENXMLFILE)) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (FileHelper.isReadable(fileChooser.getSelectedFile().getAbsolutePath()) &&
						FileHelper.isFile(fileChooser.getSelectedFile().getAbsolutePath())) {
					try {
						editorTabs[0].setText(FileHelper.readFile(fileChooser.getSelectedFile().getAbsolutePath()));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "File cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_FIND)) {
			getCurrentTab().toogleSearchToolbar();
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_VALIDATE)) {
			if (editorTabs[1].getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "Please load or provide a schema in order to validate", "Error", JOptionPane.ERROR_MESSAGE);
			} else
				validateXML();
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_OPENSCHEMAFILE)) {
			int returnVal = fileChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (FileHelper.isReadable(fileChooser.getSelectedFile().getAbsolutePath()) &&
						FileHelper.isFile(fileChooser.getSelectedFile().getAbsolutePath())) {
					try {
						editorTabs[1].setText(FileHelper.readFile(fileChooser.getSelectedFile().getAbsolutePath()));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "File cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_EXIT)) {
			editorTabs[0].destroy();
            editorTabs[1].destroy();
            System.exit(0);
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMANT_WORDWRAP)) {
			editorTabs[0].toggleWordWrap();
			editorTabs[1].toggleWordWrap();
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_VALIDATE_EXTERNAL)) {
			SchemaFileChooserReturnValue returnValue = schemaFileChooser.showDialog();
			if (returnValue == SchemaFileChooserReturnValue.OK) {
				String schemaFile = schemaFileChooser.getSchemaFile();
				validateXMLFromSchemaFile(schemaFile);
			}
		}
        else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_SAVEXMLFILEAS)) {
             try {
                 editorTabs[0].saveContentsAs();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The file cannot be written", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
        else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_SAVEXMLFILE)) {
             try {
                 editorTabs[0].saveContents();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The file cannot be written", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
        else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_SAVESCHEMAFILE)) {
			try {
                editorTabs[1].saveContents();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The file cannot be written", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
        else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_SAVESCHEMAFILEAS)) {
			try {
                editorTabs[1].saveContentsAs();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The file cannot be written", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			System.err.println("Error: unrecognized command = " + source.getName());
		}
	}
}