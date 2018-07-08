/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 *
 * @author danie
 */
public class CMChannel {
    private MulticastSocket rede;
    private final int PORTA = 2020;
    private final String IP = "223.0.0.1";
    private final ICausalMulticast application;
    
    
    public CMChannel(ICausalMulticast application) throws IOException{
        rede = new MulticastSocket(PORTA);
        this.application = (ICausalMulticast) application;
    }
    
    public void join(String user, String dest) throws UnknownHostException, IOException{
        
        rede.joinGroup(InetAddress.getByName(user));        
    }
    
    public void leave(String user, String dest) throws UnknownHostException, IOException{
        rede.leaveGroup(InetAddress.getByName(user));        
    }
    
    public void mcSend(String msg, String dest){
       this.application.deliver(msg, dest); 
        
    }
}