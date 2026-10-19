package Model;

public class Validacao {

    public static final int TAMANHO_MAX_NOME = 20;

    // Valida a opção do menu principal (1 a 8).
    public int ValidacaoMenu(int x) {
        return (x >= 1 && x <= 8) ? 1 : 0;
    }

    //---------------------------------

    // Converte texto em número dentro de [min, max]; devolve -1 se inválido.
    public int parseInteiro(String texto, int min, int max) {
        if (texto == null) {
            return -1;
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            return (valor >= min && valor <= max) ? valor : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Nome do jogador: 1 a 20 caracteres, sem caracteres de controle.
    public boolean nomeValido(String nome) {
        if (nome == null) {
            return false;
        }
        String limpo = nome.trim();
        if (limpo.isEmpty() || limpo.length() > TAMANHO_MAX_NOME) {
            return false;
        }
        for (char c : limpo.toCharArray()) {
            if (Character.isISOControl(c)) {
                return false;
            }
        }
        return true;
    }

    // Condição de GameOver: sanidade precisa ser positiva.
    public boolean ValidacaoSanidadePositiva(double x) {
        return x > 0;
    }
}
