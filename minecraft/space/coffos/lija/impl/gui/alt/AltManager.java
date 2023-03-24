package space.coffos.lija.impl.gui.alt;

import java.util.ArrayList;
import java.util.List;

public class AltManager {

    private List<Alt> alts;
    private Alt lastAlt;

    Alt getLastAlt() {
        return lastAlt;
    }

    public void setLastAlt(Alt alt) {
        lastAlt = alt;
    }

    public void setupAlts() {
        alts = new ArrayList();
    }

    public List<Alt> getAlts() {
        return alts;
    }
}