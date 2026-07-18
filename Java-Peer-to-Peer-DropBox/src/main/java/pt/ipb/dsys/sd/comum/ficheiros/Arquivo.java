package pt.ipb.dsys.sd.comum.ficheiros;
import pt.ipb.dsys.sd.comum.Mensagem_peer;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Arquivo implements Mensagem_peer {

    private static final int CHUNK_SIZE = 64 * 1024;

    public static String nome_sha256(String ficheiro_) throws NoSuchAlgorithmException, IOException {
        File ficheiro = new File(ficheiro_);


        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        try (FileInputStream fis = new FileInputStream(ficheiro)) {

            byte[] buffer = new byte[8192];
            int bytesLidos;

            while ((bytesLidos = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesLidos);
            }
        }

        byte[] hash = digest.digest();

        StringBuilder sb = new StringBuilder();

        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    public static List<FileChunk> Criar_chunks(String nome_ficheiro, String caminhoFicheiro) throws IOException, NoSuchAlgorithmException {
        String SHA256 = nome_sha256(caminhoFicheiro);
        List<FileChunk> chunks = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(caminhoFicheiro)) {

            byte[] buffer = new byte[CHUNK_SIZE];
            int bytesLidos;
            int numeroChunk = 0;

            while ((bytesLidos = fis.read(buffer)) != -1) {

                byte[] dados = Arrays.copyOf(buffer, bytesLidos);

                chunks.add(new FileChunk(nome_ficheiro,SHA256,numeroChunk, dados));

                numeroChunk++;
            }
        }

        return chunks;
    }


}
