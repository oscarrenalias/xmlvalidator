package net.renalias.xmlvalidator.ui.components;

import net.renalias.file.FileHelper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This class acts like a file save dialog, except that it shows a warning if the file is going to be overwritten,
 * asking users for confirmation.
 *
 * Returns a File object representing the file that was selected, or null if no file was selected or the dialog was cancelled
 */
public class CustomFileSaveDialog {
	public static File showSaveAsDialog(Component parent, JFileChooser fileChooser, String content) {
		boolean written = false;
		boolean cancelled = false;
		File returnValue = null;

		while (!written && !cancelled) {
			int returnVal = fileChooser.showSaveDialog(parent);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				String absoluteFileName = fileChooser.getSelectedFile().getAbsolutePath();
				String fileName = fileChooser.getSelectedFile().getName();
				boolean write = true;
				if (FileHelper.exists(absoluteFileName)) {
					// overwrite?
					int overwrite = JOptionPane.showConfirmDialog(null, "File " + fileName + " already exists. Overwrite?", "Overwrite", JOptionPane.YES_NO_OPTION);
					if (overwrite == JOptionPane.YES_OPTION)
						write = true;
					else
						write = false;
				}

				if (write) {
					System.out.print("Writing file as: " + fileChooser.getSelectedFile().getAbsolutePath());
					returnValue = fileChooser.getSelectedFile();
                    written = true;
				}
			} else {
				cancelled = true;
			}
		}

		return (returnValue);
	}
}