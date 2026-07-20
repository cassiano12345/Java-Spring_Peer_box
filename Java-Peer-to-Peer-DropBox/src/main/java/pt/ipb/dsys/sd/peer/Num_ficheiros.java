package pt.ipb.dsys.sd.peer;

import java.io.File;

public class Num_ficheiros {

    public int contarFicheiros(String caminhoPasta) {
        File pasta = new File(caminhoPasta);

        if (!pasta.exists() || !pasta.isDirectory()) {
            return 0;
        }

        File[] ficheiros = pasta.listFiles(File::isFile);

        return ficheiros == null ? 0 : ficheiros.length;
    }
}
