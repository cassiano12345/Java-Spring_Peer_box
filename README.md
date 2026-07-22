### Projeto dropbox

O presente projeto tem como objetivo simular um dropbox, onde os Peers ficam em maquinas openjdk:17.0.1 no docker, e ha duas interfaces para interagir com os Peers, uma desktop feita com swing em java, e uma web feita em Html e Javascript com recurso a uma API feita em Spring boot. Os Peers oferecem os serviços de Upload de ficheiros, Download de ficheiros, Delete ficheiros, e Listar ficheiros, bem como enviam mensagens quando estão ativos. Para fazer a ligação das interfaces com o Docker, foi também usado o Jgroups.
