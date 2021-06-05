package app.dao.jdbc;

import app.util.ConnectionManager;

import java.sql.Connection;

public abstract class JdbcDao {
    protected final Connection connection;

    public JdbcDao() {
        this.connection = ConnectionManager.getInstance().getConnexion();
    }
}
