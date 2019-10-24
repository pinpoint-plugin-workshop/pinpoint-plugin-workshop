package com.pinpoint.workshop.pizza.oven.type;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author HyunGil Jeong
 */
public class UuidTypeHandler extends BaseTypeHandler<UUID> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, UUID uuid, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, uuid.toString());
    }

    @Override
    public UUID getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String uuidString = resultSet.getString(s);
        if (uuidString != null) {
            return UUID.fromString(uuidString);
        }
        return null;
    }

    @Override
    public UUID getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String uuidString = resultSet.getString(i);
        if (uuidString != null) {
            return UUID.fromString(uuidString);
        }
        return null;
    }

    @Override
    public UUID getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String uuidString = callableStatement.getString(i);
        if (uuidString != null) {
            return UUID.fromString(uuidString);
        }
        return null;
    }
}
