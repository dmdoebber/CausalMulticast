/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 *
 * @author danie
 */
public class CMChannel{
    private final int PORTA = 2020;
    private final InetAddress IP_MIDDLEWARE = InetAddress.getByName("224.0.0.1");
    
    private final MulticastSocket rede;
    private DatagramPacket sendPacket;
    
    private final ICausalMulticast application;
    private final Recognition recognition;
    
    private final String MyIP = InetAddress.getLocalHost().getHostAddress();
    
    
    public CMChannel(ICausalMulticast application) throws IOException{
        
        rede = new MulticastSocket(PORTA);
        this.application = (ICausalMulticast) application;
        
        recognition = new Recognition();
        recognition.start();
    }
    
    public void join(String user, String dest) throws IOException{
        rede.joinGroup(IP_MIDDLEWARE); 
        
        String msg = "join" + "-" + user + "-" + dest;
        
        sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), IP_MIDDLEWARE, PORTA);
        rede.send(sendPacket);
    }   
    
    public void leave(String user, String dest) throws IOException{
        
        String msg = "leave" + "-" + user + "-" + dest;
        
        sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), IP_MIDDLEWARE, PORTA);
        rede.send(sendPacket);
        
        rede.leaveGroup(IP_MIDDLEWARE);    
    }
    
    public void mcSend(String msg, String dest) throws IOException{
       this.application.deliver(MyIP, msg); //mudar
    }
}
