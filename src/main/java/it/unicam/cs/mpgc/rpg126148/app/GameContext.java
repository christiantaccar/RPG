package it.unicam.cs.mpgc.rpg126148.app;

import it.unicam.cs.mpgc.rpg126148.persistence.GestoreSalvataggio;
import it.unicam.cs.mpgc.rpg126148.persistence.ISalvataggio;
import it.unicam.cs.mpgc.rpg126148.world.GestoreFrammenti;
import it.unicam.cs.mpgc.rpg126148.world.GestoreRicompense;
import it.unicam.cs.mpgc.rpg126148.world.IGestoreFrammenti;
import it.unicam.cs.mpgc.rpg126148.world.IGestoreRicompense;

public class GameContext {
    private final ISalvataggio gestoreSalvataggio;
    private final IGestoreFrammenti gestoreFrammenti;
    private final IGestoreRicompense gestoreRicompense;
    private int slotCorrente = 1;

    public GameContext() {
        this.gestoreSalvataggio = new GestoreSalvataggio();
        this.gestoreFrammenti = new GestoreFrammenti();
        this.gestoreRicompense = new GestoreRicompense();
    }

    public ISalvataggio getGestoreSalvataggio() { return gestoreSalvataggio; }
    public IGestoreFrammenti getGestoreFrammenti() { return gestoreFrammenti; }
    public IGestoreRicompense getGestoreRicompense() { return gestoreRicompense; }
    public int getSlotCorrente() { return slotCorrente; }
    public void setSlotCorrente(int slot) { this.slotCorrente = slot; }
}