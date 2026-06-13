package it.unicam.cs.mpgc.rpg126148.persistence;

import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.world.Mappa;

public interface ISalvataggio {
    void salva(Stregone stregone, Mappa mappa, int slot);
    SalvataggioStato carica(int slot);
    void elimina(int slot);
    SalvataggioStato[] tuttiSlot();
}