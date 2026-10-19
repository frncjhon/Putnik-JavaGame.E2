package Model;

import java.util.List;

// Classe utilizada para preparar as escolhas para exibição.

public class Escolha {

    private final int idCapitulo;
    private final int idCena;
    private final int idDialogo;
    private final String pergunta;
    private final List<Opcao> opcoes;

    public Escolha(int idCapitulo, int idCena, int idDialogo,
                   String pergunta, List<Opcao> opcoes) {
        this.idCapitulo = idCapitulo;
        this.idCena = idCena;
        this.idDialogo = idDialogo;
        this.pergunta = pergunta;
        this.opcoes = List.copyOf(opcoes);
    }

    // ------------------------------------------------------------------------

    public int getIdCapitulo() { return idCapitulo; }
    public int getIdCena() { return idCena; }
    public int getIdDialogo() { return idDialogo; }
    public String getPergunta() { return pergunta; }
    public List<Opcao> getOpcoes() { return opcoes; }

    // Identificador usado ao registrar a decisão: "cap.cena.dialogo".
    public String getId() {
        return idCapitulo + "." + idCena + "." + idDialogo;
    }

    public boolean ocorreEm(int capitulo, int cena, int dialogo) {
        return idCapitulo == capitulo && idCena == cena && idDialogo == dialogo;
    }
}
