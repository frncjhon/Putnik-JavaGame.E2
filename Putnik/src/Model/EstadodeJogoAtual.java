package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Aqui é o estado de jogo que controla a narrativa ATUAL, podendo ser mudado conforme
// escolha de Slots, alterando diretamente a progressão da Narrativa.

public class EstadodeJogoAtual implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nomeJogador;

    // Posição narrativa (para Load).
    private int idcapitulo;
    private int idcena;
    private int iddialogo;

    // Atributos do protagonista Serj.
    private double lideranca;
    private double confianca;
    private double sanidade;

    // Afinidade com NPCs.
    private double afinidadeIbis;
    private double afinidadeDeyv;

    // Inventário (0 = não possui, 1 = possui).
    private int chaveMikhail;
    private int simboloElena;

    // Decisões tomadas, no formato "cap.cena.dialogo:opcao".
    private final List<String> decisoes = new ArrayList<>();

    // -------------------------------

    // Valores de um jogo novo.
    public EstadodeJogoAtual() {
        nomeJogador = "Jogador";

        sanidade = 10;
        lideranca = 0;
        confianca = 0;
        afinidadeIbis = 0;
        afinidadeDeyv = 0;

        chaveMikhail = 0;
        simboloElena = 0;

        idcapitulo = 1;
        idcena = 1;
        iddialogo = 1;
    }

    // -------------------------------
    // Leitura

    public String getNomeJogador() { return nomeJogador; }
    public int getIdcapitulo() { return idcapitulo; }
    public int getIdcena() { return idcena; }
    public int getIddialogo() { return iddialogo; }
    public double getLideranca() { return lideranca; }
    public double getConfianca() { return confianca; }
    public double getSanidade() { return sanidade; }
    public double getAfinidadeIbis() { return afinidadeIbis; }
    public double getAfinidadeDeyv() { return afinidadeDeyv; }
    public int getChaveMikhail() { return chaveMikhail; }
    public int getSimboloElena() { return simboloElena; }

    public List<String> getDecisoes() {
        return Collections.unmodifiableList(decisoes);
    }

    // -------------------------------
    // Escrita

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public void definirPosicao(int capitulo, int cena, int dialogo) {
        this.idcapitulo = capitulo;
        this.idcena = cena;
        this.iddialogo = dialogo;
    }

    // Marca a partida como concluída (posição 0/0/0).
    public void encerrarNarrativa() {
        definirPosicao(0, 0, 0);
    }

    public boolean estaConcluida() {
        return idcapitulo == 0;
    }

    public void registrarDecisao(String decisao) {
        decisoes.add(decisao);
    }

    public boolean possuiDecisao(String decisao) {
        return decisoes.contains(decisao);
    }

    // Status do protagonista.
    public void AlteraSanidade(double valor) { sanidade += valor; }
    public void AlteraLideranca(double valor) { lideranca += valor; }
    public void AlteraConfianca(double valor) { confianca += valor; }

    // Afinidade com NPCs.
    public void ModificarAfinidadeIbis(double valor) { afinidadeIbis += valor; }
    public void ModificarAfinidadeDeyv(double valor) { afinidadeDeyv += valor; }

    // Itens.
    public void AdicionarChaveMikhail() { chaveMikhail = 1; }
    public void AdicionarSimboloElena() { simboloElena = 1; }

    // -------------------------------
    // Informações para o menu de saves

    public String getDescricaoLocal() {
        if (idcapitulo == 0) {
            return "Partida concluída";
        }
        if (idcapitulo == 6) {
            return "Final " + idcena;
        }
        if (idcapitulo == 7) {
            return "Conteúdo extra";
        }
        return "Capítulo " + idcapitulo + ", Cena " + idcena;
    }

    public String resumo() {
        return getDescricaoLocal()
                + " | Sanidade " + String.format("%.1f", sanidade)
                + " | Decisões: " + decisoes.size();
    }

    // -------------------------------

    // Valida os dados.
    public void validar() throws DadosInvalidosException {

        if (nomeJogador == null || nomeJogador.isBlank()) {
            throw new DadosInvalidosException("Nome do jogador ausente no save.");
        }

        if (decisoes == null) {
            throw new DadosInvalidosException("Lista de decisões ausente no save.");
        }
        for (String d : decisoes) {
            if (d == null) {
                throw new DadosInvalidosException("Decisão inválida no save.");
            }
        }

        double[] numeros = {lideranca, confianca, sanidade, afinidadeIbis, afinidadeDeyv};
        for (double n : numeros) {
            if (!Double.isFinite(n)) {
                throw new DadosInvalidosException("Atributo numérico inválido no save.");
            }
        }

        if ((chaveMikhail != 0 && chaveMikhail != 1)
                || (simboloElena != 0 && simboloElena != 1)) {
            throw new DadosInvalidosException("Inventário inválido no save.");
        }

        Dialogo[] cena = RepDialogo.obterCena(idcapitulo, idcena);
        if (cena == null) {
            throw new DadosInvalidosException(
                    "Posição inexistente: capítulo " + idcapitulo + ", cena " + idcena + ".");
        }
        if (iddialogo < 1 || iddialogo > cena.length) {
            throw new DadosInvalidosException(
                    "Diálogo " + iddialogo + " inexistente na cena " + idcena
                            + " do capítulo " + idcapitulo + ".");
        }
    }
}
