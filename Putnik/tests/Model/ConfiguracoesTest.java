package Model;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfiguracoesTest {

    @TempDir
    Path pasta;

    @Test
    void semArquivoMantemValoresPadrao() throws Exception {
        Configuracoes config = new Configuracoes(pasta.resolve("config.properties"));
        config.carregar();

        assertFalse(config.isLimparTela());
        assertTrue(config.isAutosave());
        assertEquals("", config.getNomePadrao());
    }

    @Test
    void preferenciasPersistemEntreExecucoes() throws Exception {
        Path arquivo = pasta.resolve("config.properties");

        Configuracoes primeira = new Configuracoes(arquivo);
        primeira.setLimparTela(true);
        primeira.setAutosave(false);
        primeira.setNomePadrao("José");
        primeira.salvar();

        Configuracoes segunda = new Configuracoes(arquivo);
        segunda.carregar();

        assertTrue(segunda.isLimparTela());
        assertFalse(segunda.isAutosave());
        assertEquals("José", segunda.getNomePadrao());
    }

    @Test
    void valorInvalidoLancaDadosInvalidosException() throws Exception {
        Path arquivo = pasta.resolve("config.properties");
        Files.writeString(arquivo, "autosave=talvez\n");

        Configuracoes config = new Configuracoes(arquivo);
        assertThrows(DadosInvalidosException.class, config::carregar);
        assertTrue(config.isAutosave()); // continua com o valor anterior
    }
}
