package iss.bug_application.repository.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private Properties jdbcProperties;
    private Connection instance = null;

    public JdbcUtils(Properties jdbcProperties) {
        this.jdbcProperties = jdbcProperties;
    }

    private Connection getNewConnection(){

        String url = jdbcProperties.getProperty("jdbc.url");
        String user = jdbcProperties.getProperty("jdbc.user");
        String pass = jdbcProperties.getProperty("jdbc.pass");
        Connection con = null;

        try{
            if(user != null && pass != null)
                con = DriverManager.getConnection(url, user, pass);
            else
                con = DriverManager.getConnection(url);
        }
        catch (SQLException e){
            System.out.println("Error getting connection: " + e);
        }

        return con;
    }

    public Connection getConnection(){

        try{
            if(instance == null || instance.isClosed())
                instance = getNewConnection();
        }
        catch(SQLException e){
            System.out.println("Error from DataBase: " + e);
        }

        return instance;
    }
}
