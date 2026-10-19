package Model;

// Classe Atuante na aplicação dos efeitos conforme escolha do jogador.

//-------------------------------------------------------------------------------------------

public class AplicarEfeito {

    private final EstadodeJogoAtual estadoatual;

    public AplicarEfeito(EstadodeJogoAtual estadoatual) {
        this.estadoatual = estadoatual;
    }

    //-----------------------------------------------------------

    public void AplicarEfeitoStatusSanidade(double valor) {
        estadoatual.AlteraSanidade(valor);
    }

    public void AplicarEfeitoStatusLideranca(double valor) {
        estadoatual.AlteraLideranca(valor);
    }

    public void AplicarEfeitoStatusConfianca(double valor) {
        estadoatual.AlteraConfianca(valor);
    }

    // ----------------------------------------------------------

    // 1 = Chave de Mikhail, 2 = Símbolo de Elena.
    public void AplicarItem(int op) {
        if (op == 1) {
            estadoatual.AdicionarChaveMikhail();
        } else if (op == 2) {
            estadoatual.AdicionarSimboloElena();
        } else {
            throw new IllegalArgumentException("Item inexistente: " + op);
        }
    }

    // ----------------------------------------------------------

    public void AlterarAfinidadeIbis(double valor) {
        estadoatual.ModificarAfinidadeIbis(valor);
    }

    public void AlterarAfinidadeDeyv(double valor) {
        estadoatual.ModificarAfinidadeDeyv(valor);
    }
}
