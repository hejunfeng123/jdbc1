package Jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class connectionMysql {

    public static void main(String[] args) {
        connection();
    }
    
    /**
     * 
     * @Title connection 
     * @Description (连接数据库) 
     * @return
     * @Return Connection 返回类型 
     * @Throws 
     * @Date  2018年2月6日
     * @修改历史  
     *     1. [2018年2月6日]创建文件 by 何俊峰
     */
    public static Connection connection() {
        // 声明Connection对象
        Connection con = null;
        // 驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        // URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://localhost:3306/acid?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
        // MySQL配置时的用户名
        String user = "root";
        // MySQL配置时的密码
        String password = "root";
        // 遍历查询结果集
        try {
            // 加载驱动程序
            Class.forName(driver);
            // 1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url, user, password);
            if (!con.isClosed()) 
                System.out.println("Succeeded connecting to the Database!");
            // 2.创建statement类对象，用来执行SQL语句！！
           /* Statement statement = con.createStatement();
            // 要执行的SQL语句
            String sql = "select * from account";
            // 3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            System.out.println("-----------------");
            System.out.println("执行结果如下所示:");
            System.out.println("-----------------");
            System.out.println("姓名" + "\t" + "职称");
            System.out.println("-----------------");

            String job = null;
            String id = null;
            while (rs.next()) {
                // 获取stuname这列数据
                job = rs.getString("name");
                // 获取stuid这列数据
                id = rs.getString("money");

                // 输出结果
                System.out.println(id + "\t" + job);
            }
            rs.close();
            con.close();*/
            //return con ;
        } catch (ClassNotFoundException e) {
            // 数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            // 数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            System.out.println("数据库数据成功获取！！");
        }
        return con;
    }
    
    /**
     * 
     * @Title release 
     * @Description (关闭连接) 
     * @param con
     * @param st
     * @param rs
     * @Return void 返回类型 
     * @Throws 
     * @Date  2018年2月6日
     * @修改历史  
     *     1. [2018年2月6日]创建文件 by 何俊峰
     */
    public static void release(Connection con , PreparedStatement st , ResultSet rs ){
        try {
            if(st != null ){
                st.close();
            }
            if(rs != null ){
                rs.close();
            }
            if(con != null ){
                con.close();
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}
