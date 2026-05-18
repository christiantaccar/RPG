package it.unicam.cs.mpgc.rpg126148.tecniche;

public class Calcio implements Tecnica {
    @Override public int esegui() { return 8; }
    @Override public int costoEnergia() { return 0; }
    @Override public String nome() { return "Calcio"; }
}