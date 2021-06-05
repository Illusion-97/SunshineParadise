package app.dao.jdbc;

import app.dao.ModelDao;
import app.model.Trip;

import java.sql.*;
import java.util.ArrayList;

public class JdbcTripDao extends JdbcDao implements ModelDao<Long, Trip> {

    @Override
    public Long create(Trip object) {
        long createdId = -1L;
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO trips(name,departure,terminus,cost) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, object.getName());
            preparedStatement.setLong(2, object.getDeparture());
            preparedStatement.setLong(3, object.getTerminus());
            preparedStatement.setFloat(4, object.getCost());
            preparedStatement.execute();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                createdId = rs.getLong(1);
                object.setId(createdId);
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException r) {
                r.printStackTrace();
            }
            e.printStackTrace();
        }
        return createdId;
    }

    @Override
    public Trip findById(Long aLong) {
        Trip aTrip = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM trips WHERE id = ?")) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) aTrip = mapToTrip(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aTrip;
    }

    @Override
    public ArrayList<Trip> findAll() {
        try (Statement st = connection.createStatement()) {
            return listTrips(st.executeQuery("SELECT * FROM trips"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Trip object) {
        int updated = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE trips SET name = ?, departure  = ?, terminus = ?, cost = ? WHERE id = ?")) {
            preparedStatement.setString(1, object.getName());
            preparedStatement.setLong(2, object.getDeparture());
            preparedStatement.setLong(3, object.getTerminus());
            preparedStatement.setFloat(4, object.getCost());
            preparedStatement.setLong(5, object.getId());
            updated = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException r) {
                r.printStackTrace();
            }
            e.printStackTrace();
        }
        return updated > 0;
    }

    @Override
    public boolean remove(Trip object) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM trips WHERE id = ?")) {
            preparedStatement.setLong(1, object.getId());
            preparedStatement.execute();
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException r) {
                r.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<Trip> listTrips(ResultSet rs) throws SQLException {
        ArrayList<Trip> all = new ArrayList<>();
        while (rs.next()) {
            all.add(mapToTrip(rs));
        }
        return all;
    }

    private Trip mapToTrip(ResultSet rs) throws SQLException {
        return new Trip(rs.getLong(1),rs.getString(2),rs.getLong(3),rs.getLong(4),rs.getFloat(5));

    }
}
