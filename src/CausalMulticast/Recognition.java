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
import java.util.Arrays;

/**
 *
 * @author danie
 */
public class Recognition extends Thread {
    private DatagramPacket receivePacket;
    private byte[] buffer;
    private final MulticastSocket rede;
    private final int PORTA = 2020;
    private final InetAddress IP_MULTICAST = InetAddress.getByName("224.0.0.1");
    
    public Recognition() throws IOException{
        this.rede = new MulticastSocket(PORTA);
        this.buffer = new byte[256];
    }

    @Override
    public void run(){
        try {
            rede.joinGroup(IP_MULTICAST);
        } catch (IOException ex) {
            System.out.println("Error join thread: " + ex);
        }
        
        String[] info;
        String action;
        String user;
        String group;
        
        while(true){
            try{
                
                receivePacket = new DatagramPacket(buffer, buffer.length);
                rede.receive(receivePacket);
                
                info   = new String(buffer).split("-"); 
                action = info[0];
                user   = info[1];
                group  = info[2];
            
            }catch(IOException ex){
                System.out.println("Error thread recognition: " + ex);
            }   
        }
    }
}
