package Jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

public class TransactionDemo1 {
    public static void main(String[] args) {
        // testTransaction1();

        // testTransaction2();

        testTransaction3();
    }

    /**
     * @Method: testTransaction1
     * @Description: 模拟转账成功时的业务场景
     * @Anthor:何俊峰
     */
    public static void testTransaction1() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = connectionMysql.connection();
            conn.setAutoCommit(false);// 通知数据库开启事务(start transaction)
            String sql1 = "update account set money=money-100 where name='A'";
            st = conn.prepareStatement(sql1);
            st.executeUpdate();
            String sql2 = "update account set money=money+100 where name='B'";
            st = conn.prepareStatement(sql2);
            st.executeUpdate();
            conn.commit();// 上面的两条SQL执行Update语句成功之后就通知数据库提交事务(commit)
            System.out.println("成功！！！"); // log4j
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionMysql.release(conn, st, rs);
        }
    }

    /**
     * @Method: testTransaction1
     * @Description: 模拟转账过程中出现异常导致有一部分SQL执行失败后让数据库自动回滚事务
     * @Anthor:孤傲苍狼
     */
    public static void testTransaction2() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = connectionMysql.connection();
            conn.setAutoCommit(false);// 通知数据库开启事务(start transaction)
            String sql1 = "update account set money=money-100 where name='A'";
            st = conn.prepareStatement(sql1);
            st.executeUpdate();
            // 用这句代码模拟执行完SQL1之后程序出现了异常而导致后面的SQL无法正常执行，事务也无法正常提交，此时数据库会自动执行回滚操作
            int x = 1 / 0;
            String sql2 = "update account set money=money+100 where name='B'";
            st = conn.prepareStatement(sql2);
            st.executeUpdate();
            conn.commit();// 上面的两条SQL执行Update语句成功之后就通知数据库提交事务(commit)
            System.out.println("成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connectionMysql.release(conn, st, rs);
        }
    }

    /**
     * @Method: testTransaction1
     * @Description: 模拟转账过程中出现异常导致有一部分SQL执行失败时手动通知数据库回滚事务
     * @Anthor:孤傲苍狼
     */
    public static void testTransaction3() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = connectionMysql.connection();
            conn.setAutoCommit(false);// 通知数据库开启事务(start transaction)
            String sql1 = "update account set money=money-100 where name='A'";
            st = conn.prepareStatement(sql1);
            st.executeUpdate();
            // 用这句代码模拟执行完SQL1之后程序出现了异常而导致后面的SQL无法正常执行，事务也无法正常提交
            int x = 1 / 0;
            String sql2 = "update account set money=money+100 where name='B'";
            st = conn.prepareStatement(sql2);
            st.executeUpdate();
            conn.commit();// 上面的两条SQL执行Update语句成功之后就通知数据库提交事务(commit)
            System.out.println("成功！！！");
        } catch (Exception e) {
            try {
                // 捕获到异常之后手动通知数据库执行回滚事务的操作
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            connectionMysql.release(conn, st, rs);
        }
    }

    /**
     * @Method: testTransaction1
     * @Description: 模拟转账成功时的业务场景
     * @Anthor:孤傲苍狼
     */
    public void testTransaction4() {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        Savepoint sp = null;

        try {
            conn = connectionMysql.connection();
            conn.setAutoCommit(false);// 通知数据库开启事务(start transaction)

            String sql1 = "update account set money=money-100 where name='A'";
            st = conn.prepareStatement(sql1);
            st.executeUpdate();

            // 设置事务回滚点
            sp = conn.setSavepoint();

            String sql2 = "update account set money=money+100 where name='B'";
            st = conn.prepareStatement(sql2);
            st.executeUpdate();

            // 程序执行到这里出现异常，后面的sql3语句执行将会中断
            int x = 1 / 0;

            String sql3 = "update account set money=money+100 where name='C'";
            st = conn.prepareStatement(sql3);
            st.executeUpdate();

            conn.commit();

        } catch (Exception e) {
            try {
                /**
                 * 我们在上面向数据库发送了3条update语句， sql3语句由于程序出现异常导致无法正常执行，数据库事务而已无法正常提交，
                 *  由于设置的事务回滚点是在sql1语句正常执行完成之后，sql2语句正常执行之前,
                 * 那么通知数据库回滚事务时，不会回滚sql1执行的update操作 只会回滚到sql2执行的update操作，也就是说，
                 * 上面的三条update语句中，sql1这条语句的修改操作起作用了 sql2的修改操作由于事务回滚没有起作用，sql3由于程序异常没有机会执行
                 */
                conn.rollback(sp);// 回滚到设置的事务回滚点
                // 回滚了要记得通知数据库提交事务
                conn.commit();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            connectionMysql.release(conn, st, rs);
        }
    }
}
