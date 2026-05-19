package it.unicam.cs.mpgc.rpg126148.tecniche;

public class PallaDiFuoco implements Tecnica {
    @Override public int esegui() { return 30; }
    @Override public int costoEnergia() { return 25; }
    @Override public String nome() { return "Palla di Fuoco"; }
}
