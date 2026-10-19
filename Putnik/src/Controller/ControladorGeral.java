package Controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import Model.Configuracoes;
import Model.DadosInvalidosException;
import Model.EstadodeJogoAtual;
import Model.LoadException;
import Model.ProgressoGlobal;
import Model.RepositorioSave;
import Model.SaveException;
import Model.Validacao;
import View.Hud;
// -------------------------------

public class ControladorGeral {

    private static final String NOME_PADRAO_JOGADOR = "Jogador";

    // Declaração de atributos
    private final Hud hud;
    private final Entrada entrada;
    private final Validacao validacao;
    private final Configuracoes config;
    private final ProgressoGlobal progresso;
    private final ControladorSaves saves;
    private boolean loopmenu;

    // Os dados (saves, configurações e progresso) ficam na pasta "dados".
    public ControladorGeral() {
        this(Paths.get("dados"), new Scanner(System.in));
    }

    public ControladorGeral(Path pastaDados, Scanner scanner) {
        hud = new Hud();
        entrada = new Entrada(scanner, hud);
        validacao = new Validacao();
        loopmenu = true;

        config = new Configuracoes(pastaDados.resolve("config.properties"));
        progresso = new ProgressoGlobal(pastaDados.resolve("progresso.properties"));
        saves = new ControladorSaves(new RepositorioSave(pastaDados), hud, entrada);

        carregarPersistencia();
    } //---------------------------------------------------------------------

    // Lê configurações e progresso; se estiverem inválidos, avisa e segue com o padrão.
    private void carregarPersistencia() {
        try {
            config.carregar();
        } catch (LoadException | DadosInvalidosException e) {
            hud.erro("Configurações ignoradas: " + e.getMessage());
        }
        try {
            progresso.carregar();
        } catch (LoadException | DadosInvalidosException e) {
            hud.erro("Progresso ignorado: " + e.getMessage());
        }
    }

    // Ferramentas para exibição.
    public void SemiCleanTerminal() {
        hud.limparTerminal();
    }
    // -------------------------------

    // Inicia o jogo.
    public void startgame() {

        // Permite a volta ao menu.
        while (loopmenu) {

            hud.Menu();
            int opcao = entrada.lerInteiro(1, 8);

            switch (opcao) {
                case 1 -> novoJogo();
                case 2 -> continuarJogo();
                case 3 -> menuSaves();
                case 4 -> menuConquistas();
                case 5 -> menuConfiguracoes();
                case 6 -> {
                    SemiCleanTerminal();
                    hud.info();
                    entrada.aguardarEnter();
                    SemiCleanTerminal();
                }
                case 7 -> {
                    SemiCleanTerminal();
                    hud.Creditos();
                    entrada.aguardarEnter();
                    SemiCleanTerminal();
                }
                default -> {
                    SemiCleanTerminal();
                    loopmenu = false;
                }
            }
        }
    } // -------------------------

    // -------------------------------
    // Jogar

    private void novoJogo() {
        SemiCleanTerminal();

        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        estado.setNomeJogador(pedirNome());
        escolherPersonalidade(estado);

        config.setNomePadrao(estado.getNomeJogador());
        salvarConfiguracoes();

        SemiCleanTerminal();
        iniciarPartida(estado);
    }

    private void continuarJogo() {
        EstadodeJogoAtual estado = saves.continuar();
        if (estado != null) {
            SemiCleanTerminal();
            iniciarPartida(estado);
        }
    }

    private void menuSaves() {
        SemiCleanTerminal();
        EstadodeJogoAtual estado = saves.menuSaves();
        if (estado != null) {
            SemiCleanTerminal();
            iniciarPartida(estado);
        }
    }

    private void iniciarPartida(EstadodeJogoAtual estado) {
        ControladorNarrativa narrativa =
                new ControladorNarrativa(estado, entrada, hud, config, progresso, saves);
        narrativa.iniciarNarrativa();
        SemiCleanTerminal();
    }

