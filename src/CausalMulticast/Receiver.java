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
    private final int PORTA = 3030;
    
    private final DatagramSocket serverSocket;
    private DatagramPacket receivePacket;
    private byte[] receiveData;
    
    public Receiver(DatagramSocket serverSocket) throws SocketException{
        this.serverSocket = serverSocket;
        receiveData = new byte[1024];
    }
    
    @Override
    public void run(){
        
        while(true){
            try {
            
                receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                
                byte[] data = receivePacket.getData();
                
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                
                Message message = (Message) ois.readObject();
                
                System.out.println(message);

                //adicionar a lista de mensagens recebidas

            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Error thread receiver " + ex);
            }
        }
    }
}