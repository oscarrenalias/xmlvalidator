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
	public static String MENUBAR_COMMAND_OPENXMLFILE = "Open XML file";
	public static String MENUBAR_COMMAND_OPENSCHEMAFILE = "Open schema file";
	public static String MENUBAR_COMMAND_SAVEXMLFILE = "Save XML file";
	public static String MENUBAR_COMMAND_FIND = "Find";
	public static String MENUBAR_COMMAND_VALIDATE = "Validate";
	public static String MENUBAR_COMMAND_EXIT = "Exit";
	public static String MENUBAR_COMMANT_WORDWRAP = "Wordwrap";

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

	}

	public void actionPerformed(ActionEvent actionEvent) {
		for(MenuItemClickedListener listener : listeners)
			listener.onMenuItemClicked(actionEvent);
	}

	public void addMenuItemClickedListener(MenuItemClickedListener listener) {
		listeners.add(listener);
	}
}
