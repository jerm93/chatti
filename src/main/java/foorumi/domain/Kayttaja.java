package foorumi.domain;

public class Kayttaja {
    
    private int id;
    private String tunnus;
    
    public Kayttaja(String tunnus) {
        this.tunnus = tunnus;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getTunnus() {
        return tunnus;
    }

    public void setTunnus(String tunnus) {
        this.tunnus = tunnus;
    }
    
    public String toString() {
        return tunnus;
    }
}