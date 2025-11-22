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
        
        return millorCol;
    }

    private int minimax(Tauler t, int depth, int alpha, int beta, boolean myTurn) {
        
        if (depth == 0 || !t.espotmoure()) {
            nodesExplorats++;
            return avaluarTauler(t, myColor);
        }

        // El color del rival = invers del meu
        int colorRival = myColor * -1;

        if (myTurn) { // Torn MAX (Nosaltres, usem myColor)
            int maxValor = Integer.MIN_VALUE;
            
            for (int col = 0; col < t.getMida(); ++col) {
                if (t.movpossible(col)) {
                    Tauler nouTauler = new Tauler(t);
                    nouTauler.afegeix(col, myColor); // Tirem nosaltres

                    if (nouTauler.solucio(col, myColor)) {
                        nodesExplorats++;
                        return 100000 + depth; 
                    }

                    // Cridem recursivament, ara li toca al rival (false)
                    int eval = minimax(nouTauler, depth - 1, alpha, beta, false);
                    
                    maxValor = Math.max(maxValor, eval);
                    alpha = Math.max(alpha, eval);
                    
                    if (beta <= alpha) break;
                }
            }
            return maxValor;

        } else { // Torn MIN (Rival, usem colorRival)
            int minValor = Integer.MAX_VALUE;
            
            for (int col = 0; col < t.getMida(); ++col) {
                if (t.movpossible(col)) {
                    Tauler nouTauler = new Tauler(t);
                    nouTauler.afegeix(col, colorRival); // Tira el rival

                    if (nouTauler.solucio(col, colorRival)) {
                        nodesExplorats++;
                        return -100000 - depth;
                    }

                    // Cridem recursivament, ara ens toca a nosaltres (true)
                    int eval = minimax(nouTauler, depth - 1, alpha, beta, true);
                    
                    minValor = Math.min(minValor, eval);
                    beta = Math.min(beta, eval);
                    
                    if (beta <= alpha) break;
                }
            }
            return minValor;
        }
    }

    private int avaluarTauler(Tauler t, int colorJugador) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}