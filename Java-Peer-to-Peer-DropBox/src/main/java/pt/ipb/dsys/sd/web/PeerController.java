package pt.ipb.dsys.sd.web;

import org.springframework.web.bind.annotation.*;
import pt.ipb.dsys.sd.comum.protocolo.File_listar_ficheiros;
import pt.ipb.dsys.sd.comum.protocolo.File_status_peers;
import pt.ipb.dsys.sd.sender.Funcionalidades_User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/peer")
public class PeerController {

    private final Funcionalidades_User_Web funcionalidades = new Funcionalidades_User_Web();

    @GetMapping
    public String index() {
        return "Peer Box API";
    }

    @GetMapping("/peers")
    public List<File_status_peers> peersAtivos() throws Exception {

        return funcionalidades.peersAtivos();
    }

    @PostMapping("/share")
    public String partilharFicheiro(@RequestParam String caminho, @RequestParam int replicacao) {

        // Vamos implementar depois
        return "Pedido para partilhar o ficheiro recebido.";
    }

    @PostMapping("/delete")
    public String apagarFicheiro(@RequestParam String nome) throws Exception {

        return funcionalidades.apagarFicheiro(nome);
    }

    @GetMapping("/retrieve")
    public String recuperarFicheiro(@RequestParam String nome) throws Exception {

        funcionalidades.recuperarFicheiro(nome);
        return "Pedido de recuperação enviado.";
    }

    @GetMapping("/list")
    public List<File_listar_ficheiros> listarFicheiros() throws Exception {


        return funcionalidades.listarFicheiros();
    }

    @GetMapping("/metadata")
    public String metadata(@RequestParam String nome) {

        // Vamos implementar depois
        return "Pedido de metadata recebido para: " + nome;
    }
}
