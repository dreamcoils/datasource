package com.example.demo.proxy;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
public class StatementProxy implements InvocationHandler {
    private PreparedStatement preparedStatement;
    private Map<Integer, Object> map;
    private String sql;

    public StatementProxy(PreparedStatement preparedStatement, Object[] args) {
        this.preparedStatement = preparedStatement;
        sql = (String) args[0];
        map = Maps.newHashMap();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith("set")) {
            Integer position = (Integer) args[0];
            Object value = args[1];
            map.put(position, value);
            return method.invoke(preparedStatement, args);
        }
        if (method.getName().equals("executeQuery")) {
            ResultSet resultSet = (ResultSet) method.invoke(preparedStatement, args);
            printSql();
            printParas();
            printResult(resultSet);
            return resultSet;
        }
        return null;
    }

    private void printSql() {
        log.info("【SQL】\n{}", sql);
    }

    private void printParas() {
        log.info("【Parameter Set】");
        map.forEach((integer, o) -> {
            log.info("Position{}——>{}", integer, o);
        });
    }

    private void printResult(ResultSet resultSet) {
        try {
            log.info("【Result Set】");
            Integer count = 1;
            while (resultSet.next()) {
                log.info("*Record:{}*", count++);
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    String name = resultSetMetaData.getColumnName(i + 1);
                    Object value = resultSet.getObject(i + 1);
                    log.info("{}：{}", name, value);
                }
            }
            log.info("【End】");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println();

    }
}
