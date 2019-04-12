package com.feeye.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @description: This is a class!
 * @author: chenjian
 * @date: 2019/02/23 09:31
 */

public interface ResultSetHandler<T> {
	T rowToObj(ResultSet rs) throws SQLException;
}
