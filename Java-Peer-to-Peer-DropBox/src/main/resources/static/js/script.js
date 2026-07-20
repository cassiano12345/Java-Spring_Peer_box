/*
async function listarPeers() {
    const response = await fetch("/peer/peers");
    console.log(response)
    const dados = await response;

    document.getElementById("resultado").innerHTML =
        JSON.stringify(dados, null, 2);
}

async function listarFicheiros() {
    const response = await fetch("/api/files");
    const dados = await response.json();

    document.getElementById("resultado").innerHTML =
        JSON.stringify(dados, null, 2);
}

 */

window.onload = function () {

    adicionarLog("PeerBox iniciado.");

    document.getElementById("btnAtualizarPeers").addEventListener("click", atualizarPeers);

    document.getElementById("btnEnviarFicheiro").addEventListener("click", enviarFicheiro);

    document.getElementById("btnDownload").addEventListener("click", fazerDownload);

    document.getElementById("btnApagar").addEventListener("click", apagarFicheiro);

};

function adicionarLog(texto) {

    const txtLogs = document.getElementById("txtLogs");

    txtLogs.value += texto + "\n";

    txtLogs.scrollTop = txtLogs.scrollHeight;

}

async function atualizarPeers() {
    const response = await fetch("/peer/peers");
    const dados = await response.json();
    console.log(dados);
    adicionarLog("A atualizar peers...");

    // fetch("/api/peers")

}

function enviarFicheiro() {

    adicionarLog("Enviar ficheiro.");

}

function fazerDownload() {

    adicionarLog("Download iniciado.");

}

function apagarFicheiro() {

    adicionarLog("Apagar ficheiro.");

}