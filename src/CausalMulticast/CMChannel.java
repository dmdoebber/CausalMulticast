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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private final Recognition recognition;
    private final Receiver receiver;
    
    public List<String> userList;
    public CausalOrder causalOrder;
    
    private final Map<Message, String> delayedMessages;
    
    private final String MyIP = InetAddress.getLocalHost().getHostAddress();
        
    public CMChannel(ICausalMulticast application) throws IOException{       
        rede = new MulticastSocket(PORT_MIDDLEWARE);
        socket = new DatagramSocket(PORT_MESSAGE);
        
        userList = new ArrayList();
        delayedMessages = new HashMap();
                
        causalOrder = new CausalOrder(application);
        
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
        recognition.MyGroup = "";
        this.userList.clear();        
        
        String msg = "leave" + "-" + user + "-" + dest + "-";
        
        sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), IP_MIDDLEWARE, PORT_MIDDLEWARE);
        rede.send(sendPacket);
        
        rede.leaveGroup(IP_MIDDLEWARE);    
    }
    
    public void mcSend(String msg, String dest) throws IOException{
        InetAddress IP;
        Message message;
        
        String IPFail = JOptionPane.showInputDialog("DIGITE UM IP PARA NÃO ENVIAR A MENSAGEM!", "192.168.0.");
        
        for(int i = 0; i < userList.size(); i++){
            message = new Message(MyIP, msg, causalOrder.copy());
            
            if(IPFail.equals(userList.get(i))){
                delayedMessages.put(message, userList.get(i));
                continue;
            }             
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message);
            byte[] data = baos.toByteArray();
            
            IP = InetAddress.getByName(userList.get(i));
            sendPacket = new DatagramPacket(data, data.length, IP, PORT_MESSAGE);
            socket.send(sendPacket);
        }
        
        if(!delayedMessages.isEmpty()){
            if(JOptionPane.showConfirmDialog(null, "DESEJA ENVIAR AS MENSAGENS EM ESPERA?\n"+delayedMessages) == 0){
                for(Message me : delayedMessages.keySet()){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(me);
                    byte[] data = baos.toByteArray();
                    
                    IP = InetAddress.getByName(delayedMessages.get(me));
                    sendPacket = new DatagramPacket(data, data.length, IP, PORT_MESSAGE);
                    socket.send(sendPacket);
                }
                delayedMessages.clear();
            }
        }
    }
}
