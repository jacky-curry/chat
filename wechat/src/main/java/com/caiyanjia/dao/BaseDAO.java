package com.caiyanjia.dao;

import com.caiyanjia.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这里可以通过class类反射获取类里面的属性，进行sql语句的拼接，就不用每个都写一个daoImpl层
 */
public abstract class BaseDAO {
    /**
     *
     * @param conn
     * @param sql
     * @param args
     * @return
     */

    public int Update(Connection conn, String sql, Object... args) {// sql中的可变形参于占位符个数相等,仅适用于当前的表

        PreparedStatement ps = null;
        try {

            // 1.预编译sql语句，返回PrepareStatement的实例
            ps = conn.prepareStatement(sql);
            // 2.填充占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);//索引从1开始

            }
            // 3.执行
            // 成功更新的话，返回值大于0
            return ps.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // 资源的关闭
            // 但因为事务的同步原因，暂时不关闭连接
            try {
                JDBCUtils.closeResoure(null, ps, ps.getResultSet());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return 0;

    }

    // 通用的查询操作，用于返回数据表中的一条记录（考虑事务）
    public static <T> T getInstance(Connection conn, Class<T> clazz, String sql, Object... args) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据:ResultSetMetaData
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();

            if (rs.next()) {
                @SuppressWarnings("deprecation")
                T t = clazz.newInstance();// 获取类对象
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列列值
                    Object columValue = rs.getObject(i + 1);
                    // 获取每个列列名
                    String columnName = rsmd.getColumnName(i + 1);

                    // 给t对象指定的columnName属性，赋值columnValue，用反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);//设置为可编辑
                    field.set(t, columValue);
                }
                return t;

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResoure(null, ps, rs);
        }

        return null;

    }

//通用的查询操作，用于返回数据表中的多条记录的集合（考虑事务）

    public static <T> List<T> getForList(Connection conn, Class<T> clazz, String sql, Object... args) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            // 获取结果集的元数据:ResultSetMetaData
            java.sql.ResultSetMetaData rsmd = rs.getMetaData();
            // 通过ResultSetMetaData获取结果集中的列数
            int columnCount = rsmd.getColumnCount();
            // 创建集合对象，用于储存查询的多行数据
            ArrayList<T> list = new ArrayList<T>();

            while (rs.next()) {
                @SuppressWarnings("deprecation")
                T t = clazz.newInstance();// 有点问题
                for (int i = 0; i < columnCount; i++) {
                    // 获取每个列列值
                    Object columValue = rs.getObject(i + 1);
                    // 获取每个列列名
                    String columnName = rsmd.getColumnName(i + 1);

                    // 给t对象指定的columnName属性，赋值columnValue，用过反射
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(t, columValue);

                }
                // 将每个对象添加到集合中
                list.add(t);

            }
            // 返回集合
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResoure(null, ps, rs);
        }

        return null;

    }

    //用于查询一些特殊值的通用方法
    public static <E> E getValue(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            rs = ps.executeQuery();
            if (rs.next()) {
                return (E) rs.getObject(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResoure(null, ps, rs);
        }
        return null;
    }

}
