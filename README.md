### Projeto dropbox

O presente projeto tem como objetivo simular um dropbox, onde os Peers ficam em maquinas openjdk:17.0.1 no docker, e ha duas interfaces para interagir com os Peers, uma desktop feita com swing em java, e uma web feita em Html e Javascript com recurso a uma API feita em Spring boot. Os Peers oferecem os serviços de Upload de ficheiros, Download de ficheiros, Delete ficheiros, e Listar ficheiros, bem como enviam mensagens quando estão ativos. Para fazer a ligação das interfaces com o Docker, foi também usado o Jgroups.

***Modo de utilização*** <br/>

Fazer o docker compose up ao ficheiro, presente na pasta docker, compilar o Main da interface desktop em (src -> java -> pt/ipb/dsys/sd/sender -> Main_sender.java), e tera acesso a interface desktop, para ter acessos a interface web basta compilar o Main do Spring boot em (src -> java -> pt/ipb/dsys/sd/web -> PeerboxApplication.java), e através do browser entrar em "localhost:8080".


### Protocolo de comunição entre Interfaces e Peers

Pasta -> src -> Main -> Java -> pt.ipb.dsys.sd -> comum -> protocolo

Para melhor comunicação entre os usuarios e os peers, foram criadas classes que servem de protocolo de comunicação entre os usuarios e os peers, onde foram criadas classes como:

- File_enviar_chunk: Que server para enviar os chunks, ou seja, o cliente envia um objeto chunk um por um, e quando o peer recebe e reconhece o objeto "File_enviar_chunk", é dado o devido tratamento. Cada objeto chunk contem: Nome do ficheiro, FileID Sha256, numero do chunk, total de chunks, os bytes do chunk, o Cluster do cliente que enviou, e uma String mensagem (que é usada pelos peers para mandar de volta um objeto do tipo"File_enviar_chunk", com a mensagem se os chunks foram totalmente recebidos).
  
- File_apagar_ficheiro: Serve para os clientes apagarem ficheiros dos peers, cada objeto dessa classe contem: Nome do ficheiro, Cluster do cliente, e uma String mensagem(que serve para obter mensagens dos peers), ou seja quando uma mensagem é apagada os peers, os peers enviam de volta um objeto "File_apagar_ficheiro" que é reconhecido pelo user, e que contem a mensagem enviada pelos peers.
  
- File_receber_ficheiro: Serve para os usuarios receberem de volta algum ficheiro dos peers, cada objeto da classe File_receber_ficheiro contem: Nome do ficheiro, FileID Sha256, numero do chunk, total de chunks, os bytes do chunk, o Cluster do cliente que fez o pedido.

- File_listar_ficheiros: A classe foi criada com objetivo, de criar objetos para obter a lista de ficheiros de cada objeto, cada objeto desta classe contem: Uma lista do tipo FicheiroInfo( onde cada objeto da lista contem: Nome do ficheiro, tamanho e numero de chunks), Cluster do cliente, e Cluster do Servidor.

- File_status_peer: A classe foi criada, com objetivo de criar objetos para obter o estado de cada peer, cada objeto da classe contem: Cluster do cliente que fez o pedido, ID_peer(Identificador logico de cada peer), IP do peer, estado do peer(ativo, desativado), numero de ficheiros, e uma String de mensagem.


### Imagens

***Página interface Web*** <br/>

A imagem a baixo mostra a página principal da interface web, onde é possível ver a caixa de logs, os botões para atualizar a tabela dos peers, e para enviar ficheiros para os peers, mais a baixo é possivel observar a tabela com informação dos peers em ativo.
<p align="center">
  <img src="Imagens_Aplicação/Pagina web 1.png" alt="OpenMontage" width="700">
</p> <br/>
A tabela a baixo mostra a os ficheiros disponiveis nos peers, e mais ao lado tem os botões para fazer download dos ficheiros a partir dos peers, bem como apagar os ficheiros dos peers.
<p align="center">
  <img src="Imagens_Aplicação/Pagina web 2.png" alt="OpenMontage" width="700">
</p>

***Página interface Desktop*** <br/>

A imagem a baixo mostra a interface desktop, onde é possivel obersar a tabela com os ficheiros presentes nos peers, mais a baixo tem a caixa dos logs, e mais a baixo tem os botões de pesquisar ficheiros no nosso disco, para posteriormente enviar, do mais ao lado tem o botão para pesquisar peers disponiveis, tem o botão para enviar ficheiros, recuperar ficheiros a partir dos peers, apagar ficheiros, e informações locais (que futuramente sera implementado).
<p align="center">
  <img src="Imagens_Aplicação/Pagina principal desktop.png" alt="OpenMontage" width="700">
</p>

***Links*** <br/>

- Link página oficial Spring boot <br/>
https://spring.io/projects/spring-boot

- Link da Maven Central Repository <br/>
https://central.sonatype.com/?smo=true
