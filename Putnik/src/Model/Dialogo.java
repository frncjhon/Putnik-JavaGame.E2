package Model;

public class Dialogo{


        // Identificação da posição na narrativa
        private int idCapitulo;
        private int idCena;
        private int idDialogo;

        // Dados da fala
        private String personagem;
        private String texto;


        // ---------------------------------------------


        // Construtor somente com texto
        public Dialogo(String texto) {
            this(0, 0, 0, null, texto);
        }
        // Construtor com personagem e texto
        public Dialogo(String personagem, String texto) {
            this(0, 0, 0, personagem, texto);
        }
        // Construtor com IDs e somente texto
        public Dialogo(int idCapitulo, int idCena, int idDialogo,
                       String texto) {
            this(idCapitulo, idCena, idDialogo, null, texto);
        }
        // Construtor completo, com IDs, personagem e texto
        public Dialogo(int idCapitulo, int idCena, int idDialogo,
                       String personagem, String texto) {

            this.idCapitulo = idCapitulo;
            this.idCena = idCena;
            this.idDialogo = idDialogo;
            this.personagem = personagem;
            this.texto = texto;
        }


        // ---------------------------------------------


        // Métodos de acesso
        public int getIdCapitulo() {
            return idCapitulo;
        }

        public int getIdCena() {
            return idCena;
        }

        public int getIdDialogo() {
            return idDialogo;
        }

        public String getPersonagem() {
            return personagem;
        }

        public String getTexto() {
            return texto;
        }
}
