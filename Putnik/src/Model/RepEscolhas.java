package Model;

import java.util.List;
import java.util.function.Consumer;

public class RepEscolhas {

    // Monta o efeito a partir dos números da tabela (0 = não altera).
    // Ordem: afinidade Ibis, afinidade Deyv, confiança, liderança, sanidade.
    private static Consumer<AplicarEfeito> ef(int ibis, int deyv, int conf, int lid, int san) {
        return e -> {
            if (ibis != 0) e.AlterarAfinidadeIbis(ibis);
            if (deyv != 0) e.AlterarAfinidadeDeyv(deyv);
            if (conf != 0) e.AplicarEfeitoStatusConfianca(conf);
            if (lid != 0) e.AplicarEfeitoStatusLideranca(lid);
            if (san != 0) e.AplicarEfeitoStatusSanidade(san);
        };
    }

    // Escolha(capitulo, cena, dialogo depois do qual aparece, pergunta, opcoes)
    // Opcao(texto da opção, texto do resultado, efeito)
    private static final List<Escolha> ESCOLHAS = List.of(

            // ===== CAPÍTULO 1 =====

            // Escolha 1 - depois da última fala da cena 1
            new Escolha(1, 1, 5,
                    "O que Serj deve fazer?",
                    List.of(
                            new Opcao("Examinar os escombros ao redor",
                                    "Serj examina os escombros ao redor e encontra uma pequena abertura entre os destroços.",
                                    ef(0, 0, 1, 0, 0)),
                            new Opcao("Levantar-se e procurar uma saída",
                                    "Serj se levanta e procura uma saída entre os escombros. Depois de alguns instantes, encontra uma passagem para fora.",
                                    ef(0, 0, 0, 1, 0)))),

            // Escolha 2 - depois da voz que chama Serj
            new Escolha(1, 2, 12,
                    "O que Serj deve fazer diante da voz?",
                    List.of(
                            new Opcao("Ignorar a voz",
                                    "Serj decide ignorar a voz e permanece ao lado de Ibis. Os dois continuam pelo corredor.",
                                    ef(5, 0, 2, 0, 1)),
                            new Opcao("Procurar a origem da voz",
                                    "Serj segue a voz por alguns instantes, mas não encontra ninguém. Percebendo que está se afastando de Ibis, ele decide voltar e continuar com o amigo.",
                                    ef(0, 0, -1, 2, -2)))),

            // Escolha 3 - depois de achar o mapa
            new Escolha(1, 3, 8,
                    "O que fazer com os registros?",
                    List.of(
                            new Opcao("Levar o mapa e seguir em frente",
                                    "Serj guarda o mapa e decide que o melhor caminho é seguir em frente.",
                                    ef(0, 0, 1, 2, 2)),
                            new Opcao("Vasculhar mais arquivos",
                                    "Serj continua procurando por mais informações, mas os arquivos restantes não revelam nada além dos mesmos registros. Sem encontrar algo novo, ele guarda o que encontrou e segue com Ibis.",
                                    ef(0, 0, 2, 1, 0)))),

            // Escolha 4 - depois de Serj desconfiar dos símbolos
            new Escolha(1, 4, 6,
                    "Quem deve decidir o próximo passo?",
                    List.of(
                            new Opcao("Serj assume a liderança",
                                    "Serj assume a frente do grupo e decide que os dois seguirão em direção à torre.",
                                    ef(2, 0, 0, 3, 2)),
                            new Opcao("Deixar Ibis decidir",
                                    "Ibis observa os símbolos por alguns segundos e escolhe o caminho da torre. Serj aceita a decisão e os dois continuam juntos.",
                                    ef(4, 0, 0, -1, -1)))),

            // ===== CAPÍTULO 2 =====

            // Escolha 5 - depois dos jornais
            new Escolha(2, 1, 7,
                    "O que Serj deve fazer com o jornal?",
                    List.of(
                            new Opcao("Mostrar a Ibis o jornal mais antigo",
                                    "Serj mostra o jornal para Ibis. Os dois percebem que os desaparecimentos acontecem há décadas.",
                                    ef(4, 0, 0, 0, 0)),
                            new Opcao("Esconder o jornal e continuar",
                                    "Serj guarda o jornal sem mostrar a Ibis. Os dois continuam pela avenida levando consigo apenas as informações que já possuem.",
                                    ef(0, 0, -1, 2, -3)))),

            // Escolha 6 - depois do último aviso de Mikhail
            new Escolha(2, 2, 12,
                    "Como Serj deve reagir ao aviso?",
                    List.of(
                            new Opcao("Confiar no aviso de Mikhail",
                                    "Serj decide levar o aviso de Mikhail a sério e guarda a chave com cuidado.",
                                    ef(0, 0, 3, 1, -1).andThen(e -> e.AplicarItem(1))),
                            new Opcao("Desconfiar de Mikhail",
                                    "Serj não aceita o aviso de imediato. Mesmo desconfiado, ele decide deixar a chave com Ibis.",
                                    ef(0, 0, -2, 2, 1)))),

            // Escolha 7 - depois de ouvir a própria voz ("Eu sei.")
            new Escolha(2, 3, 10,
                    "O que Serj deve fazer diante da porta?",
                    List.of(
                            new Opcao("Seguir Ibis e abandonar a porta",
                                    "Serj decide confiar no aviso e acompanha Ibis para fora da sala.",
                                    ef(5, 0, 0, 0, 2)),
                            new Opcao("Tentar abrir a porta mesmo assim",
                                    "Serj tenta usar a chave, mas as três batidas ecoam novamente pelo corredor. Percebendo que não há tempo para descobrir o que existe atrás da porta, ele recua e segue Ibis.",
                                    ef(0, 0, 1, 2, -2)))),

            // Escolha 8 - depois da chegada de Deyv
            new Escolha(2, 4, 12,
                    "Como Serj deve reagir à chegada de Deyv?",
                    List.of(
                            new Opcao("Aceitar a ajuda de Deyv",
                                    "Serj aceita a ajuda de Deyv. Os três passam a seguir juntos em direção à estação.",
                                    ef(0, 5, 2, 0, -1)),
                            new Opcao("Exigir respostas antes de seguir",
                                    "Serj exige explicações antes de aceitar Deyv no grupo. Depois de um breve silêncio, Deyv aponta para a direção da estação. Sem tempo para discutir, os três seguem juntos.",
                                    ef(0, -1, 1, 2, 2)))),

            // ===== CAPÍTULO 3 =====

            // Escolha 9 - quando o trem chega
            new Escolha(3, 1, 11,
                    "Quem deve entrar primeiro no trem?",
                    List.of(
                            new Opcao("Entrar com Ibis primeiro",
                                    "Serj entra no trem junto com Ibis e os dois escolhem um dos assentos iluminados.",
                                    ef(4, 0, 1, 0, -3)),
                            new Opcao("Mandar Deyv entrar primeiro",
                                    "Deyv entra no trem primeiro e espera por alguns segundos. Como nada acontece, ele faz um sinal para Serj e Ibis entrarem também.",
                                    ef(0, -2, 1, 2, 1)))),

            // Escolha 10 - depois das visões
            new Escolha(3, 2, 10,
                    "Como Serj deve reagir às visões?",
                    List.of(
                            new Opcao("Reforçar Ibis e afastá-lo da janela",
                                    "Serj afasta Ibis da janela e pede para que ele não continue olhando para as visões.",
                                    ef(4, 0, 0, 0, 3)),
                            new Opcao("Perguntar a Deyv sobre a visão",
                                    "Serj pergunta a Deyv sobre aquilo que viu. Deyv evita responder de imediato, mas a visão desaparece antes que ele consiga explicar.",
                                    ef(0, 4, 2, 0, 0)))),

            // Escolha 11 - depois de ler o nome riscado ("...MIKHAIL")
            new Escolha(3, 3, 9,
                    "O que Serj deve fazer com a descoberta?",
                    List.of(
                            new Opcao("Contar imediatamente a Ibis",
                                    "Serj conta imediatamente a Ibis sobre o nome de Mikhail. Os dois ficam em silêncio por alguns segundos.",
                                    ef(5, 0, 1, 0, 0)),
                            new Opcao("Guardar a informação por enquanto",
                                    "Serj decide não contar tudo naquele momento. Ele guarda a informação para si enquanto os três continuam procurando uma saída.",
                                    ef(0, 0, -2, 2, -2)))),

            // Escolha 12 - depois do aviso de Mikhail sobre a porta
            new Escolha(3, 4, 11,
                    "Em quem Serj deve acreditar?",
                    List.of(
                            new Opcao("Acreditar em Mikhail",
                                    "Serj decide confiar no aviso de Mikhail e permanece diante da passagem.",
                                    ef(0, 0, 3, 1, 0)),
                            new Opcao("Acreditar na suposta saída",
                                    "Serj se aproxima da porta, mas percebe tarde demais que algo está errado. A passagem desaparece diante deles, obrigando o grupo a continuar.",
                                    ef(-3, -3, 0, -2, -5)))),

            // ===== CAPÍTULO 4 =====

            // Escolha 13 - depois de Elena falar do quarto vermelho
            new Escolha(4, 1, 12,
                    "Como Serj deve conduzir o grupo?",
                    List.of(
                            new Opcao("Manter o grupo unido e seguir Elena",
                                    "Serj mantém Ibis e Deyv juntos e decide seguir Elena pelos corredores subterrâneos.",
                                    ef(2, 0, 1, 3, 3)),
                            new Opcao("Separar o grupo para investigar",
                                    "O grupo se separa por alguns instantes, mas os corredores parecem mudar de lugar. Percebendo o risco, Serj chama Ibis e Deyv e os três voltam a seguir juntos.",
                                    ef(0, 0, -2, -2, -2)))),

            // Escolha 14 - depois da explicação de Elena sobre o preço
            new Escolha(4, 2, 12,
                    "Como Serj deve buscar respostas?",
                    List.of(
                            new Opcao("Perguntar a Elena como evitar o sacrifício",
                                    "Serj pergunta a Elena como evitar o sacrifício. Ela explica que a resposta está ligada à própria prisão.",
                                    ef(0, 0, 3, 0, 0)),
                            new Opcao("Pressionar Deyv por respostas",
                                    "Serj pressiona Deyv por respostas. Depois de alguns segundos de silêncio, Deyv decide revelar apenas o necessário.",
                                    ef(-2, 4, 1, 0, 0)))),

            // Escolha 15 - depois da confissão de Deyv
            new Escolha(4, 3, 14,
                    "Como Serj deve reagir à confissão?",
                    List.of(
                            new Opcao("Perdoar Deyv e continuar ouvindo",
                                    "Serj decide ouvir Deyv até o fim. A revelação deixa os três ainda mais próximos da verdade sobre a prisão.",
                                    ef(0, 6, 3, 0, 0)),
                            new Opcao("Afastar Deyv do grupo",
                                    "Serj decide manter Deyv afastado por enquanto. Mesmo assim, Deyv continua seguindo o grupo de longe, pois também conhece o caminho para o centro.",
                                    ef(2, -5, 0, 3, -1)))),

            // Escolha 16 - antes da descida (o símbolo de Elena entra nas duas opções)
            new Escolha(4, 4, 12,
                    "Como Serj deve se preparar para descer?",
                    List.of(
                            new Opcao("Segurar Ibis e Deyv juntos",
                                    "Serj mantém Ibis e Deyv próximos e os três se preparam para descer juntos.",
                                    ef(3, 3, 0, 0, 1).andThen(e -> e.AplicarItem(2))),
                            new Opcao("Descer primeiro e assumir o risco",
                                    "Serj desce primeiro pela escada e verifica o caminho. Depois de perceber que o local está seguro, ele sinaliza para Ibis e Deyv descerem também.",
                                    ef(0, 0, 1, 4, -1).andThen(e -> e.AplicarItem(2))))),

            // ===== CAPÍTULO 5 =====
            // Cena 1: sem escolha.

            // Escolha 18 - depois da fala de Elena e Mikhail no Centro
            new Escolha(5, 2, 13,
                    "O que Serj deve fazer diante da voz?",
                    List.of(
                            new Opcao("Exigir outra solução",
                                    "Serj exige que exista outra possibilidade. A voz permanece em silêncio enquanto os três observam a passagem.",
                                    ef(0, 0, 2, 3, 0)),
                            new Opcao("Perguntar o que acontece com quem fica",
                                    "Serj pergunta o que acontecerá com aquele que permanecer. A resposta da voz deixa claro que a permanência é algo diferente da morte.",
                                    ef(0, 0, 3, 0, 0))))
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
