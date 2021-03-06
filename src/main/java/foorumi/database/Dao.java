package foorumi.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T findOne(K key) throws SQLException;

    List<T> findAll() throws SQLException;
    
    List<T> findAllIn(Collection<K> keys) throws SQLException;

    void delete(K key) throws SQLException;
    
    void create(T type) throws SQLException;
}
