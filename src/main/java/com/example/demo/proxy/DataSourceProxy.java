package com.example.demo.proxy;

import org.apache.ibatis.datasource.pooled.PooledDataSource;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class DataSourceProxy implements InvocationHandler {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public DataSourceProxy(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DataSource getDataSourceProxy(String driver, String url, String username, String password) {
        DataSource dataSource = new PooledDataSource(driver, url, username, password);
        DataSourceProxy dataSourceProxy = new DataSourceProxy(dataSource);
        DataSource result = (DataSource) Proxy.newProxyInstance(dataSource.getClass().getClassLoader(), new Class[]{DataSource.class}, dataSourceProxy);
        return result;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("getConnection")) {
            System.out.println("DataSource Proxy Running ...");
            try {
                Connection connection = (Connection) method.invoke(dataSource, args);
                ConnectionProxy connectionProxy = new ConnectionProxy(connection);
                Connection resultConnection = (Connection) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Connection.class}, connectionProxy);
                return resultConnection;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
