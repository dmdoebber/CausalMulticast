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
    
    private final InetAddress IP_MIDDLEWARE = InetAddress.getByName("224.0.0.1");
    private final int PORTA = 2020;
    
    private DatagramPacket receivePacket;
    private MulticastSocket rede;
    
    private byte[] buffer;
    
    private CMChannel chanel;
    
    public String MyGroup;
    private final String MyIP = InetAddress.getLocalHost().getHostAddress();
    
    public Recognition(CMChannel chanel) throws IOException{
        this.MyGroup = "";
        this.chanel = chanel;
        this.rede = new MulticastSocket(PORTA);
    }

    @Override
    public void run(){
        try {
            rede.joinGroup(IP_MIDDLEWARE);
        } catch (IOException ex) {
            System.out.println("Error join thread: " + ex);
        }
        
        DatagramPacket sendPacket;
        
        String[] info;
        String action;
        String user;
        String group;
        
        while(true){
            try{
                this.buffer = new byte[256];
                receivePacket = new DatagramPacket(buffer, buffer.length);
                rede.receive(receivePacket);
                
                info   = new String(buffer).split("-"); 
                action = info[0];
                user   = info[1];
                group  = info[2];
                
                System.out.println(Arrays.toString(info));
                
                if(MyGroup.equalsIgnoreCase(group)){
                    switch (action) {
                        case "join":
                            if(!chanel.userList.contains(user))
                                chanel.userList.add(user); 
                            
                            String msg = "inGroup" + "-" + MyIP + "-" + group + "-";
                            sendPacket = new DatagramPacket(msg.getBytes(), msg.length(), IP_MIDDLEWARE, PORTA);
                            rede.send(sendPacket);
                            break;
                        case "leave":
                            chanel.userList.remove(user);
                            break;
                            
                        case "inGroup":
                            if(!user.equals(MyIP) && !chanel.userList.contains(user)){
                                chanel.userList.add(user); 
                                System.out.println("add "+ user);
                            }
                            break;
                        default:
                            System.out.println("Função inválida!");
                            break;
                    }
                }
                
                System.out.println(chanel.userList);
            }catch(IOException ex){
                System.out.println("Error thread recognition: " + ex);
            }   
        }
    }
}
