### Projeto dropbox

O presente projeto tem como objetivo simular um dropbox, onde os Peers ficam em maquinas openjdk:17.0.1 no docker, e ha duas interfaces para interagir com os Peers, uma desktop feita com swing em java, e uma web feita em Html e Javascript com recurso a uma API feita em Spring boot. Os Peers oferecem os serviços de Upload de ficheiros, Download de ficheiros, Delete ficheiros, e Listar ficheiros, bem como enviam mensagens quando estão ativos. Para fazer a ligação das interfaces com o Docker, foi também usado o Jgroups.

### Protocolo de comunição entre Interfaces e Peers

Pasta -> src -> Main -> Java -> pt.ipb.dsys.sd -> comum -> protocolo

Para melhor comunicação entre os usuarios e os peers, foram criadas classes que servem de protocolo de comunicação entre os usuarios e os peers, onde foram criadas classes como:

- File_enviar_chunk: Que server para enviar os chunks, ou seja, o cliente envia um objeto chunk um por um, e quando o peer recebe e reconhece o objeto "File_enviar_chunk", é dado o devido tratamento. Cada objeto chunk contem: Nome do ficheiro, FileID Sha256, numero do chunk, total de chunks, os bytes do chunk, o Cluster do cliente que enviou, e uma String mensagem (que é usada pelos peers para mandar de volta um objeto do tipo"File_enviar_chunk", com a mensagem se os chunks foram totalmente recebidos).
  
- File_apagar_ficheiro: Serve para os clientes apagarem ficheiros dos peers, cada objeto dessa classe contem: Nome do ficheiro, Cluster do cliente, e uma String mensagem(que serve para obter mensagens dos peers), ou seja quando uma mensagem é apagada os peers, os peers enviam de volta um objeto "File_apagar_ficheiro" que é reconhecido pelo user, e que contem a mensagem enviada pelos peers.
  
- File_receber_ficheiro: Serve para os usuarios receberem de volta algum ficheiro dos peers, cada objeto da classe File_receber_ficheiro contem: Nome do ficheiro, FileID Sha256, numero do chunk, total de chunks, os bytes do chunk, o Cluster do cliente que fez o pedido.

- File_listar_ficheiros: A classe foi criada com objetivo, de criar objetos para obter a lista de ficheiros de cada objeto, cada objeto desta classe contem: Uma lista do tipo FicheiroInfo( onde cada objeto da lista contem: Nome do ficheiro, tamanho e numero de chunks), Cluster do cliente, e Cluster do Servidor.

- File_status_peer: A classe foi criada, com objetivo de criar objetos para obter o estado de cada peer, cada objeto da classe contem: Cluster do cliente que fez o pedido, ID_peer(Identificador logico de cada peer), IP do peer, estado do peer(ativo, desativado), numero de ficheiros, e uma String de mensagem.
