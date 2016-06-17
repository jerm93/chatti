package foorumi.domain;

public class Keskustelu {

    private int id;
    private String nimi;
    private int alue;
    
    public Keskustelu(String nimi, int alue) {
        this.nimi = nimi;
        this.alue = alue;
    }
    
    public int getTunnus() {
        return id;
    }
    public void setTunnus(int id) {
        this.id = id;
    }
    
    public String getNimi() {
        return nimi;
    }
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public int getAlue() {
        return alue;
    }
    public void setAlue(int alue) {
        this.alue = alue;
    }
}