    // Pede o nome até ser válido. ENTER vazio usa o nome padrão.
    private String pedirNome() {
        String padrao = config.getNomePadrao().isEmpty() ? NOME_PADRAO_JOGADOR : config.getNomePadrao();

        while (true) {
            hud.pedirNome(config.getNomePadrao());
            String texto = entrada.lerLinha().trim();

            if (texto.isEmpty()) {
                return padrao;
            }
            if (validacao.nomeValido(texto)) {
                return texto;
            }
            hud.avisoNomeInvalido(Validacao.TAMANHO_MAX_NOME);
        }
    }

    //---------------------------------------------------------------------

    // Personalidade inicial: 1, 2 ou 3. Qualquer outra entrada começa com status zerados.
    private void escolherPersonalidade(EstadodeJogoAtual estado) {
        hud.HudStarterStats();
        System.out.println();
        hud.PedirEntrada();
        String resposta = entrada.lerLinha().trim();

        if (resposta.equals("1")) {          // Carismático: Afinidade +6
            estado.ModificarAfinidadeIbis(6);
            estado.ModificarAfinidadeDeyv(6);
        } else if (resposta.equals("2")) {   // Corajoso: Confiança +3
            estado.AlteraConfianca(3);
        } else if (resposta.equals("3")) {   // Perfeccionista: Liderança +6
            estado.AlteraLideranca(6);
        }
    }

    // -------------------------------
    // Conquistas

    private void menuConquistas() {
        SemiCleanTerminal();

        boolean[] finais = new boolean[ProgressoGlobal.TOTAL_FINAIS];
        for (int i = 0; i < finais.length; i++) {
            finais[i] = progresso.finalAlcancado(i + 1);
        }

        hud.exibirConquistas(finais, progresso.extraDesbloqueado());

        if (progresso.extraDesbloqueado()) {
            hud.menuConquistasExtra();
            if (entrada.lerInteiro(1, 2) == 1) {
                SemiCleanTerminal();
                iniciarPartida(extraComNomeAtual());
            }
        } else {
            entrada.aguardarEnter();
        }
        SemiCleanTerminal();
    }


    //---------------------------------------------------------------------

    // O extra roda em um estado próprio, sem tocar nos saves.
    private EstadodeJogoAtual extraComNomeAtual() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        if (!config.getNomePadrao().isEmpty()) {
            estado.setNomeJogador(config.getNomePadrao());
        }
        estado.definirPosicao(7, 1, 1);
        return estado;
    }

    // -------------------------------
    // Configurações

    private void menuConfiguracoes() {
        SemiCleanTerminal();

        boolean voltar = false;
        while (!voltar) {
            hud.menuConfig(config.isLimparTela(), config.isAutosave(), config.getNomePadrao());
            int opcao = entrada.lerInteiro(1, 4);

            if (opcao == 1) {
                config.setLimparTela(!config.isLimparTela());
                salvarConfiguracoes();
            } else if (opcao == 2) {
                config.setAutosave(!config.isAutosave());
                salvarConfiguracoes();
            } else if (opcao == 3) {
                alterarNomePadrao();
            } else {
                voltar = true;
            }
        }
        SemiCleanTerminal();
    }

    //---------------------------------------------------------------------

    private void alterarNomePadrao() {
        hud.pedirNome(null);
        String texto = entrada.lerLinha().trim();

        if (texto.isEmpty()) {
            config.setNomePadrao("");
        } else if (validacao.nomeValido(texto)) {
            config.setNomePadrao(texto);
        } else {
            hud.avisoNomeInvalido(Validacao.TAMANHO_MAX_NOME);
            return;
        }
        salvarConfiguracoes();
    }


    //---------------------------------------------------------------------

    private void salvarConfiguracoes() {
        try {
            config.salvar();
        } catch (SaveException e) {
            hud.erro(e.getMessage());
        }
    }
}
