package it.unicam.cs.mpgc.rpg126148.model;

import java.util.ArrayList;
import java.util.List;

public class Stregone extends Personaggio {
    private int energiaNera;
    private List<String> frammentiPergamena;

    public Stregone(String nome) {
        super(nome,100,15,5);
        this.energiaNera=50;
        this.frammentiPergamena=new ArrayList<>();
    }
    public void usaEnergia(int costo){
        energiaNera-=costo;
        if(energiaNera<=0){
            energiaNera=0;
        }
    }
    public void aggiungiFrammentoPergamene(String frammento){
        frammentiPergamena.add(frammento);
    }

    public int getEnergiaNera() {
        return energiaNera;
    }
    public List<String> getFrammentiPergamena() {
        return frammentiPergamena;
    }
}
