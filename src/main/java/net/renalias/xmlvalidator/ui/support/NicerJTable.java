package net.renalias.xmlvalidator.ui.support;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Implements some nicer defaults for our own JTable
 */
public class NicerJTable extends JTable {
	public NicerJTable(AbstractTableModel tableModel) {
		super(tableModel);
		init();
	}

	public NicerJTable() {
		super();
		init();
	}

	protected void init() {
		// some useful defaults for us
		setGridColor(Color.LIGHT_GRAY);
		setCellSelectionEnabled(true);
	}

	public Component prepareRenderer(TableCellRenderer renderer, int rowIndex, int vColIndex) {
		Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
		if (rowIndex % 2 == 0 && !isCellSelected(rowIndex, vColIndex)) {
			c.setBackground(new Color(242, 242, 242));
		} else if (rowIndex % 2 != 0 && !isCellSelected(rowIndex, vColIndex)) {
			// If not shaded, match the table's background
			c.setBackground(getBackground());
		}

		return c;
	}
}
