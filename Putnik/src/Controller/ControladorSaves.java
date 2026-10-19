package Controller;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import Model.DadosInvalidosException;
import Model.DadosSave;
import Model.EstadodeJogoAtual;
import Model.LoadException;
import Model.RepositorioSave;
import Model.SaveException;
import View.Hud;

// ------------------------------------------------------------------------------------------------

public class ControladorSaves {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final RepositorioSave repositorio;
    private final Hud hud;
    private final Entrada entrada;

    public ControladorSaves(RepositorioSave repositorio, Hud hud, Entrada entrada) {
        this.repositorio = repositorio;
        this.hud = hud;
        this.entrada = entrada;
    }

    //---------------------------------------------------------------------

    // Salvamento

    // Salva no slot 0 (autosave), sem interromper o jogo em caso de falha.
    public void autosalvar(EstadodeJogoAtual estado) {
        try {
            repositorio.salvar(RepositorioSave.SLOT_AUTOSAVE, new DadosSave(estado));
            hud.aviso("[Progresso salvo automaticamente]");
        } catch (SaveException e) {
            hud.erro("Autosave falhou: " + e.getMessage());
        }
    }

    //---------------------------------------------------------------------

    // Salvamento manual: o jogador escolhe um dos slots 1 a 3.
    public void salvarManual(EstadodeJogoAtual estado) {
        exibirSlots();
        hud.pedirSlot("salvar", 1);
        int slot = entrada.lerInteiro(1, RepositorioSave.TOTAL_SLOTS);

        if (repositorio.existe(slot)
                && !entrada.confirmar("O slot " + slot + " já tem um save. Sobrescrever?")) {
            hud.aviso("Salvamento cancelado.");
            return;
        }

        try {
            repositorio.salvar(slot, new DadosSave(estado));
            hud.sucesso("Jogo salvo no slot " + slot + ".");
        } catch (SaveException e) {
            hud.erro(e.getMessage());
        }
    }

    //---------------------------------------------------------------------

    // Carregamento

    // "Continuar": abre o save mais recente. Devolve null se não houver.
    public EstadodeJogoAtual continuar() {
        Optional<DadosSave> recente = repositorio.maisRecente();
        if (recente.isEmpty()) {
            hud.aviso("Nenhum save válido encontrado.");
            return null;
        }
        return recente.get().getEstado();
    }

    //---------------------------------------------------------------------

    // Tela de saves do menu principal. Devolve o estado carregado ou null.
    public EstadodeJogoAtual menuSaves() {
        while (true) {
            exibirSlots();
            hud.menuSlots();
            int opcao = entrada.lerInteiro(1, 3);

            if (opcao == 1) {
                EstadodeJogoAtual carregado = carregarEscolhido();
                if (carregado != null) {
                    return carregado;
                }
            } else if (opcao == 2) {
                excluirEscolhido();
            } else {
                return null;
            }
        }
    }

    //---------------------------------------------------------------------

    private EstadodeJogoAtual carregarEscolhido() {
        hud.pedirSlot("carregar (0 = autosave)", 0);
        int slot = entrada.lerInteiro(RepositorioSave.SLOT_AUTOSAVE, RepositorioSave.TOTAL_SLOTS);

        try {
            return repositorio.carregar(slot).getEstado();
        } catch (LoadException | DadosInvalidosException e) {
            hud.erro(e.getMessage());
            return null;
        }
    }

    //---------------------------------------------------------------------

    private void excluirEscolhido() {
        hud.pedirSlot("excluir (0 = autosave)", 0);
        int slot = entrada.lerInteiro(RepositorioSave.SLOT_AUTOSAVE, RepositorioSave.TOTAL_SLOTS);

        if (!repositorio.existe(slot)) {
            hud.aviso("O slot " + slot + " já está vazio.");
            return;
        }
        if (!entrada.confirmar("Excluir o slot " + slot + "? Isso não pode ser desfeito.")) {
            hud.aviso("Exclusão cancelada.");
            return;
        }

        try {
            repositorio.excluir(slot);
            hud.sucesso("Slot " + slot + " excluído.");
        } catch (SaveException e) {
            hud.erro(e.getMessage());
        }
    }

    //---------------------------------------------------------------------

    private void exibirSlots() {
        for (int slot = RepositorioSave.SLOT_AUTOSAVE; slot <= RepositorioSave.TOTAL_SLOTS; slot++) {
            String rotulo = (slot == RepositorioSave.SLOT_AUTOSAVE) ? "Autosave (0)" : "Slot " + slot;

            if (!repositorio.existe(slot)) {
                hud.exibirSlotVazio(rotulo);
                continue;
            }
            try {
                DadosSave dados = repositorio.carregar(slot);
                hud.exibirSlot(rotulo,
                        dados.getNomeJogador(),
                        dados.getDataHora().format(FORMATO),
                        dados.getLocal(),
                        dados.getResumo());
            } catch (LoadException | DadosInvalidosException e) {
                hud.exibirSlotCorrompido(rotulo);
            }
        }
    }
}
