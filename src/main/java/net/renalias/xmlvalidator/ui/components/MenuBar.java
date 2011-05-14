package net.renalias.xmlvalidator.ui.components;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

public class MenuBar extends JMenuBar implements ActionListener {

	List<MenuItemClickedListener> listeners;

	// File menu
	public static final String MENUBAR_COMMAND_OPENXMLFILE = "Open XML file";
	public static final String MENUBAR_COMMAND_OPENSCHEMAFILE = "Open schema file";
	public static final String MENUBAR_COMMAND_SAVEXMLFILE = "Save XML file";
	public static final String MENUBAR_COMMAND_FIND = "Find";
	public static final String MENUBAR_COMMAND_VALIDATE = "Validate";
	public static final String MENUBAR_COMMAND_EXIT = "Exit";
	public static final String MENUBAR_COMMANT_WORDWRAP = "Wordwrap";
	public static final String MENUBAR_COMMAND_VALIDATE_EXTERNAL = "Validate with external file";
	public static final String MENUBAR_COMMAND_SAVESCHEMAFILE = "Save schema file";
	public static final String MENUBAR_COMMAND_SAVEXMLFILEAS = "Save XML file as...";
	public static final String MENUBAR_COMMAND_SAVESCHEMAFILEAS = "Save schema file as...";
	public static final String MENUBAR_COMMAND_UNDO = "Undo";
	public static final String MENUBAR_COMMAND_REDO = "Redo";
	public static final String MENUBAR_COMMAND_CUT = "Cut";
	public static final String MENUBAR_COMMAND_COPY = "Copy";
	public static final String MENUBAR_COMMAND_PASTE = "Paste";
	public static final String MENUBAR_COMMAND_DELETE = "Delete";

	public MenuBar() {
		super();
		build();

		listeners = new LinkedList<MenuItemClickedListener>();
	}

	protected void build() {
		//Build the file menu
		JMenu file = new JMenu("File");
		add(file);

		// Open XML file
		JMenuItem openXMLfile = new JMenuItem("Open XML file", KeyEvent.VK_O);
		openXMLfile.setName(MENUBAR_COMMAND_OPENXMLFILE);
		openXMLfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		openXMLfile.getAccessibleContext().setAccessibleDescription("Open XML file");
		openXMLfile.addActionListener(this);
		file.add(openXMLfile);

		// Open schema file
		JMenuItem openSchemaFile = new JMenuItem("Open schema", KeyEvent.VK_T);
		openSchemaFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		openSchemaFile.setName(MENUBAR_COMMAND_OPENSCHEMAFILE);
		openSchemaFile.getAccessibleContext().setAccessibleDescription("Open schema file");
		openSchemaFile.addActionListener(this);
		file.add(openSchemaFile);

		file.addSeparator();

		// save XML file
		JMenuItem saveXMLfile = new JMenuItem("Save XML", KeyEvent.VK_S);
		saveXMLfile.setName(MENUBAR_COMMAND_SAVEXMLFILE);
		saveXMLfile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		saveXMLfile.getAccessibleContext().setAccessibleDescription("Save XML file");
		saveXMLfile.addActionListener(this);
		file.add(saveXMLfile);

		// save XML as...
		JMenuItem saveXMLFileAs = new JMenuItem("Save XML as...");
		saveXMLFileAs.setName(MENUBAR_COMMAND_SAVEXMLFILEAS);
		saveXMLFileAs.getAccessibleContext().setAccessibleDescription("Save XML file as");
		saveXMLFileAs.addActionListener(this);
		file.add(saveXMLFileAs);

		// save schema file
		JMenuItem saveSchemaFile = new JMenuItem("Save schema", KeyEvent.VK_C);
		saveSchemaFile.setName(MENUBAR_COMMAND_SAVESCHEMAFILE);
		saveSchemaFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
		saveSchemaFile.getAccessibleContext().setAccessibleDescription("Save schema file");
		saveSchemaFile.addActionListener(this);
		file.add(saveSchemaFile);

		// save schema file as...
		JMenuItem saveSchemaFileAs = new JMenuItem("Save schema as...");
		saveSchemaFileAs.setName(MENUBAR_COMMAND_SAVESCHEMAFILEAS);
		saveSchemaFileAs.getAccessibleContext().setAccessibleDescription("Save schema file as");
		saveSchemaFileAs.addActionListener(this);
		file.add(saveSchemaFileAs);


		file.addSeparator();

		// exit
		JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_T);
		exit.addActionListener(this);
		exit.setName(MENUBAR_COMMAND_EXIT);
		exit.getAccessibleContext().setAccessibleDescription("Exit application");
		exit.addActionListener(this);
		file.add(exit);

