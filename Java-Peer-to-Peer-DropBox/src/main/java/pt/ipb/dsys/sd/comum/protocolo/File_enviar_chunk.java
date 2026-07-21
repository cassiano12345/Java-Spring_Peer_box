package pt.ipb.dsys.sd.comum.protocolo;

import pt.ipb.dsys.sd.comum.Mensagem_peer;

public class File_enviar_chunk implements Mensagem_peer {

    private String nome_ficheiro;
    private String sha256;
    private int numero;
    private int totalChunks;
    private byte[] dados;
    private String CLUSTER_CLIENT;
    private  String mensagem;



    public File_enviar_chunk(String nomeFicheiro, String sha256_, int numero_, int totalChunks_, String CLUSTER_CLIENT, byte[] dados_) {
        nome_ficheiro = nomeFicheiro;
        this.sha256 = sha256_;
        this.numero = numero_;
        this.totalChunks = totalChunks_;
        this.CLUSTER_CLIENT = CLUSTER_CLIENT;
        this.dados = dados_;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCLUSTER_CLIENT() {
        return CLUSTER_CLIENT;
    }

    public String getNome_ficheiro() {
        return nome_ficheiro;
    }

    public int getNumero() {
        return numero;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public byte[] getDados() {
        return dados;
    }

    public String getSha256() {
        return sha256;
    }
}
