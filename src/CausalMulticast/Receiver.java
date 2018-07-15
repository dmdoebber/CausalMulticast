/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 *
 * @author danie
 */
public class Receiver extends Thread{
    private final DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private byte[] receiveData;
    private final CausalOrder causalOrder;
    
    public Receiver(DatagramSocket serverSocket, CausalOrder causalOrder) throws SocketException{
        this.serverSocket = serverSocket;
        this.causalOrder = causalOrder;     
    }
    
    @Override
    public void run(){
        while(true){
            try {
                receiveData = new byte[1024];
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                
                byte[] data = receivePacket.getData();
                
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                
                Message message = (Message) ois.readObject();
                
                causalOrder.receiveMessages(message);
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error thread receiver " + ex);
            }
        }
    }
}
