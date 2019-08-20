package sr.unasat.ad.entities;

public class Plaats {
    private String plaatsID;
    private String plaatsNaam;

    public Plaats(String pID, String pNaam) {
        plaatsID = pID;
        plaatsNaam = pNaam;
    }

    public String getPlaatsID() {
        return plaatsID;
    }

    public void setPlaatsID(String plaatsID) {
        this.plaatsID = plaatsID;
    }

    public String getPlaatsNaam() {
        return plaatsNaam;
    }

    public void setPlaatsNaam(String plaatsNaam) {
        this.plaatsNaam = plaatsNaam;
    }
}
