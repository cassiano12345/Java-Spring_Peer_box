package pt.ipb.dsys.sd.comum;

import org.jgroups.JChannel;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ObjectMessage;
import org.jgroups.Receiver;
import org.jgroups.protocols.*;
import org.jgroups.protocols.pbcast.GMS;
import org.jgroups.protocols.pbcast.NAKACK2;
import org.jgroups.protocols.pbcast.STABLE;
import org.jgroups.stack.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ipb.dsys.sd.peer.Main_peer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectionManager {
    private final JChannel peerBoxChannel;
    public JChannel userChannel;
    private static final Logger logger = LoggerFactory.getLogger(Main_peer.class);


    public ConnectionManager() throws Exception {

        peerBoxChannel = new JChannel();
        peerBoxChannel.connect("peer-box");

        userChannel = new JChannel();
    }

    public void setPeerBoxReceiver(Receiver receiver) {
        peerBoxChannel.setReceiver(receiver);
    }

    public void setUserReceiver(Receiver receiver) {
        userChannel.setReceiver(receiver);
    }

    public void sendToPeers(Object obj) throws Exception {
        peerBoxChannel.send(new ObjectMessage(null, obj));
    }

    public String nome_logico(){

        return peerBoxChannel.getName().toString();
    }

    public void sendToUser(Object obj) throws Exception {
        userChannel.send(new ObjectMessage(null, obj));
    }

    public JChannel getPeerBoxChannel() {
        return peerBoxChannel;
    }

    public JChannel getUserChannel() {
        return userChannel;
    }

    public static List<Protocol> gossipRouter() throws UnknownHostException{
        List<Protocol> protocols = new ArrayList<>();
        TUNNEL tunnel = new TUNNEL();
        try {
            InetAddress grAddress = InetAddress.getByName("gossip-router");
            logger.info("Found gossip router at {} (using it)", grAddress);
            tunnel.setGossipRouterHosts("gossip-router[12001]");
        } catch (UnknownHostException e) {
            System.setProperty("jgroups.bind_addr", "127.0.0.1");
            tunnel.setGossipRouterHosts("localhost[12001]");
        }
        protocols.add(tunnel);
        protocols.add(new PING());
        return protocols;
    }

/*
    public static List<Protocol> gossipRouter() throws UnknownHostException {
        TUNNEL tunnel = new TUNNEL();

        try {
            InetAddress.getByName("gossip-router");
            logger.info("Found gossip router (using gossip-router)");
            tunnel.setGossipRouterHosts("gossip-router[12001]");
        }
        catch(UnknownHostException e) {
            logger.info("Using localhost gossip router");
            tunnel.setGossipRouterHosts("127.0.0.1[12001]");
        }

        return Arrays.asList(
                tunnel,

                // Descoberta e gestão do cluster
                new MERGE3(),
                new FD_SOCK(),
                new FD_ALL3(),
                new VERIFY_SUSPECT(),

                // Fiabilidade
                new NAKACK2(),
                new UNICAST3(),
                new STABLE(),
                new PING(),

                // Gestão do grupo
                new GMS(),

                // Controlo de fluxo
                new UFC(),
                new MFC(),

                // Fragmentação
                new FRAG4()
        );
    }
*/
}
