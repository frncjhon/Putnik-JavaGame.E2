package Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ValidacaoTest {

    private final Validacao validacao = new Validacao();

    @Test
    void menuAceitaApenasOpcoesDeUmAOito() {
        assertEquals(1, validacao.ValidacaoMenu(1));
        assertEquals(1, validacao.ValidacaoMenu(8));
        assertEquals(0, validacao.ValidacaoMenu(0));
        assertEquals(0, validacao.ValidacaoMenu(9));
    }

    @Test
    void parseInteiroConverteDentroDoIntervalo() {
        assertEquals(3, validacao.parseInteiro("3", 1, 5));
        assertEquals(2, validacao.parseInteiro("  2  ", 1, 5));
    }

    @Test
    void parseInteiroRejeitaEntradasInvalidas() {
        assertEquals(-1, validacao.parseInteiro("abc", 1, 5));
        assertEquals(-1, validacao.parseInteiro("", 1, 5));
        assertEquals(-1, validacao.parseInteiro(null, 1, 5));
        assertEquals(-1, validacao.parseInteiro("6", 1, 5));
        assertEquals(-1, validacao.parseInteiro("0", 1, 5));
    }

    @Test
    void nomeDoJogadorPrecisaSerValido() {
        assertTrue(validacao.nomeValido("Ana"));
        assertTrue(validacao.nomeValido("  José  "));
        assertFalse(validacao.nomeValido(""));
        assertFalse(validacao.nomeValido("   "));
        assertFalse(validacao.nomeValido(null));
        assertFalse(validacao.nomeValido("a".repeat(21)));
        assertFalse(validacao.nomeValido("A\u0007na"));
    }

    @Test
    void sanidadePrecisaSerPositiva() {
        assertTrue(validacao.ValidacaoSanidadePositiva(1));
        assertFalse(validacao.ValidacaoSanidadePositiva(0));
        assertFalse(validacao.ValidacaoSanidadePositiva(-2));
    }
}
