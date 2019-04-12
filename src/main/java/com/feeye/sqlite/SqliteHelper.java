package com.feeye.sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @description: This is a class!
 * @author: chenjian
 * @date: 2019/02/23 09:26
 */
public class SqliteHelper {
	final static Logger logger = Logger.getLogger(SqliteHelper.class);

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private String dbFilePath;

	/**
	 * 构造函数
	 * @param dbFilePath sqlite db 文件路径
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public SqliteHelper(String dbFilePath) throws ClassNotFoundException, SQLException {
		this.dbFilePath = dbFilePath;
		connection = getConnection(dbFilePath);
	}

	/**
	 * 获取数据库连接
	 * @param dbFilePath db文件路径
	 * @return 数据库连接
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Connection getConnection(String dbFilePath) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
		return conn;
	}

	/**
	 * 执行select查询，返回结果列表
	 *
	 * @param sql sql select 语句
	 * @param rm 结果集的行数据处理类对象
	 * @return
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public <T> List<T> executeQuery(String sql, ResultSetHandler<T> rm) {
		List<T> rsList = new ArrayList<T>();
		try {
			resultSet = getStatement().executeQuery(sql);
			while (resultSet.next()) {
				rsList.add(rm.rowToObj(resultSet));
			}
		} catch (Exception e) {
			logger.error("error", e);
			return null;
		} finally {
			destroyed();
		}
		return rsList;
	}

	/**
	 * 执行数据库更新sql语句
	 * @param sql
	 * @return 更新行数
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int executeUpdate(String sql) {
		try {
			return getStatement().executeUpdate(sql);
		} catch (Exception e) {
			logger.error("error", e);
		} finally {
			destroyed();
		}
		return 400;
	}

	/**
	 * 执行多个sql更新语句
	 * @param sqls
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void executeUpdate(String...sqls) throws SQLException, ClassNotFoundException {
		try {
			for (String sql : sqls) {
				getStatement().executeUpdate(sql);
			}
		} finally {
			destroyed();
		}
	}

	/**
	 * 执行数据库更新 sql List
	 * @param sqls sql列表
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void executeUpdate(List<String> sqls) throws SQLException, ClassNotFoundException {
		try {
			for (String sql : sqls) {
				getStatement().executeUpdate(sql);
			}
		} finally {
			destroyed();
		}
	}

	private Connection getConnection() throws ClassNotFoundException, SQLException {
		if (null == connection) connection = getConnection(dbFilePath);
		return connection;
	}

	private Statement getStatement() throws SQLException, ClassNotFoundException {
		if (null == statement) statement = getConnection().createStatement();
		return statement;
	}

	/**
	 * 数据库资源关闭和释放
	 */
	public void destroyed() {
		try {
			if (null != connection) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			logger.error("Sqlite数据库关闭时异常", e);
		}
	}
}
