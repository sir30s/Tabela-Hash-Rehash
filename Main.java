import java.util.Random;

class TabelaHashRehash {
    static int tamanho;
    int[] tabela;
    private boolean vazia = true;
    double limite;
    int controle_quantidade = 0;
    int[] quantidade_colisoes;


    public TabelaHashRehash(int tamanho) {
        this.tamanho = tamanho;
        limite = tamanho * 0.9;
        tabela = new int[tamanho];
        quantidade_colisoes = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            tabela[i] = -1;
        }
    }

    public static int hash(int chave) {
        return chave % tamanho;
    }

    public static int rehash(int chave) {
        return (chave + 1) % tamanho;
    }

    void insere_hashing(int chave) {
        if (controle_quantidade >= limite) {
            return;
        }

        int i = hash(chave);
        int colisoes = 0;

        while (tabela[i] != -1) {
            colisoes++;
            i = rehash(i);
        }

        tabela[i] = chave;
        quantidade_colisoes[i] = colisoes;
        controle_quantidade++;
        vazia = false;

        if (controle_quantidade == limite) {
            System.out.println("90% das posições da tabela estão preenchidas!!!");
        }
    }

    int busca_hash(int chave) {
        int i = hash(chave);
        while (tabela[i] != chave && tabela[i] != -1) {
            i = rehash(i);
        }
        if (tabela[i] == -1) {
            return -1;
        } else {
            return i;
        }
    }

    int total_colisoes() {
        int total = 0;
        for (int i = 0; i < tamanho; i++) {
            total += quantidade_colisoes[i];
        }
        return total;
    }

    int[] top_tres_posicoes() {
        int[] top = new int[3];

        for (int k = 0; k < 3; k++) {
            int melhor = -1;

            for (int i = 0; i < tamanho; i++) {
                boolean escolhido = false;
                for (int j = 0; j < k; j++) {
                    if (top[j] == i) {
                        escolhido = true;
                    }
                }
                if (!escolhido && (melhor == -1 || quantidade_colisoes[i] > quantidade_colisoes[melhor])) {
                    melhor = i;
                }
            }
            top[k] = melhor;
        }

        return top;
    }

    void imprimir_posicoes_livres() {
        if (vazia) {
            System.out.println("a tabela está vazia");
        }
        System.out.println("posições livres:");
        for (int i = 0; i < tamanho; i++) {
            if (tabela[i] == -1) {
                System.out.print(i + " ");
            }
        }
        System.out.println("\n");
    }

    void imprimir_tabela(){
        for (int i = 0; i < tamanho; i++) {
            System.out.println("[" + i + "] -> " + tabela[i]);
        }
    }
}

class Main {
    public static void main(String[] args) {
        TabelaHashRehash tabelaRehash = new TabelaHashRehash(100);
        Random random = new Random(1090);

        for (int i = 0; i < 100; i++) {
            tabelaRehash.insere_hashing(random.nextInt(100001)); // mesma coisa do outro código?
        }

        System.out.println("quantidade de elementos inseridos: " + tabelaRehash.controle_quantidade);
        System.out.println("total de colisões: " + tabelaRehash.total_colisoes());

        int[] top = tabelaRehash.top_tres_posicoes();

        System.out.println("elementos que mais colidiram:");
        for (int k = 0; k < 3; k++) {
            int posicao = top[k];
            System.out.println("chave " + tabelaRehash.tabela[posicao]
                    + " -> posição " + posicao
                    + " -> colisões: " + tabelaRehash.quantidade_colisoes[posicao]);
        }

        tabelaRehash.imprimir_posicoes_livres();

        System.out.println("teste de busca:");
        for (int k = 0; k < 3; k++) {
            int chave = tabelaRehash.tabela[top[k]];
            int posicao = tabelaRehash.busca_hash(chave);
            if (posicao == -1) {
                System.out.println("chave " + chave + " não encontrada");
            } else {
                System.out.println("chave " + chave + " encontrada na posição " + posicao);
            }
        }
        System.out.println("\n--- IMPRIMINDO A LISTA INTEIRA ---\n");
        tabelaRehash.imprimir_tabela(); // debug
    }
}

// REFERÊNCIA:
// BRO CODE (goat) -> https://www.youtube.com/watch?v=FsfRsGFHuv4&t=320s (serve mais pra consulta teórica)
// JACOB SORBER -> https://www.youtube.com/watch?v=2Ti5yvumFTU (ideias pro código, mas em C)
// slides do professor
