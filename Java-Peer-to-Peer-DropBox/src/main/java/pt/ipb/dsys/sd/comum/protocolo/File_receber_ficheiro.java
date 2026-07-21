package pt.ipb.dsys.sd.comum.protocolo;

import pt.ipb.dsys.sd.comum.Mensagem_peer;

public class File_receber_ficheiro implements Mensagem_peer {
    private String nome_ficheiro;
    private String sha256;
    private int numero;
    private int totalChunks;
    private byte[] dados;
    private String CLUSTER_CLIENT;
    private  String mensagem;



    public File_receber_ficheiro(String CLUSTER_CLIENT) {

        this.CLUSTER_CLIENT = CLUSTER_CLIENT;
    }


    public void setDados(byte[] dados) {
        this.dados = dados;
    }

    public byte[] getDados() {
        return dados;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCLUSTER_CLIENT() {
        return CLUSTER_CLIENT;
    }

    public String getNome_ficheiro() {
        return nome_ficheiro;
    }

    public void setNome_ficheiro(String nome_ficheiro) {
        this.nome_ficheiro = nome_ficheiro;
    }

}
