package Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EstadodeJogoAtualTest {

    @Test
    void jogoNovoComecaComValoresIniciais() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();

        assertEquals(1, estado.getIdcapitulo());
        assertEquals(1, estado.getIdcena());
        assertEquals(1, estado.getIddialogo());
        assertEquals(10, estado.getSanidade());
        assertEquals(0, estado.getLideranca());
        assertEquals(0, estado.getConfianca());
        assertEquals(0, estado.getChaveMikhail());
        assertTrue(estado.getDecisoes().isEmpty());
        assertFalse(estado.estaConcluida());
    }

    @Test
    void atributosEAfinidadesPodemVariar() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();

        estado.AlteraSanidade(-3);
        estado.AlteraLideranca(2);
        estado.AlteraConfianca(1.5);
        estado.ModificarAfinidadeIbis(4);
        estado.ModificarAfinidadeDeyv(-1);

        assertEquals(7, estado.getSanidade());
        assertEquals(2, estado.getLideranca());
        assertEquals(1.5, estado.getConfianca());
        assertEquals(4, estado.getAfinidadeIbis());
        assertEquals(-1, estado.getAfinidadeDeyv());
    }

    @Test
    void itensEDecisoesSaoRegistrados() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();

        estado.AdicionarChaveMikhail();
        estado.registrarDecisao("1.3.12:2");

        assertEquals(1, estado.getChaveMikhail());
        assertEquals(0, estado.getSimboloElena());
        assertTrue(estado.possuiDecisao("1.3.12:2"));
        assertFalse(estado.possuiDecisao("1.3.12:1"));
    }

    @Test
    void encerrarNarrativaMarcaPartidaComoConcluida() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        estado.encerrarNarrativa();

        assertTrue(estado.estaConcluida());
        assertEquals("Partida concluída", estado.getDescricaoLocal());
    }

    @Test
    void descricaoDeLocalDependeDoCapitulo() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();

        estado.definirPosicao(3, 2, 1);
        assertEquals("Capítulo 3, Cena 2", estado.getDescricaoLocal());

        estado.definirPosicao(6, 2, 1);
        assertEquals("Final 2", estado.getDescricaoLocal());

        estado.definirPosicao(7, 1, 1);
        assertEquals("Conteúdo extra", estado.getDescricaoLocal());
    }

    @Test
    void validarAceitaEstadoInicial() {
        assertDoesNotThrow(() -> new EstadodeJogoAtual().validar());
    }

    @Test
    void validarRejeitaCapituloInexistente() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        estado.definirPosicao(99, 1, 1);

        assertThrows(DadosInvalidosException.class, estado::validar);
    }

    @Test
    void validarRejeitaDialogoForaDaCena() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        estado.definirPosicao(1, 1, 500);

        assertThrows(DadosInvalidosException.class, estado::validar);
    }

    @Test
    void validarRejeitaAtributoNaoNumerico() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        estado.AlteraSanidade(Double.NaN);

        assertThrows(DadosInvalidosException.class, estado::validar);
    }

    @Test
    void validarRejeitaNomeVazio() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        estado.setNomeJogador("  ");

        assertThrows(DadosInvalidosException.class, estado::validar);
    }
}
