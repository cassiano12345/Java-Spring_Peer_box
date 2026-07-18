package pt.ipb.dsys.sd.comum.protocolo;

import pt.ipb.dsys.sd.comum.Mensagem_peer;

public class File_status_peers implements Mensagem_peer {
    private String CLUSTER_CLIENT;

    private String mensagem;

    public File_status_peers(String CLUSTER_CLIENT) {

        this.CLUSTER_CLIENT = CLUSTER_CLIENT;
    }

    public String getCLUSTER_CLIENT() {
        return CLUSTER_CLIENT;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
