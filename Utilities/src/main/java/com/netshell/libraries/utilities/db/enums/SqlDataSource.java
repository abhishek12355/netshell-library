package com.netshell.libraries.utilities.db.enums;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Abhishek
 *         Created on 12/26/2015.
 */
public class SqlDataSource<T> implements DataSource<T> {

    private final ResultSet resultSet;

    public SqlDataSource(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public void activate() {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean hasNext() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new DBEnumException("Error Occurred", e);
        }
    }

    @Override
    public T next() {
        return null;
    }
}
