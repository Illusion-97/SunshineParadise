package app.dao.jdbc;

import app.dao.ModelDao;
import app.model.Place;

import java.sql.*;
import java.util.ArrayList;

public class JdbcPlaceDao extends JdbcDao implements ModelDao<Long, Place> {

    @Override
    public Long create(Place object) {
        long createdId = -1;
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO places(name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, object.getName());
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
    public Place findById(Long aLong) {
        Place aPlace = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM places WHERE id = ?")) {
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) aPlace = mapToPlace(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aPlace;
    }

    @Override
    public ArrayList<Place> findAll() {
        try (Statement st = connection.createStatement()) {
            return listPlaces(st.executeQuery("SELECT * FROM places"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean update(Place object) {
        int updated = 0;
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE places SET name = ? WHERE id = ?")) {
            preparedStatement.setString(1, object.getName());
            preparedStatement.setLong(2, object.getId());
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
    public boolean remove(Place object) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM places WHERE id = ?")) {
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

    private ArrayList<Place> listPlaces(ResultSet rs) throws SQLException {
        ArrayList<Place> all = new ArrayList<>();
        while (rs.next()) {
            all.add(mapToPlace(rs));
        }
        return all;
    }

    private Place mapToPlace(ResultSet rs) throws SQLException {
        return new Place(rs.getLong(1),rs.getString(2));

    }
}
