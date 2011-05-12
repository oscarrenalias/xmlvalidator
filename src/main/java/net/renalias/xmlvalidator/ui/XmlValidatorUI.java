package net.renalias.xmlvalidator.ui;

import net.renalias.file.FileHelper;
import net.renalias.xmlvalidator.core.ValidationError;
import net.renalias.xmlvalidator.core.XmlValidator;
import net.renalias.xmlvalidator.ui.components.*;
import net.renalias.xmlvalidator.ui.components.MenuBar;
import net.renalias.xmlvalidator.ui.support.CaretHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class XmlValidatorUI extends JFrame implements ElementDoubleClickedListener, MenuItemClickedListener {
	static private final String newline = "\n";
	JTabbedPane tabbedPane;
	protected ValidationDataTab validationDataTab;
	protected EditorTab editorTab;
	protected SchemaTab schemaTab;
	private JPanel panel;
	private MenuBar menubar;
	JFileChooser fileChooser = new JFileChooser();
	JFileChooser schemaChooser = new JFileChooser();

	public XmlValidatorUI() {
		super("XML Validator");
		panel = new JPanel(new BorderLayout());
		// tabbed pane for validation info
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		editorTab = new EditorTab();
		tabbedPane.addTab("Source", null, editorTab, "Source XML file");

		schemaTab = new SchemaTab();
		tabbedPane.addTab("Schema", null, schemaTab, "Schema data");

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
					editorTab.toogleSearchToolbar();
					return (true);
				}
				return (false);
			}
		});

		validationDataTab.addElementDoubleClickedListener(this);
	}

	protected void validateXML() {
		// TODO: move this to a separate thread so that the UI is not blocked
		XmlValidator validator = new XmlValidator();
		boolean result = validator.validateContent(schemaTab.getText(), editorTab.getText());

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
		CaretHelper.goToLineColumm(editorTab.getMessageData(), element.getLine(), element.getColumn());
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
						editorTab.setText(FileHelper.readFile(fileChooser.getSelectedFile().getAbsolutePath()));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "File cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_FIND)) {
			if(tabbedPane.getSelectedIndex() == 0)
				editorTab.toogleSearchToolbar();
			else
				schemaTab.toogleSearchToolbar();
		} else if (source.getName().equals(MenuBar.MENUBAR_COMMAND_VALIDATE)) {
			if(schemaTab.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(null, "Please load or provide a schema in order to validate", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else
				validateXML();
		}
		else if(source.getName().equals(MenuBar.MENUBAR_COMMAND_OPENSCHEMAFILE)) {
			int returnVal = schemaChooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if (FileHelper.isReadable(schemaChooser.getSelectedFile().getAbsolutePath()) &&
						FileHelper.isFile(schemaChooser.getSelectedFile().getAbsolutePath())) {
					try {
						schemaTab.setText(FileHelper.readFile(schemaChooser.getSelectedFile().getAbsolutePath()));
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null, "File cannot be loaded", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		else if(source.getName().equals(MenuBar.MENUBAR_COMMAND_EXIT)) {
			System.exit(0);
		}
		else if(source.getName().equals(MenuBar.MENUBAR_COMMANT_WORDWRAP)) {
			editorTab.toggleWordWrap();
			schemaTab.toggleWordWrap();
		}
	}
}