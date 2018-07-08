/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 *
 * @author danie
 */
public class Recognition extends Thread {
    private DatagramPacket receivePacket;
    private byte[] buffer = new byte[256];
    private final MulticastSocket rede;
    
    public Recognition(MulticastSocket rede) throws SocketException{
        this.rede = rede;
        this.buffer = new byte[256];
    }

    @Override
    public void run(){
        while(true){
            try{
                
                receivePacket = new DatagramPacket(buffer, buffer.length);
                rede.receive(receivePacket);
                System.out.println(new String(buffer));
            
            }catch(IOException ex){
                System.out.println("Error thread recognition: " + ex);
            }   
        }
    }
}
