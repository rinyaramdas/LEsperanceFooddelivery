package sr.unasat.ad.entities;

public class VervoersType {
    private String vervoerstypeId;
    private String vervoerstype;

    public VervoersType(String vtId, String vt) {
        vervoerstypeId = vtId;
        vervoerstype = vt;
    }

    public String getVervoerstypeId() {
        return vervoerstypeId;
    }

    public void setVervoerstypeId(String vervoerstypeId) {
        this.vervoerstypeId = vervoerstypeId;
    }

    public String getVervoerstype() {
        return vervoerstype;
    }

    public void setVervoerstype(String vervoerstype) {
        this.vervoerstype = vervoerstype;
    }
}
