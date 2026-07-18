package pt.ipb.dsys.sd.comum.protocolo;

import pt.ipb.dsys.sd.comum.Mensagem_peer;

public class File_apagar_ficheiro implements Mensagem_peer {
    private String CLUSTER_CLIENT;

    private String nome_ficheiro;

    public File_apagar_ficheiro(String CLUSTER_CLIENT) {

        this.CLUSTER_CLIENT = CLUSTER_CLIENT;
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
