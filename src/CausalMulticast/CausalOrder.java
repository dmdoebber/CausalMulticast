/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author danie && xico
 */
public class CausalOrder {
    private final Map<String, Integer> vectorClock;
    private final ArrayList<Message> bufferMessages;
    
    private final String IP;
    
    private final ICausalMulticast application;
    
    public CausalOrder(ICausalMulticast application, String IP){
        this.IP = IP;
        this.application =  application;
        
        this.bufferMessages = new ArrayList();
        this.vectorClock = new HashMap();
    }
    
    public void AddUserTOClock(String IP){
        vectorClock.put(IP, 0);
    }
    
    public void RemoveUserTOClock(String IP){
        vectorClock.remove(IP);
    }
    
    private boolean checkClock(Map<String, Integer> receiveClock) {
        for (String key : receiveClock.keySet()){
            int myClock = (int) this.vectorClock.get(key);
            int reClock = (int) receiveClock.get(key);
            
            if(reClock > myClock)
                return false; 
        }       
        return true;
    }
    
    private void checkDelivery() {
        for(Message message : bufferMessages){
            if(message.delivery)
                continue;
            
            if( this.checkClock(message.vectorClock)){               
                application.deliver(message.IP + ": " + message.Message);
                message.delivery = true;
            }
        }
    }

    public void receiveMessages(Message message) {
        this.bufferMessages.add(message);
        
        if(!IP.equals(message.IP)){
            int clock = vectorClock.get(message.IP);
            vectorClock.replace(message.IP, clock + 1);
        }
        
        this.printVectorClock();
        
        this.checkDelivery();
    }
     
    // SOMA RELOGIO NO MEU ID
    public void ClockPP() {
        int clock = vectorClock.get(IP);
        vectorClock.replace(IP, clock + 1);
    }
    
    public Map<String, Integer> getClock() {
        return this.vectorClock;
    }
    
    public void printVectorClock() {
        for(Map.Entry m  : vectorClock.entrySet())
            System.out.println(m.getKey() + " [" + m.getValue() + "]");
    }
}
