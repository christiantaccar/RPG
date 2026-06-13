package it.unicam.cs.mpgc.rpg126148.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;
import it.unicam.cs.mpgc.rpg126148.world.Cella;
import it.unicam.cs.mpgc.rpg126148.world.TipoCella;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestoreSalvataggio implements ISalvataggio{

    private static final int NUM_SLOT = 5;
    private static final String PREFISSO = "salvataggio_";
    private static final String ESTENSIONE = ".json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private String nomeFile(int slot) {
        return PREFISSO + slot + ESTENSIONE;
    }

    public void salva(Stregone stregone, it.unicam.cs.mpgc.rpg126148.world.Mappa mappa, int slot) {
        SalvataggioStato stato = new SalvataggioStato();
        stato.nome = stregone.getNome();
        stato.puntiVita = stregone.getPuntiVita();
        stato.energiaNera = stregone.getEnergiaNera();
        stato.xGiocatore = mappa.getXGiocatore();
        stato.yGiocatore = mappa.getYGiocatore();
        stato.dataOra = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        Map<String, Integer> frammenti = new HashMap<>();
        for (TipoFrammento tipo : TipoFrammento.values()) {
            frammenti.put(tipo.name(), 0);
        }
        stregone.getInventario().getOggetti().stream()
                .filter(o -> o instanceof Frammento)
                .map(o -> (Frammento) o)
                .forEach(f -> frammenti.merge(f.getTipo().name(), 1, Integer::sum));
        stato.frammenti = frammenti;

        stato.tecnicheSbloccate = stregone.getTecnicheSbloccate()
                .stream().map(Tecnica::nome).toList();
        // salva celle diventate VUOTE (ex nemici/casse sconfitti)
        List<int[]> celleVuotate = new ArrayList<>();
        for (int y = 0; y < mappa.getAltezza(); y++) {
            for (int x = 0; x < mappa.getLarghezza(); x++) {
                Cella cella = mappa.getCella(x, y);
                if (cella.isEsplorata() && cella.getTipo() == TipoCella.VUOTA) {
                    celleVuotate.add(new int[]{x, y});
                }
            }
        }
        stato.celleVuotate = celleVuotate;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFile(slot)))) {
            gson.toJson(stato, writer);
            System.out.println("💾 Salvataggio slot " + slot + " completato.");
        } catch (IOException e) {
            System.out.println("Errore salvataggio: " + e.getMessage());
        }
    }

    public SalvataggioStato carica(int slot) {
        File file = new File(nomeFile(slot));
        if (!file.exists()) return null;
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, SalvataggioStato.class);
        } catch (IOException e) {
            return null;
        }
    }

    public void elimina(int slot) {
        new File(nomeFile(slot)).delete();
    }

    public SalvataggioStato[] tuttiSlot() {
        SalvataggioStato[] slot = new SalvataggioStato[NUM_SLOT];
        for (int i = 0; i < NUM_SLOT; i++) {
            slot[i] = carica(i + 1);
        }
        return slot;
    }

}