/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author danie
 */
public class Receiver extends Thread{
    private final int PORTA = 3030;
    
    private final DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private byte[] receiveData;
    
    public Receiver() throws SocketException{
        serverSocket = new DatagramSocket(PORTA);
        
    }
    
    @Override
    public void run(){
        
        while(true){
            try {
            
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
         
            } catch (IOException ex) {
                System.out.println("Error thread receiver " + ex);
            }
        }
    }
}
