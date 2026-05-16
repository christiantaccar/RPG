package it.unicam.cs.mpgc.rpg126148.tecniche;

public class Cura implements Tecnica{
    @Override
    public String nome() {
        return "Cura";
    }
    @Override
    public int costoEnergia() {
        return 15;
    }
    @Override
    public int esegui(){
        return -15;//negativo=cura
    }
}
