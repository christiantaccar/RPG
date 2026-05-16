package it.unicam.cs.mpgc.rpg126148.model;

import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;

import java.util.ArrayList;
import java.util.List;

public class Stregone extends Personaggio {
    private int energiaNera;
    private int energiaNeraMassima;
    private List<String> frammentiPergamena;
    private Tecnica tecnicaAttuale;

    public Stregone(String nome) {
        super(nome,100,15,5);
        this.energiaNeraMassima=100;
        this.energiaNera=energiaNeraMassima;
        this.frammentiPergamena=new ArrayList<>();
    }

    public void setTecnica(Tecnica tecnica) {
        this.tecnicaAttuale = tecnica;
    }

    public Tecnica getTecnicaAttuale() {
        return tecnicaAttuale;
    }
    public int usaTecnica() {
        return tecnicaAttuale.esegui();
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
    public boolean haEnergia(int costo) {
        return energiaNera >= costo;
    }
    public void consumaEnergia(int valore) {
        energiaNera -= valore;

        if (energiaNera < 0) {
            energiaNera = 0;
        }
    }
}
