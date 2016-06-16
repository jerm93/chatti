package foorumi.database;

import foorumi.domain.Alue;
import foorumi.domain.Keskustelu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeskusteluDao implements Dao<Keskustelu, Integer>{

    private Database database;
    
    public KeskusteluDao(Database database) {
        this.database = database;
    }
    
    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        int alue = rs.getInt("alue");

        Keskustelu keskustelu = new Keskustelu(nimi, alue);
        keskustelu.setTunnus(id);
        
        rs.close();
        stmt.close();
        connection.close();

        return keskustelu;
    }

    @Override
    public List<Keskustelu> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu");

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
        String nimi = rs.getString("nimi");
        int alue = rs.getInt("alue");

        Keskustelu keskustelu = new Keskustelu(nimi, alue);
        keskustelu.setTunnus(id);
            keskustelut.add(keskustelu);
        }

        rs.close();
        stmt.close();
        connection.close();

        return keskustelut;
    }

    @Override
    public List<Keskustelu> findAllIn(Collection<Integer> keys) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Keskustelu type) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskustelu(nimi, alue) Values(?, ?)");
        
        String nimi = type.getNimi();
        int alue = type.getAlue();
        
        stmt.setObject(1, nimi);
        stmt.setObject(2, alue);
        stmt.execute();
    }
    
}
