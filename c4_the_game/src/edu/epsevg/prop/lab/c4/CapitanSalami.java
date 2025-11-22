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

    // --- HEURÍSTICA (Igual que abans) ---
    private int avaluarTauler(Tauler t, int colorJugador) {
        int puntuacio = 0;
        int mida = t.getMida();
        int columnaCentral = mida / 2;

        for (int f = 0; f < mida; f++) {
            if (t.getColor(f, columnaCentral) == colorJugador) {
                puntuacio += 6;
            }
        }

        // Horitzontal
        for (int f = 0; f < mida; f++) {
            for (int c = 0; c < mida - 3; c++) {
                puntuacio += puntuarFinestra(t.getColor(f, c), t.getColor(f, c+1), t.getColor(f, c+2), t.getColor(f, c+3), colorJugador);
            }
        }
        // Vertical
        for (int c = 0; c < mida; c++) {
            for (int f = 0; f < mida - 3; f++) {
                puntuacio += puntuarFinestra(t.getColor(f, c), t.getColor(f+1, c), t.getColor(f+2, c), t.getColor(f+3, c), colorJugador);
            }
        }
        // Diagonal /
        for (int f = 0; f < mida - 3; f++) {
            for (int c = 0; c < mida - 3; c++) {
                puntuacio += puntuarFinestra(t.getColor(f, c), t.getColor(f+1, c+1), t.getColor(f+2, c+2), t.getColor(f+3, c+3), colorJugador);
            }
        }
        // Diagonal \
        for (int f = 0; f < mida - 3; f++) {
            for (int c = 3; c < mida; c++) {
                puntuacio += puntuarFinestra(t.getColor(f, c), t.getColor(f+1, c-1), t.getColor(f+2, c-2), t.getColor(f+3, c-3), colorJugador);
            }
        }
        return puntuacio;
    }

    private int puntuarFinestra(int c1, int c2, int c3, int c4, int colorPropi) {
        int meves = 0;
        int buides = 0;
        int rivals = 0;
        int colorRival = colorPropi * -1;
        int[] caselles = {c1, c2, c3, c4};
       
        for (int val : caselles) {
            if (val == colorPropi) meves++;
            else if (val == 0) buides++;
            else if (val == colorRival) rivals++;
        }

        if (meves == 4) return 10000;          
        if (meves == 3 && buides == 1) return 100;
        if (meves == 2 && buides == 2) return 10;  
        if (rivals == 3 && buides == 1) return -90;

        return 0;
    }
}
