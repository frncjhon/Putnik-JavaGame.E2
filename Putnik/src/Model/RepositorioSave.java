package Model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

// Classe responsavél pela persistência dos slots.
    // 0 = Autosave.
    // 1 a 3 = Saves Manuais
    // Cada SLOT é um arquivo "slotN.dat"

public class RepositorioSave {

    public static final int SLOT_AUTOSAVE = 0;
    public static final int TOTAL_SLOTS = 3;

    // Só permite desserializar as classes do próprio jogo e tipos básicos.
    private static final ObjectInputFilter FILTRO =
            ObjectInputFilter.Config.createFilter("Model.*;java.util.*;java.time.*;java.lang.*;!*");

    private final Path diretorio;

    public RepositorioSave(Path diretorio) {
        this.diretorio = diretorio;
    }

    private Path arquivo(int slot) {
        return diretorio.resolve("slot" + slot + ".dat");
    }

    private boolean slotExiste(int slot) {
        return slot >= SLOT_AUTOSAVE && slot <= TOTAL_SLOTS;
    }

    // -------------------------------

    public boolean existe(int slot) {
        return slotExiste(slot) && Files.exists(arquivo(slot));
    }

    public void salvar(int slot, DadosSave dados) throws SaveException {

        if (!slotExiste(slot)) {
            throw new SaveException("Slot inexistente: " + slot);
        }

        try {
            dados.validar();
        } catch (DadosInvalidosException e) {
            throw new SaveException("Dados inválidos, o save não foi gravado: " + e.getMessage(), e);
        }

        Path destino = arquivo(slot);
        Path temporario = diretorio.resolve("slot" + slot + ".tmp");

        try {
            Files.createDirectories(diretorio);

            // Grava em arquivo temporário e só depois substitui o save antigo,
            // para não corromper o slot se algo falhar no meio.
            try (OutputStream saida = Files.newOutputStream(temporario);
                 ObjectOutputStream objeto = new ObjectOutputStream(saida)) {
                objeto.writeObject(dados);
            }
            Files.move(temporario, destino, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            try {
                Files.deleteIfExists(temporario);
            } catch (IOException ignorada) {
                // nada a fazer
            }
            throw new SaveException("Não foi possível gravar o slot " + slot + ".", e);
        }
    }

    public DadosSave carregar(int slot) throws LoadException, DadosInvalidosException {

        if (!slotExiste(slot)) {
            throw new LoadException("Slot inexistente: " + slot);
        }
        if (!Files.exists(arquivo(slot))) {
            throw new LoadException("O slot " + slot + " está vazio.");
        }

        DadosSave dados;
        try (InputStream entrada = Files.newInputStream(arquivo(slot));
             ObjectInputStream objeto = new ObjectInputStream(entrada)) {

            objeto.setObjectInputFilter(FILTRO);
            Object lido = objeto.readObject();

            if (!(lido instanceof DadosSave)) {
                throw new DadosInvalidosException("O arquivo não contém um save do jogo.");
            }
            dados = (DadosSave) lido;

        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            throw new LoadException("Falha ao ler o slot " + slot + ": arquivo corrompido ou incompatível.", e);
        }

        dados.validar();
        return dados;
    }

    public boolean excluir(int slot) throws SaveException {
        if (!slotExiste(slot)) {
            throw new SaveException("Slot inexistente: " + slot);
        }
        try {
            return Files.deleteIfExists(arquivo(slot));
        } catch (IOException e) {
            throw new SaveException("Não foi possível excluir o slot " + slot + ".", e);
        }
    }

    // Save válido mais recente entre todos os slots (usado em "Continuar").
    public Optional<DadosSave> maisRecente() {
        DadosSave melhor = null;
        for (int slot = SLOT_AUTOSAVE; slot <= TOTAL_SLOTS; slot++) {
            if (!existe(slot)) {
                continue;
            }
            try {
                DadosSave atual = carregar(slot);
                if (melhor == null || atual.getDataHora().isAfter(melhor.getDataHora())) {
                    melhor = atual;
                }
            } catch (LoadException | DadosInvalidosException e) {
                // slot corrompido: ignora
            }
        }
        return Optional.ofNullable(melhor);
    }
}
