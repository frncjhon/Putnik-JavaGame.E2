package View;


public class Hud {
    public void Menu() {
        System.out.println("""
                 ███████████  █████  █████ ███████████ ██████   █████ █████ █████   ████
                ▒▒███▒▒▒▒▒███▒▒███  ▒▒███ ▒█▒▒▒███▒▒▒█▒▒██████ ▒▒███ ▒▒███ ▒▒███   ███▒ 
                 ▒███    ▒███ ▒███   ▒███ ▒   ▒███  ▒  ▒███▒███ ▒███  ▒███  ▒███  ███   
                 ▒██████████  ▒███   ▒███     ▒███     ▒███▒▒███▒███  ▒███  ▒███████    
                 ▒███▒▒▒▒▒▒   ▒███   ▒███     ▒███     ▒███ ▒▒██████  ▒███  ▒███▒▒███   
                 ▒███         ▒███   ▒███     ▒███     ▒███  ▒▒█████  ▒███  ▒███ ▒▒███  
                 █████        ▒▒████████      █████    █████  ▒▒█████ █████ █████ ▒▒████
                ▒▒▒▒▒          ▒▒▒▒▒▒▒▒      ▒▒▒▒▒    ▒▒▒▒▒    ▒▒▒▒▒ ▒▒▒▒▒ ▒▒▒▒▒   ▒▒▒▒
                """);
        System.out.println(""" 
                               [1] - Novo Jogo
                               [2] - Continuar
                               [3] - Carregar / Gerenciar Saves
                               [4] - Conquistas
                               [5] - Configurações
                               [6] - Informações
                               [7] - Créditos
                               [8] - Fechar Jogo
                               ver.2.0
                """);
    }  // -------------------------------------------------------------------------------

    public void AvisoOpçãoInexistente() {
        System.out.println("""
                | Opção escolhida não existe, tente novamente!
                """);
    }


    public void PedirEntrada(){
        System.out.print("| Por favor, insira a opção desejada:");
    }

    public void MensagemENTER(){
        System.out.print("Pressione a tecla ENTER para continuar.");
    }

    public void VazioParaLimpeza(){
        System.out.println(" ");
    }

    // -------------------------------------------------------------------------------
    // Para opções:

    public void info(){
        System.out.println("""
                Sinopse: 
                
                Serj é um jovem fascinado por estéticas distintas, livros de
                H.P. Lovecraft, casos inexplicáveis e lendas da internet.
                Porém, tudo muda na madrugada de 20 de Maio de 2006, quando
                acorda e percebe que não está mais em sua casa, mas entre os
                destroços do que parece ser um prédio abandonado. Ao se
                levantar, percebe algo ainda mais estranho: não há qualquer
                som de seres vivos naquele lugar, nem mesmo de animais. Após
                conseguir sair dos destroços, Serj encontra Ibis, um conhecido
                de sua infância. Nenhum dos dois sabe como ou por que foram
                parar naquele lugar e, sem respostas, decidem seguir juntos
                em busca da verdade. Entretanto, conforme avançam, começam a
                descobrir que aquele lugar esconde algo muito maior e mais
                sombrio do que poderiam imaginar. Entre mistérios, escolhas,
                sacrifícios e acontecimentos que desafiam a própria realidade,
                Serj precisará descobrir até onde está disposto a ir para
                sobreviver e descobrir a verdade.
                
                Como jogar:
                
                Durante a jogatina será solicitado ao jogador para que selecione opções
                com entradas do teclado, que alteram o contexto narrativo.
                """);
    }

    public void Creditos(){
        System.out.println("""
                Este jogo e narrativa foi desenvolvido pela dupla de estudantes
                de engenharia de computação da Universidade Estadual de Feira de
                Santana na Bahia, Jhony Da França Silva e Cleuber Coelho.
                
                | Obrigado por jogar nosso jogo!
                
       
                """);
    }

    // -------------------------------------------------------------------------------


    public void exibirDialogo(String personagem, String texto) {
        if (personagem != null) {
            System.out.println(personagem + ": " + texto);
        } else {
            System.out.println(texto);
        }
    }

    // -------------------------------------------------------------------------------


    // Huds Narrativos

    public void HudStats(double afinidadedeyv,
                         double afinidadeibis,
                         double confianca,
                         double lideranca,
                         double sanidade){

        System.out.println("""
            ╔══════════════════════════════════╗
            ║          STATUS DO PERSONAGEM    ║
            ╠══════════════════════════════════╣""");
            System.out.println("║ Confiança:      " + confianca);
            System.out.println("║ Afinidade Ibis: " + afinidadeibis);
            System.out.println("║ Afinidade Deyv: " + afinidadedeyv);
            System.out.println("║ Liderança:      " + lideranca);
            System.out.println("║ Sanidade:       " + sanidade);
            System.out.println("╚══════════════════════════════════╝");
    }

