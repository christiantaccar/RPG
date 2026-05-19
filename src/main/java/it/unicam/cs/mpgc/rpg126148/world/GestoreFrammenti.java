package it.unicam.cs.mpgc.rpg126148.world;

import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.Oggetto;
import it.unicam.cs.mpgc.rpg126148.items.Pergamena;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Stregone;
import it.unicam.cs.mpgc.rpg126148.tecniche.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GestoreFrammenti {

    private static final Map<TipoFrammento, Integer> SOGLIE = new EnumMap<>(TipoFrammento.class);

    static {
        SOGLIE.put(TipoFrammento.FUOCO, 3);
        SOGLIE.put(TipoFrammento.CURA, 3);
        SOGLIE.put(TipoFrammento.OSCURITA, 5);
    }

    public void aggiungiFrammento(Stregone stregone, Frammento frammento) {
        stregone.getInventario().aggiungi(frammento);
        System.out.println("🔮 Hai ottenuto: " + frammento.getNome());

        int conteggio = contaFrammenti(stregone, frammento.getTipo());
        int soglia = SOGLIE.get(frammento.getTipo());

        System.out.println("   Frammenti " + frammento.getTipo().name() + ": " + conteggio + "/" + soglia);

        if (conteggio >= soglia) {
            sblocca(stregone, frammento.getTipo());
        }
    }

    private int contaFrammenti(Stregone stregone, TipoFrammento tipo) {
        return (int) stregone.getInventario().getOggetti().stream()
                .filter(o -> o instanceof Frammento)
                .map(o -> (Frammento) o)
                .filter(f -> f.getTipo() == tipo)
                .count();
    }

    private void sblocca(Stregone stregone, TipoFrammento tipo) {
        // rimuove i frammenti usati per lo sblocco
        int soglia = SOGLIE.get(tipo);
        List<Oggetto> daRimuovere = stregone.getInventario().getOggetti().stream()
                .filter(o -> o instanceof Frammento)
                .map(o -> (Frammento) o)
                .filter(f -> f.getTipo() == tipo)
                .limit(soglia)
                .map(f -> (Oggetto) f)
                .toList();
        daRimuovere.forEach(stregone.getInventario()::rimuovi);

        // sblocca la tecnica
        Pergamena pergamena = switch (tipo) {
            case FUOCO -> new Pergamena(
                    "Pergamena del Fuoco",
                    "Contiene Palla di Fuoco",
                    new PallaDiFuoco()
            );
            case CURA -> new Pergamena(
                    "Pergamena della Cura",
                    "Contiene la tecnica Cura",
                    new Cura()
            );
            case OSCURITA -> new Pergamena(
                    "Pergamena dell'Oscurità",
                    "Contiene una tecnica oscura",
                    new Magia()
            );
        };
        stregone.sbloccaTecnica(pergamena);
    }
}
