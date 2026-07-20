package pt.ipb.dsys.sd.comum.peerapi;

import pt.ipb.dsys.sd.comum.ficheiros.FileChunk;
import pt.ipb.dsys.sd.comum.protocolo.File_status_peers;

import java.util.List;

public interface PeerAPI {

    void enviarFicheiro(List<FileChunk> chunks) throws Exception;

    void recuperarFicheiro(String pathname) throws Exception;

    void apagarFicheiro(String pathname) throws Exception;

    void listarFicheiros() throws Exception;

    void recuperarMetadata(String pathname) throws Exception;

    List<File_status_peers> peersAtivos() throws Exception;

    void informacoesLocais() throws Exception;
}
