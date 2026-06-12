package it.unicam.cs.mpgc.rpg126148.persistence;

import java.util.List;
import java.util.Map;

public class SalvataggioStato {
    public String nome;
    public int puntiVita;
    public int energiaNera;
    public Map<String, Integer> frammenti;
    public List<String> tecnicheSbloccate;
    public int xGiocatore;
    public int yGiocatore;
    public String dataOra;
    public List<int[]> celleVuotate;//celle vuotate dopo gli eventi
}
