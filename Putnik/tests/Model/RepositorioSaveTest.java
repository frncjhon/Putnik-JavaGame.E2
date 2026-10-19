package Model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RepositorioSaveTest {

    @TempDir
    Path pasta;

    private RepositorioSave repositorio;

    @BeforeEach
    void preparar() {
        repositorio = new RepositorioSave(pasta);
    }

    private EstadodeJogoAtual estadoDeExemplo() {
        EstadodeJogoAtual estado = new EstadodeJogoAtual();
        estado.setNomeJogador("Ana");
        estado.definirPosicao(2, 3, 4);
        estado.AlteraLideranca(5);
        estado.AlteraSanidade(-2);
        estado.ModificarAfinidadeIbis(3);
        estado.AdicionarSimboloElena();
        estado.registrarDecisao("1.3.12:2");
        return estado;
    }

    @Test
    void salvarECarregarPreservaTodoOEstado() throws Exception {
        repositorio.salvar(1, new DadosSave(estadoDeExemplo()));

        DadosSave lido = repositorio.carregar(1);
        EstadodeJogoAtual estado = lido.getEstado();

        assertEquals("Ana", lido.getNomeJogador());
        assertEquals("Capítulo 2, Cena 3", lido.getLocal());
        assertEquals(2, estado.getIdcapitulo());
        assertEquals(3, estado.getIdcena());
        assertEquals(4, estado.getIddialogo());
        assertEquals(5, estado.getLideranca());
        assertEquals(8, estado.getSanidade());
        assertEquals(3, estado.getAfinidadeIbis());
        assertEquals(1, estado.getSimboloElena());
        assertTrue(estado.possuiDecisao("1.3.12:2"));
        assertNotNull(lido.getDataHora());
    }

    @Test
    void slotVazioLancaLoadException() {
        assertFalse(repositorio.existe(2));
        assertThrows(LoadException.class, () -> repositorio.carregar(2));
    }

    @Test
    void arquivoCorrompidoLancaLoadException() throws IOException {
        Files.write(pasta.resolve("slot1.dat"), new byte[]{1, 2, 3, 4, 5, 6, 7, 8});

        assertTrue(repositorio.existe(1));
        assertThrows(LoadException.class, () -> repositorio.carregar(1));
    }

    @Test
    void slotInexistenteEhRejeitado() {
        EstadodeJogoAtual estado = estadoDeExemplo();

        assertThrows(SaveException.class, () -> repositorio.salvar(4, new DadosSave(estado)));
        assertThrows(LoadException.class, () -> repositorio.carregar(-1));
        assertThrows(SaveException.class, () -> repositorio.excluir(9));
    }

    @Test
    void estadoInvalidoNaoEhGravado() {
        EstadodeJogoAtual estado = estadoDeExemplo();
        estado.definirPosicao(99, 1, 1);

        assertThrows(SaveException.class, () -> repositorio.salvar(1, new DadosSave(estado)));
        assertFalse(repositorio.existe(1));
    }

    @Test
    void sobrescreverSubstituiOSaveAnterior() throws Exception {
        EstadodeJogoAtual primeiro = estadoDeExemplo();
        primeiro.setNomeJogador("Primeiro");
        EstadodeJogoAtual segundo = estadoDeExemplo();
        segundo.setNomeJogador("Segundo");

        repositorio.salvar(3, new DadosSave(primeiro));
        repositorio.salvar(3, new DadosSave(segundo));

        assertEquals("Segundo", repositorio.carregar(3).getNomeJogador());
    }

    @Test
    void autosaveUsaOSlotZero() throws Exception {
        repositorio.salvar(RepositorioSave.SLOT_AUTOSAVE, new DadosSave(estadoDeExemplo()));

        assertTrue(repositorio.existe(0));
        assertEquals("Ana", repositorio.carregar(0).getNomeJogador());
    }

    @Test
    void excluirRemoveOSlot() throws Exception {
        repositorio.salvar(2, new DadosSave(estadoDeExemplo()));

        assertTrue(repositorio.excluir(2));
        assertFalse(repositorio.existe(2));
        assertFalse(repositorio.excluir(2));
    }

    @Test
    void maisRecenteEscolheOSaveMaisNovo() throws Exception {
        EstadodeJogoAtual antigo = estadoDeExemplo();
        antigo.setNomeJogador("Antigo");
        EstadodeJogoAtual novo = estadoDeExemplo();
        novo.setNomeJogador("Novo");

        repositorio.salvar(1, new DadosSave(antigo, LocalDateTime.of(2026, 10, 1, 10, 0)));
        repositorio.salvar(2, new DadosSave(novo, LocalDateTime.of(2026, 10, 5, 9, 30)));

        Optional<DadosSave> recente = repositorio.maisRecente();
        assertTrue(recente.isPresent());
        assertEquals("Novo", recente.get().getNomeJogador());
    }

    @Test
    void maisRecenteIgnoraSlotCorrompido() throws Exception {
        repositorio.salvar(1, new DadosSave(estadoDeExemplo(), LocalDateTime.of(2026, 10, 1, 10, 0)));
        Files.write(pasta.resolve("slot2.dat"), new byte[]{9, 9, 9});

        Optional<DadosSave> recente = repositorio.maisRecente();
        assertTrue(recente.isPresent());
        assertEquals("Ana", recente.get().getNomeJogador());
    }

    @Test
    void maisRecenteVazioQuandoNaoHaSaves() {
        assertTrue(repositorio.maisRecente().isEmpty());
    }
}
