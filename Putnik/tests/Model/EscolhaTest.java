package Model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EscolhaTest {

    @Test
    void buscaEncontraEscolhaCadastradaENullNosDemaisPontos() {
        Escolha escolha = RepEscolhas.buscar(1, 3, 12);

        assertNotNull(escolha);
        assertEquals("1.3.12", escolha.getId());
        assertNull(RepEscolhas.buscar(1, 1, 1));
    }

    @Test
    void opcaoComRequisitoFicaBloqueadaAteOEstadoSatisfazer() {
        Escolha escolha = RepEscolhas.buscar(1, 3, 12);
        Opcao exigeLideranca = escolha.getOpcoes().get(2);
        EstadodeJogoAtual estado = new EstadodeJogoAtual();

        assertFalse(exigeLideranca.estaDisponivel(estado));
        assertNotNull(exigeLideranca.getMotivoBloqueio());

        estado.AlteraLideranca(3);
        assertTrue(exigeLideranca.estaDisponivel(estado));
    }

    @Test
    void aplicarOpcaoAlteraOEstado() {
        Escolha escolha = RepEscolhas.buscar(1, 3, 12);
        EstadodeJogoAtual estado = new EstadodeJogoAtual();

        escolha.getOpcoes().get(1).aplicar(new AplicarEfeito(estado));

        assertEquals(2, estado.getAfinidadeIbis());
    }

    @Test
    void aplicarItemInexistenteLancaExcecao() {
        AplicarEfeito efeitos = new AplicarEfeito(new EstadodeJogoAtual());

        assertThrows(IllegalArgumentException.class, () -> efeitos.AplicarItem(7));
    }
}
