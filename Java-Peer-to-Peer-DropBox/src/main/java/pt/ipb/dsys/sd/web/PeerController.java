package pt.ipb.dsys.sd.web;

import org.springframework.web.bind.annotation.*;
import pt.ipb.dsys.sd.sender.Funcionalidades_User;

@RestController
@RequestMapping("/peer")
public class PeerController {

    private final Funcionalidades_User funcionalidades = new Funcionalidades_User();

    @GetMapping
    public String index() {
        return "Peer Box API";
    }

    @GetMapping("/peers")
    public String peersAtivos() throws Exception {

        return funcionalidades.peersAtivos();
    }

    @PostMapping("/share")
    public String partilharFicheiro(@RequestParam String caminho, @RequestParam int replicacao) {

        // Vamos implementar depois
        return "Pedido para partilhar o ficheiro recebido.";
    }

    @DeleteMapping("/delete")
    public String apagarFicheiro(@RequestParam String nome) throws Exception {

        funcionalidades.apagarFicheiro(nome);
        return "Pedido para apagar o ficheiro enviado.";
    }

    @GetMapping("/retrieve")
    public String recuperarFicheiro(@RequestParam String nome) throws Exception {

        funcionalidades.recuperarFicheiro(nome);
        return "Pedido de recuperação enviado.";
    }

    @GetMapping("/list")
    public String listarFicheiros() throws Exception {

        funcionalidades.listarFicheiros();
        return "Pedido de listagem enviado.";
    }

    @GetMapping("/metadata")
    public String metadata(@RequestParam String nome) {

        // Vamos implementar depois
        return "Pedido de metadata recebido para: " + nome;
    }
}
