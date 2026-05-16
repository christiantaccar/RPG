package it.unicam.cs.mpgc.rpg126148.tecniche;

public class AttaccoFisico implements Tecnica {
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
        return "Attacco Fisico";
    }
}
