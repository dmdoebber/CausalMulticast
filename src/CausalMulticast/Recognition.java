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
public class Recognition extends Thread {
    private final InetAddress IP_MIDDLEWARE = InetAddress.getByName("230.0.0.1");
    private final int PORTA = 2020;
    
    private DatagramPacket receivePacket;
    private final MulticastSocket rede;
    
    private byte[] buffer;
    
    private final CMChannel chanel;
    
    private final String MyIP = InetAddress.getLocalHost().getHostAddress();
    public String MyGroup = "";
    
    public Recognition(CMChannel chanel) throws IOException{
        this.chanel = chanel;
        this.rede = new MulticastSocket(PORTA);
    }

    @Override
    public synchronized void run(){
        try {
            rede.joinGroup(IP_MIDDLEWARE);
        } catch (IOException ex) {
            System.out.println("Error join thread: " + ex);
        }
        
        DatagramPacket sendPacket;
        
        String[] info;
        String action;
        String IP;
        String group;        
        
        while(true){
            try{
                this.buffer = new byte[256];
                receivePacket = new DatagramPacket(buffer, buffer.length);
                rede.receive(receivePacket);
                
                info   = new String(buffer).split("-"); 
                action = info[0];
                IP     = info[1];
                group  = info[2];
                
                if(MyGroup.equals(group)){
                    switch (action) {
                        case "join":
                            if(!chanel.userList.contains(IP)){
                                chanel.userList.add(IP); 
                                chanel.causalOrder.AddUserTOClock(IP);
                            }
                            String msg = "inGroup" + "-" + MyIP + "-" + group + "-";
                            sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), IP_MIDDLEWARE, PORTA);
                            rede.send(sendPacket);
                            break;
                            
                        case "leave":
                            chanel.userList.remove(IP);
                            chanel.causalOrder.RemoveUserTOClock(IP);
                            break;
                            
                        case "inGroup":
                            if(!IP.equals(MyIP) && !chanel.userList.contains(IP)){
                                chanel.userList.add(IP); 
                                chanel.causalOrder.AddUserTOClock(IP);
                            }
                            break;
                        default:
                            System.out.println("Função inválida!");
                            break;
                    }
                }
            }catch(IOException ex){
                System.out.println("Error thread recognition: " + ex);
            }   
        }
    }
}
