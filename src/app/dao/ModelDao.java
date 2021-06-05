package app.dao;

import java.util.ArrayList;

public interface ModelDao<Long,Bean> {

    Long create(Bean object);

    Bean findById(Long id);

    boolean update(Bean object);

    boolean remove(Bean object);

    ArrayList<Bean> findAll();
}
