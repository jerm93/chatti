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
        AlueDao alueDao = new AlueDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database);

        get("/index", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("alueet", alueDao.findAllStrings(IndexParam.NIMI));
            map.put("maarat", alueDao.findAllStrings(IndexParam.LKM));
            map.put("ajat", alueDao.findAllStrings(IndexParam.PVM));
            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        get("/chat", (req, res) -> {
            HashMap map = new HashMap<>();

            map.put("viestit", viestiDao.findAllText(kayttajaDao));
            return new ModelAndView(map, "chat");
        }, new ThymeleafTemplateEngine());

//       
//        post("/chat", (req, res) -> {
//            List<Kayttaja> kayttajat = kayttajaDao.findAll();
//            String nimi = req.queryParams("nimi");
//            String sisalto = req.queryParams("viesti");
//            int kayttajatunnus = 0;
//            int viimeinen = 0;
//            for (Kayttaja kayttaja : kayttajat) {
//                if (kayttaja.getTunnus().equals(nimi)) {
//                    kayttajatunnus = kayttaja.getId();
//                }
//                viimeinen = kayttaja.getId();
//            }
//            Kayttaja kayttaja = new Kayttaja(nimi);
//            if (kayttajatunnus == 0) {
//                kayttajatunnus = viimeinen + 1;
//                kayttajaDao.create(kayttaja);
//            }
//
//            
//            Viesti viesti = new Viesti(sisalto, kayttajatunnus);
//            viestiDao.create(viesti);
//            
//            HashMap map = new HashMap<>();
//
//            map.put("viestit", viestiDao.findAllText(kayttajaDao));
//            return new ModelAndView(map, "chat");
//        }, new ThymeleafTemplateEngine());
    }
}
