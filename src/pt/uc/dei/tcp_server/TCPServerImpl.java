package pt.uc.dei.tcp_server;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class TCPServerImpl extends UnicastRemoteObject implements TCPServer {

    //Hashtable que vai guardar a relação de users com as suas threads
    private Hashtable<String, Events> membersonline = new Hashtable<>();
    private volatile boolean master = false;


    public TCPServerImpl() throws RemoteException {
        super();


    }


    public static void main(String args[]) throws RemoteException {

        TCPServerImpl tcpimp = new TCPServerImpl();

        tcpimp.init(args);


    }


    private synchronized void validateIfMaster() throws NotMasterException {
        if (!master) {
            throw new NotMasterException();
        }
    }



    public void init(String args[]) {

        if (args.length < 1) {
            System.out.println("Usage: java TCPServerImpl.java other_server_ip ");
            System.exit(0);
        }

        UDPService udpservice = new UDPService(this, args[0]);
        udpservice.start();


        int numero = 0;
        Properties props = new Properties();

        try {
            props.load(new FileInputStream("support/property"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        try {

            ServerSocket listenMainSocket;
            ServerSocket listenAuxSocket;
            listenMainSocket = new ServerSocket(Integer.parseInt(props.getProperty("tcpServerPort")));
            System.out.println("TCP Server ready! Main socket à escuta no porto " + props.getProperty("tcpServerPort"));
            System.out.println("LISTEN SOCKET=" + listenMainSocket);
            listenAuxSocket = new ServerSocket(Integer.parseInt(props.getProperty("tcpServerPortAux")));
            System.out.println("TCP Server ready! Secondary socket à escuta no porto " + props.getProperty("tcpServerPortAux"));
            System.out.println("LISTEN SOCKET=" + listenAuxSocket);

            while (true) {
                if (master) {
                    Socket clientSocket = listenMainSocket.accept();
                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
                    numero++;
                    Connection clientMain = new Connection(clientSocket, numero);

                    clientMain.start();
                    Socket clientAuxSocket = listenAuxSocket.accept();
                    System.out.println("CLIENT_SOCKET (created at accept())=" + clientAuxSocket);
                    Events clientEvents = new Events(clientAuxSocket, numero, this);

                    clientEvents.start();
                }

            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());

        }


    }






    public synchronized Collection<Events> values() {
        return membersonline.values();
    }

    public synchronized Enumeration<String> keys() {
        return membersonline.keys();
    }

    public synchronized Events put(String key, Events value) {
        return membersonline.put(key, value);
    }

    public synchronized Events get(Object key) {
        return membersonline.get(key);
    }

    public synchronized boolean contains(Object value) {
        return membersonline.contains(value);
    }

    public synchronized boolean containsKey(Object key) {
        return membersonline.containsKey(key);
    }

    public synchronized boolean containsValue(Object value) {
        return membersonline.containsValue(value);
    }

    public synchronized void ping() throws RemoteException {

    }

    //metodo chamado pelo RMI para enviar uma mensagem para varios utilizadores ou várias mensagens para um só utilizador.

    public synchronized void sendMsg(Message[] messages, String[] usernames) throws RemoteException, NotMasterException {
        validateIfMaster();
        for (Message m : messages) {
            for (String u : usernames) {

                Events threadEvent = membersonline.get(u);
                threadEvent.putMsgIntoQueue(m);
            }

        }

    }

    //mandar uma msg para diversos utilizadores
    public synchronized void msgToMany(Message m, String... u) throws RemoteException, NotMasterException {
        Message[] msgs = new Message[1];
        msgs[0] = m;
        this.sendMsg(msgs, u);

    }


    //mandar varias msgs para o memso utilizador
    public void msgsToOne(String u, Message... m) throws RemoteException, NotMasterException {
        String[] user = new String[1];
        user[0] = u;
        this.sendMsg(m, user);
    }

    public synchronized void switchToMaster(boolean isMaster) {
        master = isMaster;

    }

    private void printHash() {
        Enumeration e = this.keys();
        while (e.hasMoreElements()) {

            System.out.println(e.nextElement());

        }
    }


}

/*class UDPSender extends Thread {

    TCPServerImpl tcpServer = null;


    public UDPSender(TCPServerImpl tcpServer, String ip) {

        this.tcpServer = tcpServer;


    }

    public void run() {

        Properties props = new Properties();

        try {
            props.load(new FileInputStream("support/property"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        DatagramSocket uSocket;
        try {
            uSocket = new DatagramSocket();
            //TODO em vez de ser com I AM ALive, verificar primeiro o estado de master e enviar um ping consoante isso "I AM MASTER", "I AM SLAVE"
            byte[] m = "ping".getBytes();
            //TODO mudar para ir trocando de ip constantemente
            InetAddress aHost = InetAddress.getByName(props.getProperty("tcpip2"));
            DatagramPacket msg = new DatagramPacket(m, m.length, aHost, Integer.parseInt(props.getProperty("udpPort")));
            while (true) {


                uSocket.send(msg);
                    //envia pings de 3 em 3 segundos

                    this.currentThread().sleep(3000);
                    System.out.println("Estou a enviar pacotes!");



            }


        } catch (SocketException e) {

            System.out.println("SocketException!");

            tcpServer.switchToMaster(true);
        } catch (IOException e) {

            System.out.println("IO Exception!");
            System.out.println("Nao encontrei o outro server, sou o master!");
            tcpServer.switchToMaster(true);
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception!");
            System.out.println("Mudando para master");

        }
    }
}


class UDPReceiver extends Thread {

    static DatagramSocket uSocket;

    static Properties props = new Properties();

    TCPServerImpl tcpServer = null;

    public UDPReceiver(TCPServerImpl tcpServer, String ip) {

        this.tcpServer = tcpServer;


    }

    public void run() {

        try {

            props.load(new FileInputStream("support/property"));


            byte[] buffer = new byte[1024];
            uSocket = null;

            uSocket = new DatagramSocket(Integer.parseInt(props.getProperty("udpPort")));
            //Esperar 10 segundos pelo server tcp principal para lhe dar tempo para voltar up
            uSocket.setSoTimeout(10000);
            DatagramPacket request;


            while (true) {

                try {
                    request = new DatagramPacket(buffer, buffer.length);

                    uSocket.receive(request);
                    System.out.println("Backup server waiting for his turn...");

                } catch (IOException ex) {
                    break;
                }

            }
        } catch (SocketException ex) {
            Logger.getLogger(UDPReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        uSocket.close();

    }


}*/

class UDPService extends Thread {

    TCPServerImpl tcpServer;
    String ip;
    DatagramSocket aSocket = null;
    DatagramPacket recebe, envia, enviaTransiente;
    String texto = "ping";
    byte[] msgIn = new byte[10];
    byte[] msgOut;


    public UDPService(TCPServerImpl tcpServer, String ip) {
        this.tcpServer = tcpServer;
        this.ip = ip;
    }

    public void run() {
        try {

            int count = 0;

            Properties props = new Properties();

            props.load(new FileInputStream("support/property"));
            msgOut = texto.getBytes(); //
            InetAddress aHost = InetAddress.getByName(ip); //
            aSocket = new DatagramSocket(); //
            aSocket.setSoTimeout(5000); //

            System.out.println("Backup Server Ready!"); //


            while (true) {  // While enquanto Backup
                // Envia
                try {
                    //
                    envia = new DatagramPacket(msgOut, msgOut.length, aHost, Integer.parseInt(props.getProperty("udpPort")));
                    aSocket.send(envia);

                    recebe = new DatagramPacket(msgIn, msgIn.length);           // cria um datagram packet
                    aSocket.receive(recebe);                                // fica a aguardar dados
                    String conteudo = new String(recebe.getData(), 0, recebe.getLength());

                } catch (SocketException e) {
                    System.out.println("Socket Exception");
                } catch (UnknownHostException e) {
                    System.out.println("Unknown Host Exception");
                } catch (IOException e) {


                    enviaTransiente = new DatagramPacket(msgOut, msgOut.length, aHost, Integer.parseInt(props.getProperty("udpPort")));
                    aSocket.send(enviaTransiente);
                    count++;
                    if (count == 3) {
                        System.out.println("Changing to primary server...");
                        System.out.println("Primary Server Ready!");
                        tcpServer.switchToMaster(true);


                    }


                    break;
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } //while

            aSocket = new DatagramSocket(Integer.parseInt(props.getProperty("udpPort")));
        } catch (SocketException ex) {
            System.out.println("Socket Exception");
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host Exception");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //quando muda para master, esperar forever por um pacote
        try {
            aSocket.setSoTimeout(0);
        } catch (SocketException ex) {
            System.out.println("Socket Exception");
        }

        while (true) {  // While enquanto Master
            // RECEBE
            try {
                recebe = new DatagramPacket(msgIn, msgIn.length);           // cria um datagram packet
                aSocket.receive(recebe);                                // fica a aguardar dados
                String text = new String(recebe.getData(), 0, recebe.getLength());

                msgOut = text.getBytes();
                envia = new DatagramPacket(msgOut, msgOut.length, recebe.getAddress(), recebe.getPort());
                aSocket.send(envia);
            } catch (Exception e) {
                e.printStackTrace();

            }

        } //while
    }

}
