window.onload = function () {

    adicionarLog("PeerBox iniciado.");

    document.getElementById("btnAtualizarPeers").addEventListener("click", atualizarPeers);

    document.getElementById("btnEnviarFicheiro").addEventListener("click", enviarFicheiro);

    document.getElementById("inputFicheiro").addEventListener("change", async function () {

        const ficheiro = this.files[0];

        if (!ficheiro)
            return;

        adicionarLog("A enviar: " + ficheiro.name);

        const formData = new FormData();
        formData.append("ficheiro", ficheiro);

        const response = await fetch("/peer/upload", {
            method: "POST",
            body: formData
        });

        const mensagensArray = JSON.parse(await response.text());

        for (const msg of mensagensArray) {
            console.log(msg);
            adicionarLog(msg);
        }

        // Limpa o input para permitir selecionar o mesmo ficheiro outra vez
        this.value = "";
        listarficehiros();
    });

    listarficehiros();
    atualizarPeers();
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
    document.getElementById("inputFicheiro").click();

    adicionarLog("Enviar ficheiro.");

}

async function fazerDownload(nome) {

    adicionarLog("Download iniciado.");
    const response = await fetch("/peer/retrieve?nome=" + encodeURIComponent(nome), {
        method: "GET"
    });

    const blob = await response.blob();

    const url = window.URL.createObjectURL(blob);

    const a = document.createElement("a");
    a.href = url;
    a.download = nome;

    a.click();

    window.URL.revokeObjectURL(url);

}

async function apagarFicheiro(nome) {
    const response = await fetch("/peer/delete?nome=" + encodeURIComponent(nome), {
        method: "POST"
    });

    const resultados = await response.text();
    const mensagensArray = JSON.parse(resultados);

    for (const msg of mensagensArray) {
        console.log(msg);
        adicionarLog(msg);
    }
    listarficehiros();
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
                <button onclick="fazerDownload('${ficheiro.nome}')">
                    ⬇️ Download
                </button>

                <button onclick="apagarFicheiro('${ficheiro.nome}')">
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
