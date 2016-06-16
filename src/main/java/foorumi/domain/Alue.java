package foorumi.domain;

public class Alue {
    private int id;
    private String nimi;
    
    public Alue(String nimi) {
        this.nimi = nimi;
    }
    
    public String getNimi() {
        return nimi;
    }
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }
    
    public int getTunnus() {
        return id;
    }
    public void setTunnus(int id) {
        this.id = id;
    }
}
