package Controller;

//---------------------------------------------------------------------

import java.util.List;

import Model.AplicarEfeito;
import Model.Configuracoes;
import Model.DadosInvalidosException;
import Model.Dialogo;
import Model.Escolha;
import Model.EstadodeJogoAtual;
import Model.Opcao;
import Model.ProgressoGlobal;
import Model.RepDialogo;
import Model.RepEscolhas;
import Model.SaveException;
import View.Hud;

// ----------------------------------------------

public class ControladorNarrativa {

    private final EstadodeJogoAtual estado;
    private final Entrada entrada;
    private final Hud hud;
    private final Configuracoes config;
    private final ProgressoGlobal progresso;
    private final ControladorSaves saves;
    private final AplicarEfeito efeitos;

    // ----------------------------------------------

    // Recebe a partida existente (nova ou carregada).
    public ControladorNarrativa(EstadodeJogoAtual estado,
                                Entrada entrada,
                                Hud hud,
                                Configuracoes config,
                                ProgressoGlobal progresso,
                                ControladorSaves saves) {

        this.estado = estado;
        this.entrada = entrada;
        this.hud = hud;
        this.config = config;
        this.progresso = progresso;
        this.saves = saves;
        // Os efeitos modificam a mesma partida da narrativa.
        this.efeitos = new AplicarEfeito(estado);
    }

    // ----------------------------------------------

    // Continua a narrativa a partir dos IDs presentes no estado.
    public void iniciarNarrativa() {

        if (estado.estaConcluida()) {
            hud.exibirDialogo(null, "Esta partida já foi concluída.");
            return;
        }
        executarNarrativa();
    }

    // Abre o conteúdo extra desbloqueado.
    public void iniciarConteudoExtra() {

        if (!progresso.extraDesbloqueado()) {
            hud.exibirDialogo(null, "O conteúdo extra ainda não foi desbloqueado.");
            return;
        }

        estado.definirPosicao(7, 1, 1);
        executarNarrativa();
    }

    // ----------------------------------------------

    // Loop principal da narrativa.
    private void executarNarrativa() {

        while (!estado.estaConcluida()) {

            Dialogo[] cenaAtual = RepDialogo.obterCena(estado.getIdcapitulo(), estado.getIdcena());
            Dialogo dialogoAtual = obterDialogoAtual(cenaAtual);

            if (dialogoAtual == null) {
                hud.erro("Posição narrativa inválida. Voltando ao menu.");
                return;
            }

            hud.exibirDialogo(dialogoAtual.getPersonagem(), dialogoAtual.getTexto());

            // Se houver escolha depois desta fala, o jogador decide agora.
            processarEscolha(dialogoAtual);

            // Verifica se esta era a última fala da cena.
            boolean fimDeCena = estado.getIddialogo() == cenaAtual.length;

            // Atualiza os IDs para a próxima fala.
            boolean continuar = avancarPosicao(cenaAtual);

            if (!continuar) {
                entrada.aguardarEnter();
                return;
            }

            // Autosave no fim de cada cena (inclusive ao entrar em um final).
            if (fimDeCena && config.isAutosave() && estado.getIdcapitulo() <= 6) {
                saves.autosalvar(estado);
            }

            if (aguardarProximaFala()) {
                return; // jogador escolheu voltar ao menu principal
            }
        }
    }

    //---------------------------------------------------------------------

    // ENTER segue a história; "M" abre o menu de pausa.
    // Devolve true se o jogador pediu para sair para o menu principal.
    private boolean aguardarProximaFala() {
        hud.mensagemEnterOuMenu();
        String resposta = entrada.lerLinha().trim();

        if (resposta.equalsIgnoreCase("m")) {
            if (menuPausa()) {
                return true;
            }
        }

        if (config.isLimparTela()) {
            hud.limparTerminal();
        }
        return false;
    }

    // true = voltar ao menu principal.
    private boolean menuPausa() {
        while (true) {
            hud.menuPausa();
            int opcao = entrada.lerInteiro(1, 4);

            if (opcao == 1) {
                saves.salvarManual(estado);
            } else if (opcao == 2) {
                exibirStatus();
                entrada.aguardarEnter();
            } else if (opcao == 3) {
                return false;
            } else if (entrada.confirmar("Sair para o menu principal? O progresso não salvo será perdido.")) {
                return true;
            }
        }
    }

