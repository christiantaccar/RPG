package it.unicam.cs.mpgc.rpg126148.app;

import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.persistence.GestoreSalvataggio;
import it.unicam.cs.mpgc.rpg126148.world.GestoreFrammenti;
import it.unicam.cs.mpgc.rpg126148.world.GestoreRicompense;
import it.unicam.cs.mpgc.rpg126148.world.Mappa;

public class GameContext {
    private Stregone giocatore;
    private Mappa mappa;
    private final GestoreSalvataggio gestoreSalvataggio;
    private final GestoreFrammenti gestoreFrammenti;
    private final GestoreRicompense gestoreRicompense;
    private int slotCorrente=1;
    public GameContext() {
        this.gestoreSalvataggio = new GestoreSalvataggio();
        this.gestoreFrammenti = new GestoreFrammenti();
        this.gestoreRicompense = new GestoreRicompense();
    }

    public Stregone getGiocatore() { return giocatore; }
    public void setGiocatore(Stregone giocatore) { this.giocatore = giocatore; }

    public Mappa getMappa() { return mappa; }
    public void setMappa(Mappa mappa) { this.mappa = mappa; }

    public GestoreSalvataggio getGestoreSalvataggio() { return gestoreSalvataggio; }
    public GestoreFrammenti getGestoreFrammenti() { return gestoreFrammenti; }
    public GestoreRicompense getGestoreRicompense() { return gestoreRicompense; }
    public int getSlotCorrente() { return slotCorrente; }
    public void setSlotCorrente(int slot) { this.slotCorrente = slot; }
}