		// Edit menu
		JMenu edit = new JMenu("Edit");
		add(edit);

		// Undo
		JMenuItem undo = new JMenuItem("Undo", KeyEvent.VK_U);
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
		undo.getAccessibleContext().setAccessibleDescription("Undo");
		undo.setName(MENUBAR_COMMAND_UNDO);
		undo.addActionListener(this);
		edit.add(undo);

		// Redo
		JMenuItem redo = new JMenuItem("Redo", KeyEvent.VK_R);
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		redo.getAccessibleContext().setAccessibleDescription("Redo");
		redo.setName(MENUBAR_COMMAND_REDO);
		redo.addActionListener(this);
		edit.add(redo);

		edit.addSeparator();

		// cut
		JMenuItem cut = new JMenuItem("Cut");
		cut.setName(MENUBAR_COMMAND_CUT);
		cut.addActionListener(this);
		edit.add(cut);

		// copy
		JMenuItem copy = new JMenuItem("Copy");
		copy.setName(MENUBAR_COMMAND_COPY);
		copy.addActionListener(this);
		edit.add(copy);

		// paste
		JMenuItem paste = new JMenuItem("Paste");
		paste.setName(MENUBAR_COMMAND_PASTE);
		paste.addActionListener(this);
		edit.add(paste);

		// paste
		JMenuItem delete = new JMenuItem("Delete");
		delete.setName(MENUBAR_COMMAND_DELETE);
		delete.addActionListener(this);
		edit.add(delete);

		edit.addSeparator();

		// Find
		JMenuItem find = new JMenuItem("Find...", KeyEvent.VK_F);
		find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));
		find.getAccessibleContext().setAccessibleDescription("Find");
		find.setName(MENUBAR_COMMAND_FIND);
		find.addActionListener(this);
		edit.add(find);

		// go to line
		JMenuItem gotoLine = new JMenuItem("Go to line...", KeyEvent.VK_G);
		gotoLine.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
		gotoLine.getAccessibleContext().setAccessibleDescription("Go to line");
		gotoLine.addActionListener(this);
		edit.add(gotoLine);

		edit.addSeparator();

		// toggle word wrap
		JCheckBoxMenuItem cbToggleWordWrap = new JCheckBoxMenuItem("Toggle word wrap");
		cbToggleWordWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.ALT_MASK));
		cbToggleWordWrap.addActionListener(this);
		cbToggleWordWrap.setMnemonic(KeyEvent.VK_W);
		cbToggleWordWrap.setName(MENUBAR_COMMANT_WORDWRAP);
		edit.add(cbToggleWordWrap);

		// XML
		// Edit menu
		JMenu xml = new JMenu("XML");
		add(xml);

		// validate
		JMenuItem validate = new JMenuItem("Validate", KeyEvent.VK_V);
		validate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
		validate.getAccessibleContext().setAccessibleDescription("Validate");
		validate.addActionListener(this);
		validate.setName(MENUBAR_COMMAND_VALIDATE);
		xml.add(validate);

		// validate with external file
		JMenuItem validateFile = new JMenuItem("Validate with external file...", KeyEvent.VK_X);
		validateFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
		validateFile.getAccessibleContext().setAccessibleDescription("Validate with external file");
		validateFile.addActionListener(this);
		validateFile.setName(MENUBAR_COMMAND_VALIDATE_EXTERNAL);
		xml.add(validateFile);
	}

	public void actionPerformed(ActionEvent actionEvent) {
		for(MenuItemClickedListener listener : listeners)
			listener.onMenuItemClicked(actionEvent);
	}

	public void addMenuItemClickedListener(MenuItemClickedListener listener) {
		listeners.add(listener);
	}
}
