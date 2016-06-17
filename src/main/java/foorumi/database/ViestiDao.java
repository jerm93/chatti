package foorumi.database;

import foorumi.domain.Kayttaja;
import foorumi.domain.Viesti;
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

public class ViestiDao implements Dao<Viesti, Integer> {

    private Database database;

    public ViestiDao(Database database) {
        this.database = database;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti WHERE id = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String sisalto = rs.getString("sisalto");
        Integer kayttaja = rs.getInt("kayttaja");
        Integer keskustelu = rs.getInt("keskustelu");

        long aika = rs.getLong("aika");

        Viesti viesti = new Viesti(sisalto, kayttaja, keskustelu);
        viesti.setId(id);
        viesti.setAika(aika);

        rs.close();
        stmt.close();
        connection.close();

        return viesti;
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Viesti");

        ResultSet rs = stmt.executeQuery();
        List<Viesti> viestit = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String sisalto = rs.getString("sisalto");
            Integer kayttaja = rs.getInt("kayttaja");
            long aika = rs.getLong("aika");
            Integer keskustelu = rs.getInt("keskustelu");

            Viesti viesti = new Viesti(sisalto, kayttaja, keskustelu);
            viesti.setId(id);
            viesti.setAika(aika);

            viestit.add(viesti);
        }

        rs.close();
        stmt.close();
        connection.close();

        return viestit;
    }

    @Override
    public List<Viesti> findAllIn(Collection<Integer> keys) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Viesti viesti) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Viesti(sisalto, aika, kayttaja, keskustelu) Values(?, ?, ?, ?)");

        String sisalto = viesti.getSisalto();
        int kayttaja = viesti.getKayttaja();
        int keskustelu = viesti.getKeskustelu();

        Date date = new Date();
        long aika = date.getTime();

        stmt.setObject(1, sisalto);
        stmt.setObject(2, aika);
        stmt.setObject(3, kayttaja);
        stmt.setObject(4, keskustelu);
        stmt.execute();
    }

    public ArrayList<String> findAllText(KayttajaDao kayttajaDao) throws SQLException {
        List<Viesti> viestit = this.findAll();
        List<Kayttaja> kayttajat = kayttajaDao.findAll();

        ArrayList<String> sisalto = new ArrayList<>();
        for (Viesti viesti : viestit) {
            long aika = viesti.getAika();
            Date pvm = new Date(aika);
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm:ss");
            String teksti = viesti.getSisalto();
            int kayttajaid = viesti.getKayttaja();

            String tunnus = "";
            for (Kayttaja kayttaja : kayttajat) {
                if (kayttajaid == kayttaja.getId()) {
                    tunnus = kayttaja.getTunnus();
                }
            }

            String kokoViesti = df.format(pvm) + " " + tunnus + ": " + teksti;
            sisalto.add(kokoViesti);
        }
        return sisalto;
    }

}
