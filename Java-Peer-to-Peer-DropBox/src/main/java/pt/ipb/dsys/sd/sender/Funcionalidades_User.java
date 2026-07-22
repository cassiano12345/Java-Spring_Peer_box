package pt.ipb.dsys.sd.sender;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.comum.ConnectionManager;
import pt.ipb.dsys.sd.comum.ficheiros.Arquivo;
import pt.ipb.dsys.sd.comum.peerapi.PeerAPI;
import pt.ipb.dsys.sd.comum.protocolo.*;
import pt.ipb.dsys.sd.comum.ficheiros.FileChunk;

import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Funcionalidades_User implements PeerAPI {
    private static final Logger logger = LoggerFactory.getLogger(Main_sender.class);
    private String mensagem;
    private static final ConnectionManager connection;

    static {
        try {
            connection = new ConnectionManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<String> Share(String path) throws Exception {
        File caminhos = new File(path);
        List<FileChunk> chunk = Arquivo.Criar_chunks(caminhos.getName(),caminhos.getAbsolutePath());
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
        return null;
    }

    @Override
    public File Retrieve(String pathname) throws Exception {
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_receber_ficheiro filePedirFicheiro = new File_receber_ficheiro(InetAddress.getLocalHost().getHostName());
        filePedirFicheiro.setNome_ficheiro(pathname);
        connection.sendToPeers(filePedirFicheiro);
        return null;
    }

    @Override
    public ArrayList<String> Delete(String pathname) throws Exception {
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_apagar_ficheiro fileApagarFicheiro = new File_apagar_ficheiro(InetAddress.getLocalHost().getHostName());
        fileApagarFicheiro.setNome_ficheiro(pathname);
        connection.sendToPeers(fileApagarFicheiro);
        return null;
    }

    @Override
    public List<File_listar_ficheiros> List_Files() throws Exception {
        List<File_listar_ficheiros> fileListarFicheiros = new ArrayList<>();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());

        fileListarFicheiros.clear();
        File_listar_ficheiros fileListarFicheiros1 = new File_listar_ficheiros(InetAddress.getLocalHost().getHostName());

        connection.setUserReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                if (msg.getObject() instanceof File_listar_ficheiros lista) {
                    fileListarFicheiros.add(lista);
                }
            }
        });

        connection.sendToPeers(fileListarFicheiros1);
        //Thread.sleep(2000);

        return new ArrayList<>(fileListarFicheiros);
    }

    @Override
    public List<File_status_peers> peersAtivos() throws Exception {
        List<File_status_peers> statusPeers = new ArrayList<>();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());

        statusPeers.clear();

        File_status_peers fileStatusPeers = new File_status_peers(InetAddress.getLocalHost().getHostName());

        connection.setUserReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                if (msg.getObject() instanceof File_status_peers peer) {
                    statusPeers.add(peer);
                }
            }
        });

        fileStatusPeers.setMensagem("Ola, tem algum peer ativo?");
        connection.sendToPeers(fileStatusPeers);

        Thread.sleep(2000);

        return new ArrayList<>(statusPeers);
    }


    // Fazer futuramente
    @Override
    public void Obter_Metadata(String pathname) throws Exception {

    }
}
