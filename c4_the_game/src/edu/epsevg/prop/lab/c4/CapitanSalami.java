package edu.epsevg.prop.lab.c4;

public class CapitanSalami implements Jugador, IAuto {

    private int maxDepth;
    private int nodesExplorats = 0;
    private String nomJugador;
    private int myColor;

    public CapitanSalami(int profunditat) {
        this.maxDepth = profunditat;
        this.nomJugador = "CapitanSalami";
    }
    
    @Override
    public String nom() {
        return this.nomJugador;
    }
    
    @Override
    public int moviment(Tauler t, int color) {
        //TODO
        return 0;
    }
}