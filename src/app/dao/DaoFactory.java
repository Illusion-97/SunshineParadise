package app.dao;

import app.dao.jdbc.JdbcPlaceDao;
import app.dao.jdbc.JdbcTripDao;

public class DaoFactory {

    private DaoFactory() {
    }

    public static JdbcTripDao getTripDao(){
        return new JdbcTripDao();
    }

    public static JdbcPlaceDao getPlaceDao(){
        return new JdbcPlaceDao();
    }
}
