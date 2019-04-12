package com.feeye.util;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/22 09:19
 */
public class PageUtil {
	public static DefaultTableCellRenderer getTableRender() {
		DefaultTableCellRenderer cr = new DefaultTableCellRenderer();
		cr.setHorizontalAlignment(JLabel.CENTER);
		return cr;
	}
}
