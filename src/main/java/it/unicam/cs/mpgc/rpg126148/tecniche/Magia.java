package it.unicam.cs.mpgc.rpg126148.tecniche;

public class Magia implements Tecnica {
    @Override
    public int esegui() {
        return 40;
    }
    @Override
    public int costoEnergia() {
        return 35;
    }
    @Override
    public String nome() {
        return "Maledizione Nera";
    }

}
