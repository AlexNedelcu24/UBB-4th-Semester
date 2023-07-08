package network.utils;

import network.rpcprotocol.ClientRpcReflectionWorker;
import services.IServices;

import java.net.Socket;


public class RpcConcurrentServer extends AbsConcurrentServer {
    private IServices Server;
    public RpcConcurrentServer(int port, IServices chatServer) {
        super(port);
        this.Server = chatServer;
        System.out.println("RpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
       // ChatClientRpcWorker worker=new ChatClientRpcWorker(chatServer, client);
        ClientRpcReflectionWorker worker=new ClientRpcReflectionWorker(Server, client);

        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
