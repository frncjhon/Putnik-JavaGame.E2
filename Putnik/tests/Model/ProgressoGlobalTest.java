package Model;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ProgressoGlobalTest {

    @TempDir
    Path pasta;

    @Test
    void comecaSemFinaisEComExtraBloqueado() {
        ProgressoGlobal progresso = new ProgressoGlobal(pasta.resolve("progresso.properties"));

        assertTrue(progresso.getFinais().isEmpty());
        assertFalse(progresso.extraDesbloqueado());
    }

    @Test
    void registrarFinalDesbloqueiaOExtra() throws Exception {
        ProgressoGlobal progresso = new ProgressoGlobal(pasta.resolve("progresso.properties"));

        assertTrue(progresso.registrarFinal(2));
        assertFalse(progresso.registrarFinal(2)); // repetido
        assertTrue(progresso.finalAlcancado(2));
        assertFalse(progresso.finalAlcancado(1));
        assertTrue(progresso.extraDesbloqueado());
    }

    @Test
    void finalInexistenteEhRejeitado() {
        ProgressoGlobal progresso = new ProgressoGlobal(pasta.resolve("progresso.properties"));

        assertThrows(DadosInvalidosException.class, () -> progresso.registrarFinal(0));
        assertThrows(DadosInvalidosException.class, () -> progresso.registrarFinal(4));
    }

    @Test
    void progressoPersisteEntreExecucoes() throws Exception {
        Path arquivo = pasta.resolve("progresso.properties");

        ProgressoGlobal primeira = new ProgressoGlobal(arquivo);
        primeira.registrarFinal(1);
        primeira.registrarFinal(3);
        primeira.salvar();

        ProgressoGlobal segunda = new ProgressoGlobal(arquivo);
        segunda.carregar();

        assertTrue(segunda.finalAlcancado(1));
        assertFalse(segunda.finalAlcancado(2));
        assertTrue(segunda.finalAlcancado(3));
        assertTrue(segunda.extraDesbloqueado());
    }

    @Test
    void arquivoComDadosInvalidosLancaExcecao() throws Exception {
        Path arquivo = pasta.resolve("progresso.properties");

        Files.writeString(arquivo, "finais=9\n");
        assertThrows(DadosInvalidosException.class, () -> new ProgressoGlobal(arquivo).carregar());

        Files.writeString(arquivo, "finais=abc\n");
        assertThrows(DadosInvalidosException.class, () -> new ProgressoGlobal(arquivo).carregar());
    }
}
