package pt.ipb.dsys.sd.sender;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.comum.ConnectionManager;
import pt.ipb.dsys.sd.comum.ficheiros.FicheiroInfo;
import pt.ipb.dsys.sd.comum.ficheiros.FileAssembler;
import pt.ipb.dsys.sd.comum.peerapi.PeerAPI;
import pt.ipb.dsys.sd.comum.protocolo.*;
import pt.ipb.dsys.sd.comum.ficheiros.FileChunk;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Funcionalidades_User implements PeerAPI {
    private static final Logger logger = LoggerFactory.getLogger(Main_sender.class);
    private String mensagem;
    private File_status_peers statusPeers;
    @Override
    public void enviarFicheiro(List<FileChunk> chunk) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        for (FileChunk chunks : chunk) {
            File_enviar_chunk msg = new File_enviar_chunk(
                    chunks.getNome_ficheiro(),
                    chunks.getSha256(),
                    chunks.getNumero(),
                    chunk.size(),
                    InetAddress.getLocalHost().getHostName(),
                    chunks.getDados());
            logger.info("Enviado chunk {}", chunks.getNumero());
            connection.sendToPeers(msg);
        }
    }

    @Override
    public void recuperarFicheiro(String pathname) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_pedir_ficheiro filePedirFicheiro = new File_pedir_ficheiro(InetAddress.getLocalHost().getHostName());
        filePedirFicheiro.setNome_ficheiro(pathname);
        connection.sendToPeers(filePedirFicheiro);
    }

    @Override
    public void apagarFicheiro(String pathname) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_apagar_ficheiro fileApagarFicheiro = new File_apagar_ficheiro(InetAddress.getLocalHost().getHostName());
        fileApagarFicheiro.setNome_ficheiro(pathname);
        connection.sendToPeers(fileApagarFicheiro);
    }

    @Override
    public void listarFicheiros() throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_listar_ficheiros fileInformacoesLocais = new File_listar_ficheiros(InetAddress.getLocalHost().getHostName());
        connection.sendToPeers(fileInformacoesLocais);
    }

    @Override
    public void recuperarMetadata(String pathname) throws Exception {

    }

    @Override
    public File_status_peers peersAtivos() throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());

        File_status_peers fileStatusPeers = new File_status_peers(InetAddress.getLocalHost().getHostName());
        fileStatusPeers.setMensagem("Ola, tem algum peer ativo?");
        connection.sendToPeers(fileStatusPeers);

        connection.setUserReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                statusPeers = msg.getObject();
            }
        });
        return statusPeers;
    }

    @Override
    public void informacoesLocais() throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_listar_ficheiros fileInformacoesLocais = new File_listar_ficheiros(InetAddress.getLocalHost().getHostName());
        connection.sendToPeers(fileInformacoesLocais);

    }
}
