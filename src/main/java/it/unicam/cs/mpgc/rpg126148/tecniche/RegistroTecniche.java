package it.unicam.cs.mpgc.rpg126148.tecniche;

import java.util.HashMap;
import java.util.Map;

public class RegistroTecniche {
    private static final Map<String, Tecnica> TECNICHE = new HashMap<>();

    static {
        TECNICHE.put("Palla di Fuoco", new PallaDiFuoco());
        TECNICHE.put("Cura", new Cura());
        TECNICHE.put("Maledizione Nera", new Magia());
    }

    public static Tecnica get(String nome) {
        return TECNICHE.get(nome);
    }
}
