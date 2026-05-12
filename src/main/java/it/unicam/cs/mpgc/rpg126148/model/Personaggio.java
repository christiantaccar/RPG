package it.unicam.cs.mpgc.rpg126148.model;

public class Personaggio {

    private String nome;
    private int puntiVita;
    private int puntiVitaMassimi;
    private int attacco;
    private int difesa;

    public Personaggio(String nome,int puntiVitaMassimi, int attacco, int difesa) {
        this.nome=nome;
        this.puntiVita=puntiVitaMassimi;
        this.puntiVitaMassimi=puntiVitaMassimi;
        this.attacco=attacco;
        this.difesa=difesa;
    }
    public boolean eVivo() {
        return puntiVita > 0;
    }
    public void subisciDanno(int danno) {
        int dannoEffettivo = Math.max(0, danno - difesa);
        puntiVita -= dannoEffettivo;

        if (puntiVita < 0) {
            puntiVita = 0;
        }
    }
    public void cura(int valore) {
        puntiVita += valore;
        if (puntiVita > puntiVitaMassimi) {
            puntiVita = puntiVitaMassimi;
        }
    }

    public int attacca() {
        return attacco;
    }
    //getter
    public String getNome() {
        return nome;
    }

    public int getPuntiVita() {
        return puntiVita;
    }

}
