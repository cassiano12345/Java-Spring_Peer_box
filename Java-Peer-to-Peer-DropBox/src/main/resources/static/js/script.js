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
    listarficehiros();
};

function adicionarLog(texto) {

    const txtLogs = document.getElementById("txtLogs");

    txtLogs.value += texto + "\n";

    txtLogs.scrollTop = txtLogs.scrollHeight;

}

async function atualizarPeers() {
    const response = await fetch("/peer/peers");
    const peers = await response.json();
    console.log(peers);
    if(peers != ""){
    const corpo = document.getElementById("tbodyPeers");

        adicionarLog(peers.length+" Peer(s) ativo(s)");

    // Limpa a tabela
    corpo.innerHTML = "";

    peers.forEach(peer => {

        const linha = document.createElement("tr");

        linha.innerHTML = `
            <td>${peer.cluster_CLIENT}</td>
            <td>${peer.ip}</td>
            <td>${peer.estado}</td>
            <td>${peer.num_ficheiros}</td>
        `;

        corpo.appendChild(linha);
    });
    }else{
        adicionarLog("Nenhum peer ativo");
    }
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

async function listarficehiros() {
    const response = await fetch("/peer/list");
    const ficheiros = await response.json();
    console.log(ficheiros);
    const corpo = document.getElementById("tbodyFicheiros");

    if(ficheiros != "") {
        corpo.innerHTML = "";

        ficheiros.forEach(peer => {

            peer.ficheiros.forEach(ficheiro => {

                const linha = document.createElement("tr");

                linha.innerHTML = `
                <td>${ficheiro.nome}</td>
                <td>${ficheiro.tamanho}</td>
                <td>${peer.cluster_SERVIDOR}</td>
                <td>${ficheiro.numeroChunks}</td>
                <td>
                <button onclick="downloadFicheiro('${peer.cluster_SERVIDOR}', '${ficheiro.nome}')">
                    ⬇️ Download
                </button>

                <button onclick="apagarFicheiro('${peer.cluster_SERVIDOR}', '${ficheiro.nome}')">
                    🗑️ Apagar
                </button>
            </td>
            `;

                corpo.appendChild(linha);
            });

        });
    }else{
        adicionarLog("Não ha ficheiros disponíveis nos peers!");
        corpo.innerHTML = "";

    }
}
