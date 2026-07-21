package pt.ipb.dsys.sd.web;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.comum.ConnectionManager;
import pt.ipb.dsys.sd.comum.peerapi.PeerAPI;
import pt.ipb.dsys.sd.comum.protocolo.*;
import pt.ipb.dsys.sd.comum.ficheiros.FileChunk;
import pt.ipb.dsys.sd.sender.Main_sender;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Funcionalidades_User_Web implements PeerAPI{
    private static final Logger logger = LoggerFactory.getLogger(Main_sender.class);
    private ArrayList<String> mensagem = new ArrayList<>();
    //private static List<File_status_peers> statusPeers = new ArrayList<>();

    @Override
    public ArrayList<String> enviarFicheiro(List<FileChunk> chunk) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        ArrayList<String> mensagem_enviar_ficheiro = new ArrayList<>();
        mensagem_enviar_ficheiro.clear();

        connection.setUserReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                if (msg.getObject() instanceof File_enviar_chunk) { // Verificando se o objeto recebido pelo user é do tipo File_enviar_chunk.
                    File_enviar_chunk chunk = (File_enviar_chunk) msg.getObject();
                    mensagem_enviar_ficheiro.add(chunk.getMensagem());
                }
            }
        });


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

        Thread.sleep(1000);
        return mensagem_enviar_ficheiro;
    }

    @Override
    public void recuperarFicheiro(String pathname) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_receber_ficheiro filePedirFicheiro = new File_receber_ficheiro(InetAddress.getLocalHost().getHostName());
        filePedirFicheiro.setNome_ficheiro(pathname);
        connection.sendToPeers(filePedirFicheiro);
    }

    @Override
    public ArrayList<String> apagarFicheiro(String pathname) throws Exception {

        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_apagar_ficheiro fileApagarFicheiro = new File_apagar_ficheiro(InetAddress.getLocalHost().getHostName());


        connection.setUserReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                if (msg.getObject() instanceof File_apagar_ficheiro apagar) {
                    mensagem.add(apagar.getResposta());
                }
            }
        });

        fileApagarFicheiro.setNome_ficheiro(pathname);
        connection.sendToPeers(fileApagarFicheiro);
        Thread.sleep(1000);
        return mensagem;
    }

    @Override
    public List<File_listar_ficheiros> listarFicheiros() throws Exception {
        List<File_listar_ficheiros> fileListarFicheiros = new ArrayList<>();
        ConnectionManager connection = new ConnectionManager();
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
        Thread.sleep(1000);

        return new ArrayList<>(fileListarFicheiros);
    }

    @Override
    public void recuperarMetadata(String pathname) throws Exception {

    }

    @Override
    public List<File_status_peers> peersAtivos() throws Exception {
        List<File_status_peers> statusPeers = new ArrayList<>();
        ConnectionManager connection = new ConnectionManager();
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

        Thread.sleep(5000);

        return new ArrayList<>(statusPeers);
    }

    @Override
    public void informacoesLocais() throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_listar_ficheiros fileInformacoesLocais = new File_listar_ficheiros(InetAddress.getLocalHost().getHostName());
        connection.sendToPeers(fileInformacoesLocais);

    }
}
