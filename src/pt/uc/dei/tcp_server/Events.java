package pt.uc.dei.tcp_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;

public class Events extends Thread {

    ObjectInputStream in;
    ObjectOutputStream out = null;
    Socket clientSocket;
    int thread_number;
    String username = null;
    LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<>();
    TCPServerImpl tcpServer;

    public Events(Socket aClientSocket, int numero, TCPServerImpl tcpServer) {
        this.thread_number = numero;
        this.tcpServer = tcpServer;


        try {
            this.clientSocket = aClientSocket;
            this.in = new ObjectInputStream(clientSocket.getInputStream());
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Connection:" + e.getMessage());
        }
    }

    public void sendOut(Message mensagem) {
        try {
            synchronized (out) {

                out.writeObject(mensagem);
                out.flush();
                out.reset();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void putMsgIntoQueue(Message msg) {
        queue.offer(msg);

    }


    public void run() {



        while (true) {

            try {

                Message mensagemAux = (Message) in.readObject();

                switch (mensagemAux.getTipo()) {


                    case (Message.SENDTOHASH):
                        //assim que envia a mensagem para o tcpserver para o user ser colocado na Hashtable de user online, é logo definido o username da thread em si, para mais tarde utilizar o sendMsg()

                        this.username = mensagemAux.username;
                        tcpServer.put(mensagemAux.username, this);
                        break;

                    //TODO passar isto para a connection possivelmente
                    case (Message.CHECKONLINE):
                        mensagemAux.data = "Members currently online:\n";
                        Enumeration e = tcpServer.keys();
                        while (e.hasMoreElements()) {

                            mensagemAux.data += e.nextElement();

                        }
                        sendOut(mensagemAux);
                        break;

                    case (Message.SENDINVITATIONS):


                        break;

                    case (Message.CHECKINVITATIONS):


                        break;

                    case (Message.CHECKCHATMESSAGES):

                        break;



                }


            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }


    }


}
