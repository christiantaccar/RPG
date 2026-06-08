package it.unicam.cs.mpgc.rpg126148.world;

import java.util.ArrayList;
import java.util.List;

public class Mappa {
    private final int larghezza;
    private final int altezza;
    private final Cella[][] griglia;
    private final List<Stanza> stanze;
    private int xGiocatore;
    private int yGiocatore;

    public Mappa(int larghezza, int altezza) {
        this.larghezza = larghezza;
        this.altezza = altezza;
        this.griglia = new Cella[altezza][larghezza];
        this.stanze = new ArrayList<>();

        for (int y = 0; y < altezza; y++) {
            for (int x = 0; x < larghezza; x++) {
                griglia[y][x] = new Cella(x, y, TipoCella.MURO);
            }
        }
    }

    public void aggiungiStanza(Stanza stanza) {
        stanze.add(stanza);
        int xI = stanza.getXInizio();
        int yI = stanza.getYInizio();
        int w = stanza.getLarghezza();
        int h = stanza.getAltezza();

        for (int y = yI; y < yI + h; y++) {
            for (int x = xI; x < xI + w; x++) {
                boolean isBordo = x == xI || x == xI + w - 1
                        || y == yI || y == yI + h - 1;
                griglia[y][x].setTipo(isBordo ? TipoCella.BORDO : TipoCella.VUOTA);
            }
        }
    }

    public void aggiungiCorridoio(int xInizio, int yInizio, int xFine, int yFine) {
        if (xInizio == xFine) {
            int minY = Math.min(yInizio, yFine);
            int maxY = Math.max(yInizio, yFine);
            for (int y = minY; y <= maxY; y++) {
                griglia[y][xInizio].setTipo(TipoCella.VUOTA);
            }
        } else {
            int minX = Math.min(xInizio, xFine);
            int maxX = Math.max(xInizio, xFine);
            for (int x = minX; x <= maxX; x++) {
                griglia[yInizio][x].setTipo(TipoCella.VUOTA);
            }
        }
    }

    public void setIngresso(int x, int y) {
        griglia[y][x].setTipo(TipoCella.INGRESSO);
    }

    public void posizionaNemico(int x, int y) {
        griglia[y][x].setTipo(TipoCella.NEMICO);
    }

    public void posizionaCassa(int x, int y) {
        griglia[y][x].setTipo(TipoCella.CASSA);
    }

    public void posizionaGiocatore(int x, int y) {
        this.xGiocatore = x;
        this.yGiocatore = y;
    }

    public Cella getCella(int x, int y) { return griglia[y][x]; }
    public int getXGiocatore() { return xGiocatore; }
    public int getYGiocatore() { return yGiocatore; }
    public int getLarghezza() { return larghezza; }
    public int getAltezza() { return altezza; }

    public Stanza getStanzaCorrente() {
        return stanze.stream()
                .filter(s -> s.contiene(xGiocatore, yGiocatore))
                .findFirst()
                .orElse(null);
    }

    public boolean isInternoStanza(int x, int y) {
        return stanze.stream().anyMatch(s -> s.contiene(x, y)
                && griglia[y][x].getTipo() != TipoCella.BORDO
                && griglia[y][x].getTipo() != TipoCella.INGRESSO);
    }

    public void stampa() {
        Stanza stanzaGiocatore = getStanzaCorrente();

        for (int y = 0; y < altezza; y++) {
            for (int x = 0; x < larghezza; x++) {
                final int fx = x;
                final int fy = y;

                if (x == xGiocatore && y == yGiocatore) {
                    System.out.print("@ ");
                    continue;
                }

                Cella cella = griglia[y][x];
                TipoCella tipo = cella.getTipo();

                if (tipo != TipoCella.MURO
                        && tipo != TipoCella.BORDO
                        && tipo != TipoCella.INGRESSO) {
                    boolean dentroAltraStanza = stanze.stream()
                            .filter(s -> s != stanzaGiocatore)
                            .anyMatch(s -> s.contiene(fx, fy)
                                    && griglia[fy][fx].getTipo() != TipoCella.BORDO
                                    && griglia[fy][fx].getTipo() != TipoCella.INGRESSO);
                    if (dentroAltraStanza) {
                        System.out.print("? ");
                        continue;
                    }
                }

                switch (tipo) {
                    case MURO -> System.out.print("# ");
                    case BORDO -> System.out.print("+ ");
                    case INGRESSO -> System.out.print("O ");
                    case VUOTA -> System.out.print(". ");
                    case NEMICO -> System.out.print("N ");
                    case CASSA -> System.out.print("C ");
                }
            }
            System.out.println();
        }
    }
}