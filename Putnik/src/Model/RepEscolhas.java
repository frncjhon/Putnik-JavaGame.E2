package Model;

import java.util.List;

// Repositório para escolhas durante a narrativa.
public class RepEscolhas {

    private static final List<Escolha> ESCOLHAS = List.of(

            // Capítulo 1, cena 3, depois de "Nós não estávamos sozinhos."
            new Escolha(1, 3, 12,
                    "As pegadas seguem para a saída. O que Serj faz?",
                    List.of(
                            new Opcao("Seguir as pegadas sozinho, na frente de Ibis.",
                                    e -> {
                                        e.AplicarEfeitoStatusConfianca(1);
                                        e.AplicarEfeitoStatusSanidade(-1);
                                    }),
                            new Opcao("Pedir que Ibis decida o caminho.",
                                    e -> e.AlterarAfinidadeIbis(2)),
                            new Opcao("Assumir o comando e definir o rumo do grupo.",
                                    estado -> estado.getLideranca() >= 3,
                                    "requer Liderança 3 ou mais",
                                    e -> {
                                        e.AplicarEfeitoStatusLideranca(1);
                                        e.AlterarAfinidadeIbis(-1);
                                    })
                    ))
    );

    // Devolve a escolha que ocorre depois do diálogo indicado, ou null.
    public static Escolha buscar(int capitulo, int cena, int dialogo) {
        for (Escolha escolha : ESCOLHAS) {
            if (escolha.ocorreEm(capitulo, cena, dialogo)) {
                return escolha;
            }
        }
        return null;
    }
}