    public void HudStarterStats(){
        System.out.println();
        System.out.println("       Escolha a personalidade inicial do seu personagem.");
        System.out.println("╔══════════════════╗   ╔══════════════════╗   ╔══════════════════╗");
        System.out.println("║   CARISMÁTICO    ║   ║     CORAJOSO     ║   ║  PERFECCIONISTA  ║");
        System.out.println("╠══════════════════╣   ╠══════════════════╣   ╠══════════════════╣");
        System.out.println("║                  ║   ║                  ║   ║                  ║");
        System.out.println("║   Afinidade +6   ║   ║   Confiança +3   ║   ║   Liderança +6   ║");
        System.out.println("║                  ║   ║                  ║   ║                  ║");
        System.out.println("║       [ 1 ]      ║   ║       [ 2 ]      ║   ║       [ 3 ]      ║");
        System.out.println("║                  ║   ║                  ║   ║                  ║");
        System.out.println("╚══════════════════╝   ╚══════════════════╝   ╚══════════════════╝");
        System.out.println("~ Se prefererir uma experiência mais desafiadora, selecione qualquer N- ~");
        System.out.println("               ~ para começar com status em 0!~");
    }

    // ------------------------------------------------------------------------------------------------

    // Saves, configurações, conquistas, escolhas e mensagens.

    public void limparTerminal() {
        for (int i = 0; i < 70; i++) {
            System.out.println(" ");
        }
    }

    public void pedirNome(String padrao) {
        System.out.println();
        if (padrao != null && !padrao.isEmpty()) {
            System.out.print("| Digite o nome do jogador [ENTER = " + padrao + "]: ");
        } else {
            System.out.print("| Digite o nome do jogador: ");
        }
    }

    public void avisoNomeInvalido(int maximo) {
        System.out.println("| Nome inválido. Use de 1 a " + maximo + " caracteres.");
    }

    public void mensagemEnterOuMenu() {
        System.out.print("[ENTER] continuar  |  [M] menu de pausa: ");
    }

    // ---- Mensagens gerais

    public void sucesso(String mensagem) {
        System.out.println("| [OK] " + mensagem);
    }

    public void erro(String mensagem) {
        System.out.println("| [ERRO] " + mensagem);
    }

    public void aviso(String mensagem) {
        System.out.println("| " + mensagem);
    }

    public void perguntaConfirmacao(String pergunta) {
        System.out.print("| " + pergunta + " (s/n): ");
    }

    public void avisoConfirmacaoInvalida() {
        System.out.println("| Responda com 's' (sim) ou 'n' (não).");
    }

    // ---- Saves

    public void exibirSlot(String rotulo, String nome, String dataHora, String local, String resumo) {
        System.out.println("╔═ " + rotulo);
        System.out.println("║ Jogador:  " + nome);
        System.out.println("║ Data:     " + dataHora);
        System.out.println("║ Local:    " + local);
        System.out.println("║ Resumo:   " + resumo);
        System.out.println("╚══════════════════════════════════════════");
    }

    public void exibirSlotVazio(String rotulo) {
        System.out.println("╔═ " + rotulo);
        System.out.println("║ (vazio)");
        System.out.println("╚══════════════════════════════════════════");
    }

    public void exibirSlotCorrompido(String rotulo) {
        System.out.println("╔═ " + rotulo);
        System.out.println("║ (save corrompido ou incompatível)");
        System.out.println("╚══════════════════════════════════════════");
    }

    public void menuSlots() {
        System.out.println("""
                [1] - Carregar partida
                [2] - Excluir save
                [3] - Voltar
                """);
    }

    public void pedirSlot(String acao, int min) {
        System.out.print("| Slot para " + acao + " (" + min + " a 3): ");
    }

    // ---- Pausa e configurações

    public void menuPausa() {
        System.out.println("""
                ═══════ PAUSA ═══════
                [1] - Salvar jogo
                [2] - Ver status
                [3] - Continuar
                [4] - Voltar ao menu principal
                """);
    }

    public void menuConfig(boolean limparTela, boolean autosave, String nomePadrao) {
        System.out.println("═══════ CONFIGURAÇÕES ═══════");
        System.out.println("[1] - Limpar a tela a cada fala:  " + (limparTela ? "SIM" : "NÃO"));
        System.out.println("[2] - Salvamento automático:      " + (autosave ? "SIM" : "NÃO"));
        System.out.println("[3] - Nome padrão do jogador:     "
                + ((nomePadrao == null || nomePadrao.isEmpty()) ? "(nenhum)" : nomePadrao));
        System.out.println("[4] - Voltar");
        System.out.println();
    }

    // ---- Conquistas

    public void exibirConquistas(boolean[] finais, boolean extra) {
        System.out.println("═══════ CONQUISTAS ═══════");
        int alcancados = 0;
        for (int i = 0; i < finais.length; i++) {
            System.out.println("Final " + (i + 1) + ": " + (finais[i] ? "[X] alcançado" : "[ ] bloqueado"));
            if (finais[i]) {
                alcancados++;
            }
        }
        System.out.println("Finais alcançados: " + alcancados + "/" + finais.length);
        System.out.println("Conteúdo extra: " + (extra ? "[X] desbloqueado" : "[ ] bloqueado (alcance um final)"));
        System.out.println();
    }

    public void menuConquistasExtra() {
        System.out.println("""
                [1] - Ver conteúdo extra
                [2] - Voltar
                """);
    }

    // ---- Escolhas

    public void exibirPergunta(String pergunta) {
        System.out.println();
        System.out.println("» " + pergunta);
    }

    public void exibirOpcao(int numero, String texto, String motivoBloqueio) {
        if (motivoBloqueio == null) {
            System.out.println("   [" + numero + "] " + texto);
        } else {
            System.out.println("   [" + numero + "] " + texto + "  (bloqueada: " + motivoBloqueio + ")");
        }
    }
}
