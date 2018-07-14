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
    
    /* construtor da classe */
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
    
    /* metodo para verificar se é possível trocar os valores do vetor*/
     private boolean checkClock(Map<String, Integer> receiveClock) {
        for (String key : receiveClock.keySet()){
            int myClock = (int) this.vectorClock.get(key);
            int reClock = (int) receiveClock.get(key);
            
            if(reClock > myClock)
                return false; 
        }       
        return true;
    }
    
     /*  Metodo que analisa se as mensagens do buffer podem ser entregues ao destinatario  */
    private void ver_entrega_Buffer() {
        
        for(Message message : bufferMessages){
            if( this.checkClock(message.vectorClock) && !message.delivery){
                application.deliver(message.user + ": " + message.Message);
                message.delivery = true;
            }
        }
    }
    /*
        Ordena as mensagens de acordo com a ordem causal
    */
    public void ordenar_mensagem_Receive(Message message) {
        this.bufferMessages.add(message);
       
         // Chama metodo que verifica se o buffer tem mensagens a serem entregues
        this.ver_entrega_Buffer();
        
        if(!message.user.equals(this.IP)){
            Iterator myVector = vectorClock.entrySet().iterator();

            while(myVector.hasNext()){
                Map.Entry m = (Map.Entry) myVector.next();
                if(m.getKey().equals(message.user)){
                    int clock =  (int) m.getValue();
                    vectorClock.replace(message.user, clock + 1);
                }
            }      
        }
        
       
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
}