    // ----------------------------------------------

    private void processarEscolha(Dialogo dialogo) {

        Escolha escolha = RepEscolhas.buscar(
                dialogo.getIdCapitulo(), dialogo.getIdCena(), dialogo.getIdDialogo());
        if (escolha == null) {
            return;
        }

        List<Opcao> opcoes = escolha.getOpcoes();

        hud.exibirPergunta(escolha.getPergunta());
        for (int i = 0; i < opcoes.size(); i++) {
            Opcao opcao = opcoes.get(i);
            String bloqueio = opcao.estaDisponivel(estado) ? null : opcao.getMotivoBloqueio();
            hud.exibirOpcao(i + 1, opcao.getTexto(), bloqueio);
        }

        int escolhida;
        while (true) {
            escolhida = entrada.lerInteiro(1, opcoes.size());
            Opcao opcao = opcoes.get(escolhida - 1);
            if (opcao.estaDisponivel(estado)) {
                break;
            }
            hud.aviso("Essa opção está bloqueada: " + opcao.getMotivoBloqueio() + ".");
        }

        opcoes.get(escolhida - 1).aplicar(efeitos);
        estado.registrarDecisao(escolha.getId() + ":" + escolhida);
    }

    // Localiza o diálogo que deve ser apresentado.
    private Dialogo obterDialogoAtual(Dialogo[] cena) {

        if (cena == null) {
            return null;
        }

        int indice = estado.getIddialogo() - 1;

        if (indice < 0 || indice >= cena.length) {
            return null;
        }

        Dialogo dialogo = cena[indice];

        // Confere se o diálogo corresponde aos IDs do estado.
        if (dialogo.getIdCapitulo() != estado.getIdcapitulo()
                || dialogo.getIdCena() != estado.getIdcena()
                || dialogo.getIdDialogo() != estado.getIddialogo()) {
            return null;
        }

        return dialogo;
    }

    // Avança a posição narrativa.
    private boolean avancarPosicao(Dialogo[] cenaAtual) {

        int capitulo = estado.getIdcapitulo();
        int cena = estado.getIdcena();
        int dialogo = estado.getIddialogo();

        // Ainda existem falas nesta cena.
        if (dialogo < cenaAtual.length) {
            estado.definirPosicao(capitulo, cena, dialogo + 1);
            return true;
        }

        // Uma das três rotas finais terminou.
        if (capitulo == 6) {
            registrarFinal(cena);
            estado.encerrarNarrativa();
            return false;
        }

        // O conteúdo extra terminou.
        if (capitulo == 7) {
            estado.encerrarNarrativa();
            return false;
        }

        // Próxima cena do capítulo.
        if (cena < 4) {
            estado.definirPosicao(capitulo, cena + 1, 1);
            return true;
        }

        // Próximo capítulo.
        if (capitulo < 5) {
            estado.definirPosicao(capitulo + 1, 1, 1);
            return true;
        }

        // Fim do capítulo 5: escolher o final.
        estado.definirPosicao(6, determinarFinal(), 1);
        return true;
    }

    // Registra o final no progresso global e grava em disco.
    private void registrarFinal(int numeroFinal) {
        try {
            boolean inedito = progresso.registrarFinal(numeroFinal);
            progresso.salvar();
            if (inedito) {
                hud.sucesso("Final " + numeroFinal + " alcançado! Veja em Conquistas.");
            }
        } catch (DadosInvalidosException | SaveException e) {
            hud.erro(e.getMessage());
        }
    }

    // ------------------------------------------------------------------------------------------------

    // Finais
    private int determinarFinal() {

        if (estado.getAfinidadeIbis() > estado.getAfinidadeDeyv()
                && estado.getLideranca() > estado.getConfianca()
                && estado.getLideranca() > estado.getSanidade()) {

            return 1;
        }

        if (estado.getAfinidadeDeyv() > estado.getAfinidadeIbis()
                && estado.getConfianca() > estado.getLideranca()
                && estado.getConfianca() > estado.getSanidade()) {

            return 2;
        }

        // Final padrão provisório.
        return 3;
    }

    private void exibirStatus() {
        hud.HudStats(estado.getAfinidadeDeyv(), estado.getAfinidadeIbis(),
                estado.getConfianca(), estado.getLideranca(), estado.getSanidade());
    }
}
