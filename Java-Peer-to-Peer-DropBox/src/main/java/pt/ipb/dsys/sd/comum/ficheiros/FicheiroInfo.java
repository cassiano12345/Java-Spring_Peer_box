package pt.ipb.dsys.sd.comum.ficheiros;

import java.io.Serializable;

public class FicheiroInfo implements Serializable {

    private String nome;
    private long tamanho;
    private int numeroChunks;

    public FicheiroInfo(String nome, long tamanho, int numeroChunks) {
        this.nome = nome;
        this.tamanho = tamanho;
        this.numeroChunks = numeroChunks;
    }

    public String getNome() {
        return nome;
    }

    public long getTamanho() {
        return tamanho;
    }

    public int getNumeroChunks() {
        return numeroChunks;
    }
}
