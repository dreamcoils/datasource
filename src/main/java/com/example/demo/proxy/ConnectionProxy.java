package com.example.demo.proxy;

import java.lang.reflect.Proxy;
import java.sql.Connection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;

public class ConnectionProxy implements InvocationHandler {
    private Connection connection;

    public ConnectionProxy(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("prepareStatement")) {
            System.out.println("Connection Proxy Running ...");
            PreparedStatement preparedStatement = (PreparedStatement) method.invoke(connection, args);
            StatementProxy statementProxy = new StatementProxy(preparedStatement, args);
            PreparedStatement resultStatement = (PreparedStatement) Proxy.newProxyInstance
                    (this.getClass().getClassLoader(), new Class[]{PreparedStatement.class}, statementProxy);
            return resultStatement;
        }
        return null;
    }
}
