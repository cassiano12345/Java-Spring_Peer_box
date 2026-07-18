package pt.ipb.dsys.sd.comum.peerapi;

import pt.ipb.dsys.sd.comum.ficheiros.FileChunk;
import java.util.List;

public interface PeerAPI {

    void enviarFicheiro(List<FileChunk> chunks) throws Exception;

    void recuperarFicheiro(String pathname) throws Exception;

    void apagarFicheiro(String pathname) throws Exception;

    void listarFicheiros() throws Exception;

    void recuperarMetadata(String pathname) throws Exception;

    String peersAtivos() throws Exception;

    void informacoesLocais() throws Exception;
}
