package pt.ipb.dsys.sd.peer;

import org.jgroups.Message;
import org.jgroups.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.comum.ConnectionManager;
import pt.ipb.dsys.sd.comum.ficheiros.Arquivo;
import pt.ipb.dsys.sd.comum.ficheiros.FileAssembler;
import pt.ipb.dsys.sd.comum.protocolo.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class Main_peer {
    private static final Logger logger = LoggerFactory.getLogger(Main_peer.class);
    public static void main(String[] args) throws Exception {
        ConnectionManager connection = new ConnectionManager();
        Map<String, Map<Integer, File_enviar_chunk>> chunksList = new HashMap<>(); // Criar um objeto map do tipo File_enviar_chunk para receber os chunks.

        connection.setPeerBoxReceiver(new Receiver() {
            @Override
            public void receive(Message msg) {
                if (msg.getObject() instanceof File_status_peers) {
                    File_status_peers status = msg.getObject();
                    try {
                        File_status_peers estado_peer = new File_status_peers(status.getCLUSTER_CLIENT());
                        estado_peer.setID_peer(connection.nome_logico());
                        InetAddress ip = InetAddress.getLocalHost();
                        estado_peer.setIP(ip.getHostAddress());
                        estado_peer.setEstado("Ativo ✅");
                        Num_ficheiros numFicheiros = new Num_ficheiros();
                        estado_peer.setNum_ficheiros(numFicheiros.contarFicheiros("FICHEIROS_PEERS"));
                        connection.userChannel.connect(status.getCLUSTER_CLIENT());
                        //connection.sendToUser("Sim estou a escuta do "+ connection.nome_logico());
                        connection.sendToUser(estado_peer);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (msg.getObject() instanceof File_enviar_chunk) { // Verificando se o objeto recebido pelo user é do tipo File_enviar_chunk.
                    File_enviar_chunk chunk = (File_enviar_chunk) msg.getObject();
                    chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>()).put(chunk.getNumero(), chunk); // Passando o chunk para o Map onde primeiro cria o map com a SHA256, dps cria um map com o num do chunk, e dentro guarda o chunk recebido.
                    logger.info("Recebido chunk {}", chunk.getNumero());
                    if (chunksList.get(chunk.getSha256()).size() == chunk.getTotalChunks()) { // Verificar se o numero de chunks para o determinado SHA256 é igual ao total de chunks que deve receber
                        File pasta = new File("FICHEIROS_PEERS"); // Criar a pasta para guardar os ficheiros.
                        if (!pasta.exists()) {
                            pasta.mkdirs();
                        }
                        File ficheiro = new File("FICHEIROS_PEERS/" + chunk.getNome_ficheiro());
                        if (!ficheiro.exists()){
                            Map<Integer, File_enviar_chunk> chunksRecebidos = chunksList.computeIfAbsent(chunk.getSha256(), k -> new HashMap<>());
                            try {
                                FileAssembler.reconstruirFicheiro(chunksRecebidos,"FICHEIROS_PEERS/" + chunk.getNome_ficheiro()); // Reconstruir o ficheiro e guardar na pasta downloads
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                chunksList.remove(chunk.getSha256());
                                logger.info("Ficheiro recebido com sucesso pelo: " + connection.nome_logico());
                                connection.userChannel.connect(chunk.getCLUSTER_CLIENT());
                                chunk.setMensagem("Ficheiro recebido com sucesso pelo: " + connection.nome_logico());
                                connection.sendToUser(chunk);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }else{
                            try {
                                chunksList.remove(chunk.getSha256());
                                logger.info("O ficheiro ja existe no: " + connection.nome_logico()+ ", mude de nome ou envie um novo ficheiro");
                                connection.userChannel.connect(chunk.getCLUSTER_CLIENT());
                                chunk.setMensagem("O ficheiro ja existe no: " + connection.nome_logico()+ ", mude de nome ou envie um novo ficheiro");
                                connection.sendToUser(chunk);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }//
                    }
                }
                else if (msg.getObject() instanceof File_receber_ficheiro) {
                    File_receber_ficheiro filePedirFicheiro = msg.getObject();
                    Funcionalidades_Peer funcionalidadesPeer = new Funcionalidades_Peer();
                    try {
                        funcionalidadesPeer.enviar_ficheiro(Arquivo.Criar_chunks(filePedirFicheiro.getNome_ficheiro(),"FICHEIROS_PEERS/"+filePedirFicheiro.getNome_ficheiro()),filePedirFicheiro.getCLUSTER_CLIENT());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
                else if (msg.getObject() instanceof File_apagar_ficheiro) {
                    File_apagar_ficheiro fileApagarFicheiro = msg.getObject();
                    File ficheiro = new File("FICHEIROS_PEERS/" + fileApagarFicheiro.getNome_ficheiro());
                    File_apagar_ficheiro fileApagarFicheiro_resposta = new File_apagar_ficheiro(fileApagarFicheiro.getCLUSTER_CLIENT());
                    if (!ficheiro.exists()){
                        try {
                            logger.info("Ficheiro não existe no: " + connection.nome_logico());
                            connection.userChannel.connect(fileApagarFicheiro.getCLUSTER_CLIENT());
                            fileApagarFicheiro_resposta.setResposta("Ficheiro não existe no: " + connection.nome_logico());
                            connection.sendToUser(fileApagarFicheiro_resposta);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        ficheiro.delete();
                        try {
                            logger.info("Ficheiro apagado no: " + connection.nome_logico());
                            connection.userChannel.connect(fileApagarFicheiro.getCLUSTER_CLIENT());
                            fileApagarFicheiro_resposta.setResposta("Ficheiro apagado no: " + connection.nome_logico());
                            connection.sendToUser(fileApagarFicheiro_resposta);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
                else if (msg.getObject() instanceof File_listar_ficheiros) {
                    File_listar_ficheiros fileInformacoesLocais = msg.getObject();
                    fileInformacoesLocais.setFicheiros(PeerInfo.listarFicheiros());
                    fileInformacoesLocais.setCLUSTER_SERVIDOR(connection.nome_logico());
                    logger.info("Oii recebi");

                    try {
                        connection.userChannel.connect(fileInformacoesLocais.getCLUSTER_CLIENT());
                        connection.sendToUser(fileInformacoesLocais);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    logger.info(msg.getObject().toString());
                }
            }
        });
    }
}
