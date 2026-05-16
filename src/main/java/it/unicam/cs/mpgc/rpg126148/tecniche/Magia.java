package it.unicam.cs.mpgc.rpg126148.tecniche;

public class Magia implements Tecnica {
    @Override
    public int esegui() {
        return 25;
    }
    @Override
    public int costoEnergia() {
        return 20;
    }
    @Override
    public String nome() {
        return "Magia";
    }

}
