package Model;

public class SaveException extends Exception {

    private static final long serialVersionUID = 1L;

    public SaveException(String mensagem) {
        super(mensagem);
    }
    public SaveException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
