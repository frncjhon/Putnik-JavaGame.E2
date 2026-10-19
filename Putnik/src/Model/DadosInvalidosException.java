package Model;

public class DadosInvalidosException extends Exception {

    private static final long serialVersionUID = 1L;

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }

    public DadosInvalidosException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}

// Classe que cria uma exceção personalizada para identificar erros causados por dados inválidos no jogo.