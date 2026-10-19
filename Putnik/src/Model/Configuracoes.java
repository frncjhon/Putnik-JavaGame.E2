package Model;

//-------------------------------------------------------------------------------------------

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

//-------------------------------------------------------------------------------------------


// Configurações globais.

public class Configuracoes {

    private final Path arquivo;

    private boolean limparTela = false;
    private boolean autosave = true;
    private String nomePadrao = "";

    public Configuracoes(Path arquivo) {
        this.arquivo = arquivo;
    }
    public boolean isLimparTela() {
        return limparTela;
    }
    public boolean isAutosave() {
        return autosave;
    }
    public String getNomePadrao() {
        return nomePadrao;
    }

    public void setLimparTela(boolean limparTela) { this.limparTela = limparTela; }
    public void setAutosave(boolean autosave) { this.autosave = autosave; }
    public void setNomePadrao(String nomePadrao) {
        this.nomePadrao = (nomePadrao == null) ? "" : nomePadrao;
    }

    //-------------------------------------------------------------------------------------------


    // Se o arquivo não existe, mantém os valores padrão.
    public void carregar() throws LoadException, DadosInvalidosException {
        if (!Files.exists(arquivo)) {
            return;
        }

        Properties props = new Properties();
        try (Reader leitor = Files.newBufferedReader(arquivo)) {
            props.load(leitor);
        } catch (IOException | IllegalArgumentException e) {
            throw new LoadException("Não foi possível ler as configurações.", e);
        }

        boolean novoLimpar = lerBoolean(props, "limparTela", limparTela);
        boolean novoAuto = lerBoolean(props, "autosave", autosave);
        String novoNome = props.getProperty("nomePadrao", "");

        limparTela = novoLimpar;
        autosave = novoAuto;
        setNomePadrao(novoNome);
    }

    // Salva as configurações atuais no arquivo.
    public void salvar() throws SaveException {
        Properties props = new Properties();
        props.setProperty("limparTela", String.valueOf(limparTela));
        props.setProperty("autosave", String.valueOf(autosave));
        props.setProperty("nomePadrao", nomePadrao);

        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
            try (Writer escritor = Files.newBufferedWriter(arquivo)) {
                props.store(escritor, "Configuracoes de ThePutnik");
            }
        } catch (IOException e) {
            throw new SaveException("Não foi possível gravar as configurações.", e);
        }
    }

    //-------------------------------------------------------------------------------------------

    // Converte uma propriedade textual em um valor booleano.
    private boolean lerBoolean(Properties props, String chave, boolean padrao)
            throws DadosInvalidosException {
        String valor = props.getProperty(chave);
        if (valor == null) {
            return padrao;
        }
        if (valor.equals("true")) {
            return true;
        }
        if (valor.equals("false")) {
            return false;
        }
        throw new DadosInvalidosException("Valor inválido para '" + chave + "': " + valor);
    }
}
