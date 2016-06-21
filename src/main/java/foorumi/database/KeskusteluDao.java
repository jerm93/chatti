package foorumi.database;

import foorumi.domain.Alue;
import foorumi.domain.Keskustelu;
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

public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database database;

    public KeskusteluDao(Database database) {
        this.database = database;
    }

    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE  keskustelu_tunnus = ?");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("keskustelu_tunnus");
        String nimi = rs.getString("keskustelu_nimi");
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
            Integer id = rs.getInt("keskustelu_tunnus");
            String nimi = rs.getString("keskustelu_nimi");
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

        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        // Luodaan IN-kyselyä varten paikat, joihin arvot asetetaan --
        // toistaiseksi IN-parametrille ei voi antaa suoraan kokoelmaa
        StringBuilder muuttujat = new StringBuilder("?");
        for (int i = 1; i < keys.size(); i++) {
            muuttujat.append(", ?");
        }

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu WHERE alue = (" + muuttujat + ")");

        int laskuri = 1;
        for (int key : keys) {
            stmt.setObject(laskuri, key);
            laskuri++;
        }

        ResultSet rs = stmt.executeQuery();
        List<Keskustelu> keskustelut = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("keskustelu_tunnus");
            String nimi = rs.getString("keskustelu_nimi");
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

    public List<String> findAllStrings(IndexParam i, int alueTunnus) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement keskusteluHaku = connection.prepareStatement(
                "SELECT keskustelu_tunnus, keskustelu_nimi, aika "
                        + "FROM Keskustelu, Viesti "
                        + "WHERE alue = " + alueTunnus + " "
                        + "AND keskustelu = keskustelu_tunnus "
                        + "AND keskustelu_tunnus IN "
                        + "(SELECT keskustelu_tunnus FROM Keskustelu "
                        + "WHERE alue = " + alueTunnus + " "
                        + "ORDER BY keskustelu_tunnus DESC "
                        + "LIMIT 10) "
                        + "GROUP BY keskustelu_tunnus "
                        + "ORDER BY aika DESC");
        PreparedStatement viestiHaku = connection.prepareStatement("SELECT COUNT(id) lkm, aika FROM Viesti WHERE keskustelu = ? GROUP BY keskustelu ORDER BY aika DESC LIMIT 1");

        ResultSet keskustelut = keskusteluHaku.executeQuery();

        List<String> teksti = new ArrayList<>();
        while (keskustelut.next()) {
            int id = keskustelut.getInt("keskustelu_tunnus");
            String nimi = keskustelut.getString("keskustelu_nimi");

            if (i == IndexParam.NIMI) {
                teksti.add(nimi);
            } else {
                viestiHaku.setObject(1, id);
                ResultSet viestit = viestiHaku.executeQuery();

                boolean hasOne = viestit.next();
                if (!hasOne) {
                    if (i == IndexParam.LKM) {
                        teksti.add("0");
                    } else {
                        teksti.add("Ei viestejä");
                    }
                    continue;
                }

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

        keskustelut.close();
        keskusteluHaku.close();
        viestiHaku.close();
        connection.close();

        return teksti;
    }

    public Keskustelu findLatest() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Keskustelu ORDER BY keskustelu_tunnus DESC LIMIT 1");

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("keskustelu_tunnus");
        String nimi = rs.getString("keskustelu_nimi");
        int alue = rs.getInt("alue");

        Keskustelu keskustelu = new Keskustelu(nimi, alue);
        keskustelu.setTunnus(id);

        rs.close();
        stmt.close();
        connection.close();

        return keskustelu;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(Keskustelu type) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO Keskustelu(keskustelu_nimi, alue) Values(?, ?)");

        String nimi = type.getNimi();
        int alue = type.getAlue();

        stmt.setObject(1, nimi);
        stmt.setObject(2, alue);
        stmt.execute();
    }

}
