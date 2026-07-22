package pt.ipb.dsys.sd.web;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.comum.ConnectionManager;
import pt.ipb.dsys.sd.comum.ficheiros.Arquivo;
import pt.ipb.dsys.sd.comum.ficheiros.FileAssembler;
import pt.ipb.dsys.sd.comum.peerapi.PeerAPI;
import pt.ipb.dsys.sd.comum.protocolo.*;
import pt.ipb.dsys.sd.comum.ficheiros.FileChunk;
import pt.ipb.dsys.sd.sender.Main_sender;


import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Funcionalidades_User_Web implements PeerAPI{
    private static final Logger logger = LoggerFactory.getLogger(Main_sender.class);
    private ArrayList<String> mensagem = new ArrayList<>();
    Map<String, Map<Integer, File_receber_ficheiro>> chunksList = new HashMap<>(); // Criar um objeto map do tipo File_enviar_chunk para receber os chunks.
    File ficheiro_final;

    @Override
    public ArrayList<String> Share(String nome) throws Exception {
        File caminhos = new File("FICHEIROS_PEERS_USER/" + nome);
        List<FileChunk> chunk = Arquivo.Criar_chunks(caminhos.getName(),caminhos.getAbsolutePath());

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
        caminhos.delete();
        return mensagem_enviar_ficheiro;
    }

    @Override
    public File Retrieve(String pathname) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        File_receber_ficheiro filePedirFicheiro = new File_receber_ficheiro(InetAddress.getLocalHost().getHostName());

        connection.setUserReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                if (msg.getObject() instanceof File_receber_ficheiro) { // Verificando se o objeto recebido pelo user é do tipo File_enviar_chunk.
                    File_receber_ficheiro chunk = (File_receber_ficheiro) msg.getObject();
                    chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>()).put(chunk.getNumero(), chunk); // Passando o chunk para o Map onde primeiro cria o map com a SHA256, dps cria um map com o num do chunk, e dentro guarda o chunk recebido.
                    logger.info("Recebido chunk {}", chunk.getNumero());
                    if (chunksList.get(chunk.getSha256()).size() == chunk.getTotalChunks()) { // Verificar se o numero de chunks para o determinado SHA256 é igual ao total de chunks que deve receber
                        File pasta = new File("FICHEIROS_PEERS_USER"); // Criar a pasta para guardar os ficheiros.
                        if (!pasta.exists()) {
                            pasta.mkdirs();
                        }
                        File ficheiro = new File("FICHEIROS_PEERS_USER/" + chunk.getNome_ficheiro());
                        setFicheiro_final(ficheiro);
                        if (!ficheiro.exists()){
                            Map<Integer, File_receber_ficheiro> chunksRecebidos = chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>());
                            try {
                                FileAssembler.reconstruirFicheiro_receber(chunksRecebidos,"FICHEIROS_PEERS_USER/" + chunk.getNome_ficheiro()); // Reconstruir o ficheiro e guardar na pasta downloads
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            //janela.mostrarAlerta("Ficheiro recebido!");
                            chunksList.remove(chunk.getSha256());
                        }else{
                            chunksList.remove(chunk.getSha256());

                            //janela.adicionarLog("O ficheiro ja existe no diretorio!");
                            //janela.mostrarAlerta("O ficheiro ja existe no diretorio, mude de nome ou envie um novo ficheiro!");
                        }//
                    }
                }
            }
        });




        filePedirFicheiro.setNome_ficheiro(pathname);
        connection.sendToPeers(filePedirFicheiro);
        Thread.sleep(1000);
        return ficheiro_final;
    }

    @Override
    public ArrayList<String> Delete(String pathname) throws Exception {

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
    public List<File_listar_ficheiros> List_Files() throws Exception {
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


    // Fazer futuramente
    @Override
    public void Obter_Metadata(String pathname) throws Exception {

    }

    public void setFicheiro_final(File ficheiro_final) {
        this.ficheiro_final = ficheiro_final;
    }
}
