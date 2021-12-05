package DriverText;

import com.mchange.v2.c3p0.ConnectionTester;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connection_2 {
    @Test
    public void testConnection() throws SQLException {
        /*
        * 创建连接的步骤
        * 先创建一个Driver对象，在通过Driver中的connect方法来连接数据库 {Driver对象.connect(utl,Properties)}
        *
        * */
        //创建了一个Driver的对象
        Driver driver = new com.mysql.jdbc.Driver();
        //调用Driver中的connect方法来实现连接
        //1.创建url
        String url = "jdbc:mysql://localhost:3306/test";
        //通过Properties类来封装user和password
        Properties  info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
    }

    //通过创建反射来创建Driver对象
    @Test
    public void testConnection2() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        Class clazz = Class.forName("com.mysql.jdbc.Driver");

        //创建一个driver对象
        Driver driver2 = (Driver) clazz.getDeclaredConstructor().newInstance();

        //1.获取url
        String url = "jdbc:mysql://localhost:3306/test";
        //2.通过Properties来获取数据库的root和密码
        Properties info2 = new Properties();
        info2.setProperty("user","root");
        info2.setProperty("password","123456");


        //通过Driver对象来创建Connection连接
        Connection conn2 = driver2.connect(url,info2);
        System.out.println(conn2);
    }

    @Test
    public  void testConnection3 () throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
      Class class3 = Class.forName("com.mysql.jdbc.Driver");
      //创建Driver的对象
        Driver driver3 = (Driver) class3.getDeclaredConstructor().newInstance();
      //注册驱动
        DriverManager.registerDriver(driver3);
        //创建3个连接信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";
      //获取连接
        Connection conn3 = DriverManager.getConnection(url,user,password);
      //已经成功创建连接
        System.out.println(conn3);
    }

    //方式四:在方式三的基础上进行的优化
    @Test
    public void testConnection4_1() throws SQLException {
        //在mysql中可以直接这样写
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        //创建连接
      Connection conn4_1 =   DriverManager.getConnection(url,user,password);

      //创建成功
        System.out.println("第四种创建的连接为:"+conn4_1);

    }
    //方式四:在非mysql时创建的是
    @Test
    public void testConnect4_2() throws ClassNotFoundException, SQLException {
        //创建反射 （创建反射的时候已经注册了Driver）
        Class.forName("com.mysql.jdbc.Driver");

        //三种基本的连接信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "123456";

        //创建连接
        Connection conn4_2 = DriverManager.getConnection(url,user,password);
        System.out.println("一般创建:" + conn4_2);
    }

    //方式五:通过引用外部文件来
    @Test
    public void testConnection5() throws IOException, ClassNotFoundException, SQLException {
        InputStream is = ConnectionTester.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String url = pros.getProperty("url");
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String driverClass = pros.getProperty("driverClass");

        //创建反射
        Class.forName(driverClass);
        //创建连接
        Connection conn5 = DriverManager.getConnection(url,user,password);
        System.out.println("通过数据连接池成功创建了连接:" + conn5);
    }
}
