package foorumi;

import java.util.*;
import spark.ModelAndView;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import foorumi.database.*;
import foorumi.domain.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Database database = new Database("jdbc:sqlite:chat");
        database.init();

        KayttajaDao kayttajaDao = new KayttajaDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        get("/chat", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("viestit", viestiDao.findAllText(kayttajaDao));
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());

//        get("/", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("viesti", "tervehdys");
//
//            return new ModelAndView(map, "index");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelijat", opiskelijaDao.findAll());
//
//            return new ModelAndView(map, "opiskelijat");
//        }, new ThymeleafTemplateEngine());
//
//        get("/opiskelijat/:id", (req, res) -> {
//            HashMap map = new HashMap<>();
//            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));
//
//            return new ModelAndView(map, "opiskelija");
//        }, new ThymeleafTemplateEngine());
        post("/chat", (req, res) -> {
            List<Kayttaja> kayttajat = kayttajaDao.findAll();
            String nimi = req.queryParams("nimi");
            String sisalto = req.queryParams("viesti");
            int kayttajatunnus = 0;
            int viimeinen = 0;
            for (Kayttaja kayttaja : kayttajat) {
                if (kayttaja.getTunnus().equals(nimi)) {
                    kayttajatunnus = kayttaja.getId();
                }
                viimeinen = kayttaja.getId();
            }
            Kayttaja kayttaja = new Kayttaja(nimi);
            if (kayttajatunnus == 0) {
                kayttajatunnus = viimeinen + 1;
                kayttajaDao.create(kayttaja);
            }

            
            Viesti viesti = new Viesti(sisalto, kayttajatunnus);
            viestiDao.create(viesti);
            
            HashMap map = new HashMap<>();

            map.put("viestit", viestiDao.findAllText(kayttajaDao));
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
    }
}
