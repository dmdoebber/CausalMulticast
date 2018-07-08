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
public class CausalMulticast {
    private MulticastSocket rede;
    private final int PORTA = 2020;
    private final String IP = "223.0.0.1";
    
    
    public CausalMulticast() throws IOException{
        rede = new MulticastSocket(PORTA);
    }
    
    public void join(String user, String dest) throws UnknownHostException, IOException{
        
        rede.joinGroup(InetAddress.getByName(user));        
    }
    
    public void leave(String user, String dest) throws UnknownHostException, IOException{
        rede.leaveGroup(InetAddress.getByName(user));        
    }
    
    public void mcSend(String msg, String dest){
        
        
    }
}
