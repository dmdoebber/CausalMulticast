/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author danie
 */
public class CMChannel{
    private final InetAddress IP_MIDDLEWARE = InetAddress.getByName("230.0.0.1");
    private final int PORT_MIDDLEWARE = 2020;
    private final int PORT_MESSAGE = 3030;
    
    private final DatagramSocket socket;
    private final MulticastSocket rede;
    private DatagramPacket sendPacket;
    
    private final ICausalMulticast application;
    private final Recognition recognition;
    private final Receiver receiver;
    
    public List<String> userList;
    
    public CausalOrder causalOrder;
    
    
    private final String MyIP = InetAddress.getLocalHost().getHostAddress();
    public int MyID;
    
    
    public CMChannel(ICausalMulticast application) throws IOException{
       
        rede = new MulticastSocket(PORT_MIDDLEWARE);
        socket = new DatagramSocket(PORT_MESSAGE);
        
        this.application = (ICausalMulticast) application;
        this.userList = new ArrayList();
                
        causalOrder = new CausalOrder(application, MyIP);
        
        recognition = new Recognition(this);
        recognition.start();
        
        receiver = new Receiver(socket, causalOrder);
        receiver.start();
    }
    
    public void join(String user, String dest) throws IOException{
        rede.joinGroup(IP_MIDDLEWARE); 
         
        this.recognition.MyGroup = dest;
        
        String msg = "join" + "-" + user + "-" + dest + "-";
        
        sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), IP_MIDDLEWARE, PORT_MIDDLEWARE);
        rede.send(sendPacket);
    }   
    
    public void leave(String user, String dest) throws IOException{
        this.userList.clear();
        recognition.MyGroup = "";
        
        String msg = "leave" + "-" + user + "-" + dest + "-";
        
        sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), IP_MIDDLEWARE, PORT_MIDDLEWARE);
        rede.send(sendPacket);
        
        rede.leaveGroup(IP_MIDDLEWARE);    
    }
    
    public void mcSend(String msg, String dest) throws IOException{
        InetAddress IP;
        Message message;
        
        String stringFail = JOptionPane.showInputDialog("Digite o numero do usuario para não enviar!", -1);
        
        int fail = Integer.parseInt(stringFail);
        String IPFail = "";
        
        if(fail <= userList.size() && fail > 0)
            IPFail = userList.get(fail);
        
        for(int i = 0; i < userList.size(); i++){
            
            if(IPFail.equals(userList.get(i))) continue;
            System.out.println("aqui "+IPFail + " "+ userList.get(i));
            
            IP = InetAddress.getByName(userList.get(i));
            message = new Message(MyIP, msg);
            message.vectorClock = causalOrder.get_Relogio();
            
            message.toString();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            
            oos.writeObject(message);
            
            byte[] data = baos.toByteArray();
            
            sendPacket = new DatagramPacket(data, data.length, IP, PORT_MESSAGE);
            socket.send(sendPacket);
        }
        
        this.causalOrder.somar_Relogio();
    }
}
