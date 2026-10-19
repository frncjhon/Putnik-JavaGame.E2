package Controller;

import java.util.Scanner;

import Model.Validacao;
import View.Hud;

// ------------------------------------------------------------------------------------------------
// Centraliza a leitura do teclado.

public class Entrada {

    private final Scanner scanner;
    private final Hud hud;
    private final Validacao validacao = new Validacao();

    public Entrada(Scanner scanner, Hud hud) {
        this.scanner = scanner;
        this.hud = hud;
    }

    public String lerLinha() {
        return scanner.nextLine();
    }

    // Repete até o jogador digitar um número entre min e max.
    public int lerInteiro(int min, int max) {
        while (true) {
            hud.PedirEntrada();
            int valor = validacao.parseInteiro(scanner.nextLine(), min, max);
            if (valor != -1) {
                return valor;
            }
            hud.AvisoOpçãoInexistente();
        }
    }


    //---------------------------------------------------------------------

    public boolean confirmar(String pergunta) {
        while (true) {
            hud.perguntaConfirmacao(pergunta);
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) {
                return true;
            }
            if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não")) {
                return false;
            }
            hud.avisoConfirmacaoInvalida();
        }
    }

    public void aguardarEnter() {
        hud.MensagemENTER();
        scanner.nextLine();
    }
}
