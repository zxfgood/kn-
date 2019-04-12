package com.feeye.page.grabFrame;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.feeye.init.SysData;

/**
 * @description: This is a class!
 * @author: chenjian
 * @date: 2019/02/19 11:03
 */
public class CheckBoxEditor extends DefaultCellEditor implements TableCellRenderer {
	private JCheckBox jCheckBox;
	public CheckBoxEditor() {
		super( new JCheckBox());
		this.setClickCountToStart(100);
		this.jCheckBox = new JCheckBox();
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		jCheckBox.setSelected(Boolean.TRUE.equals(value));
		jCheckBox.setSelected(Boolean.TRUE.equals(value));
		return jCheckBox;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		jCheckBox.setSelected(Boolean.TRUE.equals(value));
		return jCheckBox;
	}
}