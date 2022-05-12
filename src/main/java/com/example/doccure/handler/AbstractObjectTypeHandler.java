package com.example.doccure.handler;

import cn.hutool.json.JSONUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.thymeleaf.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AbstractObjectTypeHandler<T> extends BaseTypeHandler<T> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, T t, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i,JSONUtil.toJsonStr(t));
    }

    @Override
    public T getNullableResult(ResultSet resultSet, String columnName) throws SQLException{
        String data = resultSet.getString(columnName);
        return StringUtils.isEmpty(data) ? null : JSONUtil.toBean(data, (Class<T>) getRawType());
    }

    @Override
    public T getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String data = resultSet.getString(columnIndex);
        return StringUtils.isEmpty(data) ? null : JSONUtil.toBean(data, (Class<T>) getRawType());
    }

    @Override
    public T getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String data = callableStatement.getString(columnIndex);
        return StringUtils.isEmpty(data) ? null : JSONUtil.toBean(data, (Class<T>) getRawType());
    }
}
