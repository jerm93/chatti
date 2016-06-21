/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database;

import foorumi.domain.Alue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue WHERE alue_tunnus = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("alue_tunnus");
        String nimi = rs.getString("alue_nimi");

        Alue alue = new Alue(nimi);
        alue.setTunnus(id);

        rs.close();
        stmt.close();
        connection.close();

        return alue;
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Alue");

        ResultSet rs = stmt.executeQuery();
        List<Alue> alueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("nimi");

            Alue alue = new Alue(nimi);
            alue.setTunnus(id);
            alueet.add(alue);
        }

        rs.close();
        stmt.close();
        connection.close();

        return alueet;
    }

    @Override
    public List<Alue> findAllIn(Collection<Integer> keys) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Alue type) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Alue(nimi) Values(?)");

        String nimi = type.getNimi();

        stmt.setObject(1, nimi);
        stmt.execute();
    }

    public List<String> findAllStrings(IndexParam i) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement alueHaku = connection.prepareStatement("SELECT * FROM Alue");
        PreparedStatement viestiHaku = connection.prepareStatement("SELECT COUNT(id) lkm, aika FROM Viesti, Keskustelu WHERE keskustelu = keskustelu_tunnus AND alue = ?  GROUP BY alue ORDER BY aika DESC LIMIT 1");

        ResultSet alueet = alueHaku.executeQuery();

        List<String> teksti = new ArrayList<>();
        while (alueet.next()) {
            int id = alueet.getInt("alue_tunnus");
            String nimi = alueet.getString("alue_nimi");

            if (i == IndexParam.NIMI) {
                teksti.add(nimi);
            } else {
                viestiHaku.setObject(1, id);
                ResultSet viestit = viestiHaku.executeQuery();

                int lkm = viestit.getInt("lkm");
                long aika = viestit.getLong("aika");
                Date pvm = new Date(aika);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");

                if (i == IndexParam.LKM) {
                    teksti.add("" + lkm);
                } else {
                    teksti.add(df.format(pvm));
                }
                viestit.close();
            }
        }

        alueet.close();
        alueHaku.close();
        viestiHaku.close();
        connection.close();

        return teksti;
    }
}
