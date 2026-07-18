package pt.ipb.dsys.sd.peer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.comum.ConnectionManager;
import pt.ipb.dsys.sd.comum.ficheiros.FileChunk;
import pt.ipb.dsys.sd.comum.protocolo.File_enviar_chunk;

import java.util.List;


public class Funcionalidades_Peer {
    private static final Logger logger = LoggerFactory.getLogger(Main_peer.class);

    public void enviar_ficheiro(List<FileChunk> chunk, String cluster_user) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(cluster_user);
        for (FileChunk chunks : chunk) {
            File_enviar_chunk msg = new File_enviar_chunk(
                    chunks.getNome_ficheiro(),
                    chunks.getSha256(),
                    chunks.getNumero(),
                    chunk.size(),
                    cluster_user,
                    chunks.getDados());
            logger.info("Enviado chunk {}", chunks.getNumero());
            connection.sendToUser(msg);
        }
    }

}
