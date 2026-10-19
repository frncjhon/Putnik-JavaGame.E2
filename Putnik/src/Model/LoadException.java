package Model;

public class LoadException extends Exception {

    private static final long serialVersionUID = 1L;
    public LoadException(String mensagem) {
        super(mensagem);
    }
    public LoadException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
