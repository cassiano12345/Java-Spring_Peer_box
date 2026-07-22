package pt.ipb.dsys.sd.sender;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.comum.ConnectionManager;
import pt.ipb.dsys.sd.comum.ficheiros.FicheiroInfo;
import pt.ipb.dsys.sd.comum.ficheiros.FileAssembler;
import pt.ipb.dsys.sd.comum.protocolo.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

import javax.swing.*;
import java.io.File;
import java.util.List;

public class Main_sender {
    private static final Logger logger = LoggerFactory.getLogger(Main_sender.class);
    public static Janela janela = new Janela();

    public static void main(String[] args) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        connection.userChannel.connect(InetAddress.getLocalHost().getHostName());
        Map<String, Map<Integer, File_receber_ficheiro>> chunksList = new HashMap<>(); // Criar um objeto map do tipo File_enviar_chunk para receber os chunks.

        // Criando um objeto do tipo janela, e abrindo ela.
        SwingUtilities.invokeLater(() -> {
            janela.setVisible(true);
        });
        Funcionalidades_User funcionalidadesUser = new Funcionalidades_User();
        funcionalidadesUser.listarFicheiros();//Atualizar a tabela
        connection.setUserReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                if (msg.getObject() instanceof File_status_peers) {
                    File_status_peers status = msg.getObject();
                    try {
                        janela.mostrarAlerta(status.getEstado());
                        janela.adicionarLog("O peer " + status.getID_peer() + " esta "+ status.getEstado());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (msg.getObject() instanceof File_listar_ficheiros) {
                    File_listar_ficheiros fileInformacoesLocais = msg.getObject();
                    List<FicheiroInfo> ficheiros = fileInformacoesLocais.getFicheiros();
                    for (FicheiroInfo f : ficheiros) {
                        System.out.println(
                                f.getNome() + " | " +
                                        f.getTamanho() + " bytes | " +
                                        f.getNumeroChunks() + " chunks");
                        janela.adicionarFicheiro(f.getNome(),String.valueOf(f.getTamanho()) , f.getNumeroChunks(), fileInformacoesLocais.getCLUSTER_SERVIDOR());
                    }
                }
                else if (msg.getObject() instanceof File_enviar_chunk) { // Verificando se o objeto recebido pelo user é do tipo File_enviar_chunk.
                    File_enviar_chunk chunk = (File_enviar_chunk) msg.getObject();
                    janela.adicionarLog(chunk.getMensagem());
                }
                else if (msg.getObject() instanceof File_apagar_ficheiro) {
                    File_apagar_ficheiro resposta = msg.getObject();
                    try {
                        janela.adicionarLog(resposta.getResposta());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (msg.getObject() instanceof File_receber_ficheiro) { // Verificando se o objeto recebido pelo user é do tipo File_enviar_chunk.
                    File_receber_ficheiro chunk = (File_receber_ficheiro) msg.getObject();
                    chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>()).put(chunk.getNumero(), chunk); // Passando o chunk para o Map onde primeiro cria o map com a SHA256, dps cria um map com o num do chunk, e dentro guarda o chunk recebido.
                    logger.info("Recebido chunk {}", chunk.getNumero());
                    if (chunksList.get(chunk.getSha256()).size() == chunk.getTotalChunks()) { // Verificar se o numero de chunks para o determinado SHA256 é igual ao total de chunks que deve receber
                        File pasta = new File("FICHEIROS_PEERS_USER"); // Criar a pasta para guardar os ficheiros.
                        if (!pasta.exists()) {
                            pasta.mkdirs();
                        }
                        File ficheiro = new File("FICHEIROS_PEERS_USER/" + chunk.getNome_ficheiro());
                        if (!ficheiro.exists()){
                            Map<Integer, File_receber_ficheiro> chunksRecebidos = chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>());
                            try {
                                FileAssembler.reconstruirFicheiro_receber(chunksRecebidos,"FICHEIROS_PEERS_USER/" + chunk.getNome_ficheiro()); // Reconstruir o ficheiro e guardar na pasta downloads
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            janela.mostrarAlerta("Ficheiro recebido!");
                            chunksList.remove(chunk.getSha256());
                        }else{
                            chunksList.remove(chunk.getSha256());

                            janela.adicionarLog("O ficheiro ja existe no diretorio!");
                            //janela.mostrarAlerta("O ficheiro ja existe no diretorio, mude de nome ou envie um novo ficheiro!");
                        }//
                    }
                }
                else {
                    janela.adicionarLog(msg.getObject().toString());
                }


                /*
                                else if (msg.getObject() instanceof File_enviar_chunk) { // Verificando se o objeto recebido pelo user é do tipo File_enviar_chunk.
                    File_enviar_chunk chunk = (File_enviar_chunk) msg.getObject();
                    chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>()).put(chunk.getNumero(), chunk); // Passando o chunk para o Map onde primeiro cria o map com a SHA256, dps cria um map com o num do chunk, e dentro guarda o chunk recebido.
                    logger.info("Recebido chunk {}", chunk.getNumero());
                    if (chunksList.get(chunk.getSha256()).size() == chunk.getTotalChunks()) { // Verificar se o numero de chunks para o determinado SHA256 é igual ao total de chunks que deve receber
                        File pasta = new File("FICHEIROS_PEERS_USER"); // Criar a pasta para guardar os ficheiros.
                        if (!pasta.exists()) {
                            pasta.mkdirs();
                        }
                        File ficheiro = new File("FICHEIROS_PEERS_USER/" + chunk.getNome_ficheiro());
                        if (!ficheiro.exists()){
                            Map<Integer, File_enviar_chunk> chunksRecebidos = chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>());
                            try {
                                FileAssembler.reconstruirFicheiro(chunksRecebidos,"FICHEIROS_PEERS_USER/" + chunk.getNome_ficheiro()); // Reconstruir o ficheiro e guardar na pasta downloads
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            janela.mostrarAlerta("Ficheiro recebido!");
                            chunksList.remove(chunk.getSha256());
                        }else{
                            chunksList.remove(chunk.getSha256());

                            janela.adicionarLog("O ficheiro ja existe no diretorio!");
                            //janela.mostrarAlerta("O ficheiro ja existe no diretorio, mude de nome ou envie um novo ficheiro!");
                        }//
                    }
                }
                                */
            }
        });
    }
}
