package it.unicam.cs.mpgc.rpg126148.world;

import it.unicam.cs.mpgc.rpg126148.items.Frammento;
import it.unicam.cs.mpgc.rpg126148.model.Maledizione;

import java.util.Optional;

public interface IGestoreRicompense {
    Optional<Frammento> genera(Maledizione nemico);
}