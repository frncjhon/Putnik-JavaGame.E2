package Model;

// Classe para salvar os dados em um SLOT.

import java.io.Serializable;
import java.time.LocalDateTime;

// Conteúdo em um SLOT: Metadados(Nome, hora...), Estado de jogo.

public class DadosSave implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nomeJogador;
    private final LocalDateTime dataHora;
    private final String resumo;
    private final EstadodeJogoAtual estado;

    public DadosSave(EstadodeJogoAtual estado) {
        this(estado, LocalDateTime.now());
    }

    public DadosSave(EstadodeJogoAtual estado, LocalDateTime dataHora) {
        this.estado = estado;
        this.dataHora = dataHora;
        this.nomeJogador = estado.getNomeJogador();
        this.resumo = estado.resumo();
    }

    public String getNomeJogador() { return nomeJogador; }
    public LocalDateTime getDataHora() { return dataHora; }
    public String getResumo() { return resumo; }
    public EstadodeJogoAtual getEstado() { return estado; }

    public String getLocal() {
        return estado.getDescricaoLocal();
    }

    public void validar() throws DadosInvalidosException {
        if (estado == null || dataHora == null || nomeJogador == null || resumo == null) {
            throw new DadosInvalidosException("Metadados do save incompletos.");
        }
        estado.validar();
    }
}
