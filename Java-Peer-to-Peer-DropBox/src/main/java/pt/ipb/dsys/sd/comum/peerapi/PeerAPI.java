package pt.ipb.dsys.sd.comum.peerapi;

import pt.ipb.dsys.sd.comum.protocolo.File_listar_ficheiros;
import pt.ipb.dsys.sd.comum.protocolo.File_status_peers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface PeerAPI {

    ArrayList<String> Share(String pathname) throws Exception; //Enviar ficheiros

    File Retrieve(String pathname) throws Exception; // Receber ficheiros

    ArrayList<String> Delete(String pathname) throws Exception; // Apagar ficheiros

    List<File_listar_ficheiros> List_Files() throws Exception; // Listar ficheiros

    List<File_status_peers> peersAtivos() throws Exception; // Obter informações de peers ativos

    // Fazer futuramente...

    void Obter_Metadata(String pathname) throws Exception;
}
