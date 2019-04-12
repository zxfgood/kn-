package com.feeye.page.grabFrame;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.feeye.init.SysData;

/**
 * @description: This is a class!
 * @author: chenjian
 * @date: 2019/02/26 20:57
 */
public class CheckHeaderCellRenderer extends DefaultCellEditor implements TableCellRenderer {
	TableModelProxy tableModel;
	JTableHeader tableHeader;
	final JCheckBox selectBox;

	public CheckHeaderCellRenderer(final JTable table) {
		super(new JCheckBox());
		this.setClickCountToStart(100);
		this.tableModel = (TableModelProxy) table.getModel();
		this.tableHeader = table.getTableHeader();
		selectBox = new JCheckBox(tableModel.getColumnName(1));
		selectBox.setSelected(false);
		tableHeader.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (new Date().getTime()-SysData.updateTime<100) {
					return;
				}
				if (e.getClickCount() > 0) {
					// 获得选中列
					int selectColumn = tableHeader.columnAtPoint(e.getPoint());
					if (selectColumn == 1) {
						SysData.updateTime = new Date().getTime();
						boolean value = !selectBox.isSelected();
						selectBox.setSelected(value);
						tableModel.selectAllOrNull(value);
//						tableHeader.repaint();
					}
				}
			}
		});
	}
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		String valueStr = (String) value;
		JLabel label = new JLabel(valueStr);
		label.setHorizontalAlignment(SwingConstants.CENTER); // 表头标签剧中
		selectBox.setHorizontalAlignment(SwingConstants.CENTER);// 表头标签剧中
		selectBox.setBorderPainted(true);
		JComponent component = (column == 1) ? selectBox : label;

		component.setForeground(tableHeader.getForeground());
		component.setBackground(tableHeader.getBackground());
		component.setFont(tableHeader.getFont());
		component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

		return component;
	}
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
												   int row, int column) {
		String valueStr = (String) value;
		JLabel label = new JLabel(valueStr);
		label.setHorizontalAlignment(SwingConstants.CENTER); // 表头标签剧中
		selectBox.setHorizontalAlignment(SwingConstants.CENTER);// 表头标签剧中
		selectBox.setBorderPainted(true);
		JComponent component = (column == 1) ? selectBox : label;

		component.setForeground(tableHeader.getForeground());
		component.setBackground(tableHeader.getBackground());
		component.setFont(tableHeader.getFont());
		component.setBorder(UIManager.getBorder("TableHeader.cellBorder"));

		return component;
	}

}
