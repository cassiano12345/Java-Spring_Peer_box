package pt.ipb.dsys.sd.comum.ficheiros;
import pt.ipb.dsys.sd.comum.protocolo.File_enviar_chunk;
import pt.ipb.dsys.sd.comum.protocolo.File_receber_ficheiro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FileAssembler {

    // Função tranformar os chunks em ficheiro.
    public static void reconstruirFicheiro(Map<Integer, File_enviar_chunk> chunks,String caminhoDestino) throws IOException {

        File ficheiro = new File(caminhoDestino);

        try (FileOutputStream fos = new FileOutputStream(ficheiro)) {

            for (int i = 0; i < chunks.size(); i++) {

                File_enviar_chunk chunk = chunks.get(i);

                if (chunk == null) {
                    throw new IOException("Falta o chunk " + i);
                }

                fos.write(chunk.getDados());
            }
        }
    }

    public static void reconstruirFicheiro_receber(Map<Integer, File_receber_ficheiro> chunks, String caminhoDestino) throws IOException {

        File ficheiro = new File(caminhoDestino);

        try (FileOutputStream fos = new FileOutputStream(ficheiro)) {

            for (int i = 0; i < chunks.size(); i++) {

                File_receber_ficheiro chunk = chunks.get(i);

                if (chunk == null) {
                    throw new IOException("Falta o chunk " + i);
                }

                fos.write(chunk.getDados());
            }
        }
    }

}
