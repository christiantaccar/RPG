package it.unicam.cs.mpgc.rpg126148.world;

import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.items.TipoFrammento;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;

import java.util.Optional;

public class GestoreRicompense implements IGestoreRicompense {

    public Optional<Frammento> genera(Maledizione nemico) {
        return switch (nemico.getLivelloMinaccia()) {
            case 1 -> Optional.of(new Frammento(TipoFrammento.FUOCO));
            case 2 -> Optional.of(new Frammento(TipoFrammento.CURA));
            case 3 -> Optional.of(new Frammento(TipoFrammento.OSCURITA));
            default -> Optional.empty();
        };
    }
}
