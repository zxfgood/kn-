package com.feeye.page.grabFrame;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/25 17:19
 */
public class PaxInfoEditor implements TableCellRenderer{

	private JPanel panel;

	private JTextArea area;

	public PaxInfoEditor() {
		this.panel = new JPanel(null);
		initArea();
		panel.add(area);
	}

	private void initArea() {
		this.area = new JTextArea();
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (value!=null) {
			int length = value.toString().split("&").length;
			table.setRowHeight(row, table.getRowHeight()*length);
		} else {
			area.setText("");
		}
		if (isSelected||row==table.getSelectedRow()) {
			area.setBackground(table.getSelectionBackground());
			area.setForeground(table.getSelectionForeground());
		} else {
			area.setBackground(table.getBackground());
			area.setForeground(table.getForeground());
		}
		area.setText(value==null?"":value.toString().replace("&","\n"));
		panel.add(area);
		return area;
	}

}
