/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
            if( this.checkClock(message.vectorClock) && !message.delivery){
                
                application.deliver(message.IP + ": " + message.Message);
                message.delivery = true;
            }
        }
    }

    //when Pi receives msg from Pj
    public void receiveMessages(Message message) {
        this.bufferMessages.add(message);
        
        if(!IP.equals(message.IP)){
            int clock = vectorClock.get(message.IP);
            vectorClock.replace(message.IP, clock + 1);
        }
        
        this.checkDelivery();
    }
     
    /*     Metodo para somar mais um ao relogio no id atual  */
    public void somar_Relogio() {
        int clock = vectorClock.get(IP);
        vectorClock.replace(IP, clock + 1);
    }
    
    public Map<String, Integer> getClock() {
        return this.vectorClock;
    }
    
    public void printVector() {
        
        for(Map.Entry m  : vectorClock.entrySet())
            System.out.println(m.getKey() + " [" + m.getValue() + "]");
    }
    
    
    
    
    /* metodo para atualizar o vetor quando é recebido algum valor*/
    private void atualiza_Relogio_no_Receive(Map<String, Integer> receiveClock) {
        Iterator myVector = vectorClock.entrySet().iterator();
        Iterator receiveVector = receiveClock.entrySet().iterator();
        
        while(myVector.hasNext() && receiveVector.hasNext()){
            Map.Entry m = (Map.Entry) myVector.next();
            Map.Entry r = (Map.Entry) receiveVector.next();
            
            String id1 = (String) m.getKey();
            String id2 = (String) r.getKey();
            
            if(!id1.equals(id2)){
                int clock1 = (int) m.getValue();
                int clock2 = (int) r.getValue();
                
                vectorClock.replace(id1, Math.max(clock1, clock2));
            }
        }
    }
}
