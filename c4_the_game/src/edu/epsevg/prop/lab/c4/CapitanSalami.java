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
        this.myColor = color;
        nodesExplorats = 0;
        
        // Variables per guardar la millor jugada trobada
        int millorCol = -1;
        int millorValor = Integer.MIN_VALUE;

        // Bucle principal (l'arrel de l'arbre)
        for (int col = 0; col < t.getMida(); ++col) {
            if (t.movpossible(col)) {
                
                // Simulem el moviment
                Tauler aux = new Tauler(t);
                aux.afegeix(col, color);
                
                // Si guanyem directament, ni ho pensem
                if (aux.solucio(col, color)) return col;

                // Cridem al minimax (ara li toca al rival -> minimitzar)
                int valor = minimax(aux, maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

                // Ens quedem amb la millor opció
                if (valor > millorValor) {
                    millorValor = valor;
                    millorCol = col;
                }
            }
        }

        System.out.println("Heurístiques calculades: " + nodesExplorats);
        
        // Si tot falla (no hauria de passar), tornem la primera vàlida
        if (millorCol == -1) {
             for (int i = 0; i < t.getMida(); i++) {
                if (t.movpossible(i)) return i;
            }
        }
        
        return millorCol;
    }

    private int minimax(Tauler t, int depth, int alpha, int beta, boolean myTurn) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}