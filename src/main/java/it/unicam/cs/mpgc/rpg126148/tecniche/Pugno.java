package it.unicam.cs.mpgc.rpg126148.tecniche;

public class Pugno implements Tecnica {
    @Override
    public int esegui(){
        return 10;
    }
    @Override
    public int costoEnergia(){
        return 0;
    }
    @Override
    public String nome() {
        return "Pugno";
    }
}
