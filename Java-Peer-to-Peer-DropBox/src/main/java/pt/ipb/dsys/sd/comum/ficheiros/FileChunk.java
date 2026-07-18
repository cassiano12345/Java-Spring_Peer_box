package pt.ipb.dsys.sd.comum.ficheiros;
import java.io.Serializable;


public class FileChunk implements Serializable {
    private final String nome_ficheiro;
    private final String sha256;
    private final int numero;
    private final byte[] dados;


    public FileChunk(String nomeFicheiro, String sha256, int numero, byte[] dados) {
        nome_ficheiro = nomeFicheiro;
        this.sha256 = sha256;
        this.numero = numero;
        this.dados = dados;
    }

    public String getNome_ficheiro() {
        return nome_ficheiro;
    }

    public String getSha256() {
        return sha256;
    }

    public int getNumero() {
        return numero;
    }

    public byte[] getDados() {
        return dados;
    }

}
