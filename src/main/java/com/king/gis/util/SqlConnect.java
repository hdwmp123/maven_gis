package com.king.gis.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SqlConnect {
	// 驱动程序类
	private static String driver = "";
	// /连接数据库url
	private static String connectionUrl = "";
	// 用户名
	private static String user = "";
	// 用户密码
	private static String password = "";
	// 数据库连接对象
	private Connection connection = null;
	// 数据库对象
	private Statement statement = null;
	// 数据集对象
	private ResultSet resultSet = null;

	public SqlConnect() {
		this.createConnection();
	}

	static {
		Properties props = new Properties();
		// FileInputStream fis = null;
		InputStream in = null;
		try {
			// fis = new FileInputStream("D:/database.properties");
			// props.load(fis);
			in = SqlConnect.class.getResourceAsStream("/database.properties");
			props.load(in);
			driver = props.getProperty("database.driver");
			connectionUrl = props.getProperty("database.url");
			user = props.getProperty("database.username");
			password = props.getProperty("database.password");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// try {
			// fis.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Statement getStatement() {
		return this.statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}

	public ResultSet getResultSet() {
		return this.resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	/**
	 * 获取一个连接对象，默认连接对象本地数据库。
	 * 
	 * @return 连接是否成功
	 */
	public boolean createConnection() {
		boolean result = false;
		try {
			Class.forName(driver);
			this.connection = DriverManager.getConnection(connectionUrl, user,
					password);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 更新数据库
	 * 
	 * @param sql
	 *            更新的 sql语句
	 * @param printSql
	 *            是否打印SQL
	 * @return 更新是否成功
	 */
	public boolean execute(String sql, boolean printSql) {
		boolean b = false;
		try {
			if (printSql) {
				System.out.println(sql);
			}
			this.statement = this.connection.createStatement();
			this.statement.execute(sql);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 更新数据库
	 * 
	 * @param sql
	 *            更新的 sql语句
	 * @param printSql
	 *            是否打印SQL
	 * @return 更新是否成功
	 */
	public boolean update(String sql, boolean printSql) {
		boolean b = false;
		try {
			if (printSql) {
				System.out.println(sql);
			}
			this.statement = this.connection.createStatement();
			this.statement.executeUpdate(sql);
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 执行查询，将查询的结果集给resultmentSet。
	 * 
	 * @param sql
	 *            查询的 sql语句
	 * @param printSql
	 *            是否打印SQL
	 */
	public void query(String sql, boolean printSql) {
		try {
			if (printSql) {
				System.out.println(sql);
			}
			this.statement = this.connection.createStatement();
			this.resultSet = this.statement.executeQuery(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测结果集是否为空
	 * 
	 * @return true为存在记录
	 */
	public boolean next() {
		boolean b = false;
		try {
			if (this.resultSet.next()) {
				b = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 获得结果集中当前行columnLabel的记录
	 * 
	 * @param colKey
	 * @return 值记录
	 */
	public String getValue(String colKey) {
		String value = null;
		try {
			if (this.resultSet != null) {
				value = this.resultSet.getString(colKey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 返回结果集的个数
	 * **/
	public int getNum(String sql) {
		int i = 0;
		try {
			this.statement = this.connection.createStatement();
			this.resultSet = this.statement.executeQuery(sql);
			this.resultSet.last();
			i = this.resultSet.getRow();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 返回结果集的个数
	 * **/
	public int getNum(String sql, String countKey) {
		int i = 0;
		try {
			this.statement = this.connection.createStatement();
			this.resultSet = this.statement.executeQuery(sql);
			this.resultSet.next();
			i = this.resultSet.getInt(countKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * 通过列名查询整型数据
	 * 
	 * **/
	public int getIntValueByString(String colKey) {
		int value = 0;
		String temp;
		try {
			if (this.resultSet != null) {
				temp = this.resultSet.getString(colKey);
				value = Integer.parseInt(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 通过列名查询Float数据
	 * 
	 * **/
	public float getFloatValueByString(String colKey) {
		float value = 0;
		String temp;
		try {
			if (this.resultSet != null) {
				temp = this.resultSet.getString(colKey);
				value = Float.parseFloat(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	public int getIntValue(int colIndex) {
		int value = 0;
		try {
			if (this.resultSet != null) {
				value = this.resultSet.getInt(colIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	// 删除
	public void delete(String sql) {
		try {
			this.statement = this.connection.createStatement();
			this.statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 添加数据
	public void add(String sql) {
		try {
			this.statement = this.connection.createStatement();
			this.statement.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭连接对象
	 */
	public void closeConnection() {
		try {
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库对象
	 */
	public void closeStatement() {
		try {
			if (this.statement != null) {
				this.statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭结果集
	 */
	public void closeResultSet() {
		try {
			if (this.resultSet != null) {
				this.resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据连接对象，数据库对象和数据结果集对象。
	 */
	public void closeAll() {
		closeResultSet();
		closeStatement();
		closeConnection();
	}

	/**
	 * 测试该类函数。
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SqlConnect db = new SqlConnect();
		if (db.createConnection()) {
			String check = "select 1 count_ from dual";
			db.query(check, true);
			while (db.next()) {
				System.out.println(db.getValue("count_"));
			}
			System.out.println("测试数据库连接成功.");
			db.closeAll();
		}
	}
}