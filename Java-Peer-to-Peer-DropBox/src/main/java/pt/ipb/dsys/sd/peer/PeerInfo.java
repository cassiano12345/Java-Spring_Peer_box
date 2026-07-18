package pt.ipb.dsys.sd.peer;

import pt.ipb.dsys.sd.comum.ficheiros.FicheiroInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PeerInfo {

    private static final String PASTA_PEER = "FICHEIROS_PEERS";

    public static List<FicheiroInfo> listarFicheiros() {

        List<FicheiroInfo> ficheiros = new ArrayList<>();

        File pasta = new File(PASTA_PEER);

        if (!pasta.exists()) {
            pasta.mkdirs();
            return ficheiros;
        }

        File[] lista = pasta.listFiles();

        if (lista == null) {
            return ficheiros;
        }

        for (File ficheiro : lista) {

            if (ficheiro.isFile()) {

                long tamanho = ficheiro.length();

                // 64 KB = 65536 bytes
                int numeroChunks = (int) Math.ceil((double) tamanho / 65536);

                ficheiros.add(new FicheiroInfo(
                        ficheiro.getName(),
                        tamanho,
                        numeroChunks
                ));
            }
        }

        return ficheiros;
    }
}
