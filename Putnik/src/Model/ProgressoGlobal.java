package Model;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

// Aqui fica salvo os dados GLOBAIS do jogo, de forma que satisfaz a exigência de salvar:
// Progresso, configurações e conquistas.

public class ProgressoGlobal {

    public static final int TOTAL_FINAIS = 3;

    private final Path arquivo;
    private final Set<Integer> finais = new TreeSet<>();

    public ProgressoGlobal(Path arquivo) {
        this.arquivo = arquivo;
    }

    // Devolve true se o final ainda não tinha sido alcançado.
    public boolean registrarFinal(int numero) throws DadosInvalidosException {
        if (numero < 1 || numero > TOTAL_FINAIS) {
            throw new DadosInvalidosException("Final inexistente: " + numero);
        }
        return finais.add(numero);
    }

    public boolean finalAlcancado(int numero) {
        return finais.contains(numero);
    }

    public Set<Integer> getFinais() {
        return Collections.unmodifiableSet(finais);
    }

    // O capítulo extra abre assim que o primeiro final é alcançado.
    public boolean extraDesbloqueado() {
        return !finais.isEmpty();
    }

    // -------------------------------

    public void carregar() throws LoadException, DadosInvalidosException {
        if (!Files.exists(arquivo)) {
            return;
        }

        Properties props = new Properties();
        try (Reader leitor = Files.newBufferedReader(arquivo)) {
            props.load(leitor);
        } catch (IOException | IllegalArgumentException e) {
            throw new LoadException("Não foi possível ler o progresso do jogo.", e);
        }

        Set<Integer> lidos = new TreeSet<>();
        String texto = props.getProperty("finais", "").trim();
        if (!texto.isEmpty()) {
            for (String parte : texto.split(",")) {
                try {
                    int numero = Integer.parseInt(parte.trim());
                    if (numero < 1 || numero > TOTAL_FINAIS) {
                        throw new DadosInvalidosException("Final inexistente no progresso: " + numero);
                    }
                    lidos.add(numero);
                } catch (NumberFormatException e) {
                    throw new DadosInvalidosException("Progresso inválido: " + parte, e);
                }
            }
        }

        finais.clear();
        finais.addAll(lidos);
    }

    public void salvar() throws SaveException {
        StringBuilder lista = new StringBuilder();
        for (int f : finais) {
            if (lista.length() > 0) {
                lista.append(",");
            }
            lista.append(f);
        }

        Properties props = new Properties();
        props.setProperty("finais", lista.toString());

        try {
            if (arquivo.getParent() != null) {
                Files.createDirectories(arquivo.getParent());
            }
            try (Writer escritor = Files.newBufferedWriter(arquivo)) {
                props.store(escritor, "Progresso global de ThePutnik");
            }
        } catch (IOException e) {
            throw new SaveException("Não foi possível gravar o progresso do jogo.", e);
        }
    }
}
