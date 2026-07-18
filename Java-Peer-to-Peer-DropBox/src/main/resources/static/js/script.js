async function listarPeers() {
    const response = await fetch("/peer/peers");
    console.log(response)
    const dados = await response;

    document.getElementById("resultado").innerHTML = dados;
}

async function listarFicheiros() {
    const response = await fetch("/api/files");
    const dados = await response.json();

    document.getElementById("resultado").innerHTML =
        JSON.stringify(dados, null, 2);
}