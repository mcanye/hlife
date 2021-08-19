package hlife.utils;

import org.testng.Reporter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 * @author Mchi
 *
 */
public class MysqlUtil {
	Connection con;
	//驱动程序名
	        String driver = "com.mysql.jdbc.Driver";
	       //URL指向要访问的数据库
	         String url = PropertiesUtils.getConfigValue("mysqlurl");
	        //MySQL配置时的用户名
	          String user = PropertiesUtils.getConfigValue("mysqlusername");
	         //MySQL配置时的密码
	          String password = PropertiesUtils.getConfigValue("mysqlpassword");

	/**
	 * 获取连接
	 * @return
	 */
	public  Connection getConnection(){
	    try {
	        Class.forName(driver);
	        Reporter.log("数据库驱动加载成功");
	           System.out.println("数据库驱动加载成功");

	    } catch (ClassNotFoundException e) {
	    	 Reporter.log(e.getMessage());
	    	 e.printStackTrace();
	    }
	    try {
	    	con = DriverManager.getConnection(url,user,password);
	    	Reporter.log("数据库连接成功");
	        System.out.println("数据库连接成功");
	    } catch (SQLException e) {
	    	 Reporter.log(e.getMessage());
	        e.printStackTrace();
	    }

	    return con;
	}

	//关闭连接
	public void closeConnection(Connection conn) {
		if(conn != null) {
			try {
				if(con != null) {
					con.close();
				}
				conn.close();
			} catch (SQLException e) {
				Reporter.log(e.getMessage());
		    	 e.printStackTrace();
			}
		}
	}

}
