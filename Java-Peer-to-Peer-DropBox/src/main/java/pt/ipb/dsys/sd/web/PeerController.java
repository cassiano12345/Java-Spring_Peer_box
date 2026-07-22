package pt.ipb.dsys.sd.web;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.ipb.dsys.sd.comum.protocolo.File_listar_ficheiros;
import pt.ipb.dsys.sd.comum.protocolo.File_status_peers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    @PostMapping("/upload")
    public ArrayList<String> upload(@RequestParam("ficheiro") MultipartFile ficheiro) throws Exception {
        Path destino = Paths.get("FICHEIROS_PEERS_USER", ficheiro.getOriginalFilename());
        Files.createDirectories(destino.getParent());
        ficheiro.transferTo(destino);

        return funcionalidades.enviarFicheiro(ficheiro.getOriginalFilename());
    }


    @PostMapping("/delete")
    public ArrayList<String> apagarFicheiro(@RequestParam String nome) throws Exception {

        return funcionalidades.apagarFicheiro(nome);
    }

    @PostMapping("/retrieve")
    public ResponseEntity<Resource> recuperarFicheiro(@RequestParam String nome) throws Exception {
        File ficheiro = funcionalidades.recuperarFicheiro(nome);

        Resource resource = new FileSystemResource(ficheiro);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + ficheiro.getName() + "\"")
                .contentLength(ficheiro.length())
                .body(resource);
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
