package pt.ipb.dsys.sd.comum.protocolo;

import pt.ipb.dsys.sd.comum.Mensagem_peer;
import pt.ipb.dsys.sd.comum.ficheiros.FicheiroInfo;

import java.util.ArrayList;
import java.util.List;


public class File_listar_ficheiros implements Mensagem_peer {
    private List<FicheiroInfo> ficheiros = new ArrayList<>();
    private String CLUSTER_CLIENT;
    private String CLUSTER_SERVIDOR;

    public File_listar_ficheiros(String CLUSTER_CLIENT) {

        this.CLUSTER_CLIENT = CLUSTER_CLIENT;
    }

    public String getCLUSTER_CLIENT() {
        return CLUSTER_CLIENT;
    }

    public String getCLUSTER_SERVIDOR() {
        return CLUSTER_SERVIDOR;
    }

    public void setCLUSTER_SERVIDOR(String CLUSTER_SERVIDOR) {
        this.CLUSTER_SERVIDOR = CLUSTER_SERVIDOR;
    }

    public List<FicheiroInfo> getFicheiros() {
        return ficheiros;
    }

    public void setFicheiros(List<FicheiroInfo> ficheiros) {
        this.ficheiros = ficheiros;
    }
}
