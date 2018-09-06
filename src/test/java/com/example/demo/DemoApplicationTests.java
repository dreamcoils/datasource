package com.example.demo;

import com.example.demo.proxy.DataSourceProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://10.30.128.232:3306/ptc_clue";
    private String username = "home";
    private String password = "123456";
    private String sql = "select * from clue_sea_public where city_id=? and brand=?";

    @Test
    public void contextLoads() {
        DataSource dataSource = DataSourceProxy.getDataSourceProxy(driver, url, username, password);
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "11");
            preparedStatement.setString(2, "platform");
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
