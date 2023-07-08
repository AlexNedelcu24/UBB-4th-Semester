import network.utils.AbstractServer;
import network.utils.RpcConcurrentServer;
import network.utils.ServerException;
import org.hibernate.SessionFactory;
import persistence.*;
import persistence.repoDB.*;
import persistence.util.HibernateUtil;
import server.ServicesImpl;
import services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
        // UserRepository userRepo=new UserRepositoryMock();
        Properties serverProps=new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        ISalesAgent interfaceSalesAgent = new SalesAgentRepo(sessionFactory);

        IProduct interfaceProduct = new ProductRepo(sessionFactory);

        IOffer interfaceOffer = new OfferRepo(sessionFactory);

        IClient interfaceClient = new ClientRepo(sessionFactory);

        IOrder interfaceOrder = new OrderRepo(sessionFactory);

        //private IServices ser = new ServicesImpl(interfaceSalesAgent,interfaceProduct,interfaceClient,interfaceOffer);


        IServices ServerImpl = new ServicesImpl(interfaceSalesAgent,interfaceProduct,interfaceClient,interfaceOffer,interfaceOrder);
        int ServerPort=defaultPort;
        try {
            ServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+ServerPort);
        AbstractServer server = new RpcConcurrentServer(ServerPort, ServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(ServerException e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
    }
}
