package Model;

import java.util.function.Consumer;
import java.util.function.Predicate;

// Classe responsável por gerenciar as alternativa de uma escolha.
public class Opcao {

    private final String texto;
    private final Predicate<EstadodeJogoAtual> requisito;
    private final String motivoBloqueio;
    private final Consumer<AplicarEfeito> efeito;

    public Opcao(String texto, Consumer<AplicarEfeito> efeito) {
        this(texto, null, null, efeito);
    }

    public Opcao(String texto,
                 Predicate<EstadodeJogoAtual> requisito,
                 String motivoBloqueio,
                 Consumer<AplicarEfeito> efeito) {
        this.texto = texto;
        this.requisito = requisito;
        this.motivoBloqueio = motivoBloqueio;
        this.efeito = efeito;
    }

    public String getTexto() { return texto; }

    public String getMotivoBloqueio() { return motivoBloqueio; }

    public boolean estaDisponivel(EstadodeJogoAtual estado) {
        return requisito == null || requisito.test(estado);
    }

    public void aplicar(AplicarEfeito efeitos) {
        if (efeito != null) {
            efeito.accept(efeitos);
        }
    }
}
