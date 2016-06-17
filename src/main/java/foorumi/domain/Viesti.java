package foorumi.domain;

public class Viesti {
    
    private int id;
    private String sisalto;
    private int kayttaja;
    private long aika;
    private int keskustelu;
    
    public Viesti(String sisalto, int kayttaja, int keskustelu) {
        this.sisalto = sisalto;
        this.kayttaja = kayttaja;
        this.keskustelu = keskustelu;
    }

    public String getSisalto() {
        return sisalto;
    }

    public void setSisalto(String sisalto) {
        this.sisalto = sisalto;
    }
    
    public int getKayttaja() {
        return kayttaja;
    }

    public void setKayttaja(int kayttaja) {
        this.kayttaja = kayttaja;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public long getAika() {
        return aika;
    }
    
    public void setAika(long time) {
        this.aika = time;
    }
    
    public int getKeskustelu() {
        return keskustelu;
    }
    
    public void setKeskustelu(int keskustelu) {
        this.keskustelu = keskustelu;
    }
    
    public String toString() {
        return sisalto;
    }
}
