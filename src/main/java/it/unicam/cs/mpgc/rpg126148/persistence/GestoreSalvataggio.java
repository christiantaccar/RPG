package it.unicam.cs.mpgc.rpg126148.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.tecniche.Tecnica;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestoreSalvataggio {

    private static final String FILE_SALVATAGGIO = "salvataggio.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void salva(Stregone stregone) {
        SalvataggioStato stato = new SalvataggioStato();
        stato.nome = stregone.getNome();
        stato.puntiVita = stregone.getPuntiVita();
        stato.energiaNera = stregone.getEnergiaNera();

        // contiamo i frammenti per tipo
        Map<String, Integer> frammenti = new HashMap<>();
        for (TipoFrammento tipo : TipoFrammento.values()) {
            frammenti.put(tipo.name(), 0);
        }
        stregone.getInventario().getOggetti().stream()
                .filter(o -> o instanceof Frammento)
                .map(o -> (Frammento) o)
                .forEach(f -> frammenti.merge(f.getTipo().name(), 1, Integer::sum));
        stato.frammenti = frammenti;

        // nomi tecniche sbloccate
        stato.tecnicheSbloccate = stregone.getTecnicheSbloccate()
                .stream()
                .map(Tecnica::nome)
                .toList();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_SALVATAGGIO))) {
            gson.toJson(stato, writer);
            System.out.println("💾 Salvataggio completato.");
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
        }
    }

    public SalvataggioStato carica() {
        File file = new File(FILE_SALVATAGGIO);
        if (!file.exists()) {
            System.out.println("Nessun salvataggio trovato.");
            return null;
        }
        try (FileReader reader = new FileReader(file)) {
            SalvataggioStato stato = gson.fromJson(reader, SalvataggioStato.class);
            System.out.println("📂 Salvataggio caricato.");
            return stato;
        } catch (IOException e) {
            System.out.println("Errore durante il caricamento: " + e.getMessage());
            return null;
        }
    }
}
