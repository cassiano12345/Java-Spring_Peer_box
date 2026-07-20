package pt.ipb.dsys.sd.comum.protocolo;

import pt.ipb.dsys.sd.comum.Mensagem_peer;

public class File_status_peers implements Mensagem_peer {
    private String CLUSTER_CLIENT;
    private String ID_peer;
    private String IP;
    private String estado;
    private int num_ficheiros;
    private String mensagem;

    public File_status_peers(String CLUSTER_CLIENT) {

        this.CLUSTER_CLIENT = CLUSTER_CLIENT;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getNum_ficheiros() {
        return num_ficheiros;
    }

    public void setNum_ficheiros(int num_ficheiros) {
        this.num_ficheiros = num_ficheiros;
    }

    public String getID_peer() {
        return ID_peer;
    }

    public void setID_peer(String ID_peer) {
        this.ID_peer = ID_peer;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
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
