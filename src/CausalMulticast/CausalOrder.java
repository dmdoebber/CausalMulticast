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
 * @author danie
 */
public class CausalOrder {
    private final Map<String, Integer> vectorClock;
    private final ArrayList<Message> bufferMessages;
    
    private final ICausalMulticast application;
    
    public CausalOrder(ICausalMulticast application){
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
        for(String key : receiveClock.keySet()){
            if(!vectorClock.get(key).equals(receiveClock.get(key)))
                return false;
        }
        return true;
    }
    
    public void receiveMessages(Message message) {
        boolean Continue = true;
        
        if(this.checkClock(message.vectorClock)){
            this.ClockPlusPlus(message.IP);
            this.application.deliver(message.IP + ": " + message);
            
            if(!bufferMessages.isEmpty()){
                while(Continue){
                    Continue = false;
                    for(Message msg : bufferMessages){
                        if(this.checkClock(msg.vectorClock)){
                            this.ClockPlusPlus(msg.IP);
                            this.application.deliver(msg.IP + ": " + msg);
                            this.bufferMessages.remove(msg);
                            
                            Continue = true;                    
                            break;
                        }
                    }                    
                }        
            }
        }else
            bufferMessages.add(message);
        
        this.printBufferMessages();
    }
     
    public void ClockPlusPlus(String IP) {
        int clock = vectorClock.get(IP);
        vectorClock.replace(IP, clock + 1);
    }
    
    public void printVectorClock() {
        System.out.println("My Vector Clock!");
        for(Map.Entry m  : vectorClock.entrySet())
            System.out.println(m.getKey() + " [" + m.getValue() + "]");
    }
    
    private void printBufferMessages(){
        System.out.print("buffer = [");
        for(Message message : bufferMessages)
            if(!message.delivery)
                System.out.print(message);
        System.out.println("];");
    }
    
    public Map copy(){
        Map<String, Integer> copy = new HashMap();
        for(String key : vectorClock.keySet())
            copy.put(key, vectorClock.get(key));
        return copy;
    }
}
