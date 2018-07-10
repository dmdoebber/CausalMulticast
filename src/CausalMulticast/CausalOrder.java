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
    private Map<String, Integer> vectorClock;
    private String IP;
    private ArrayList<Message> Messages;
    private final ICausalMulticast application;
    
    /* construtor da classe */
    public CausalOrder(ICausalMulticast application ){
        this.Messages = new ArrayList();
        this.application =  application;
        this.vectorClock = new HashMap();
    }
    /* metodo para inicializar vetor com 0 */
    public void AddUserTOClock(String IP){
        this.IP = IP;
        vectorClock.put(IP, 0);
        this.imprimir_Vetor();
    }
    
    public void RemoveUserTOClock(){
        
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
     private boolean verificar_Relogio(Map<String, Integer> receiveClock, String ipReceive) {
        Iterator myVector = vectorClock.entrySet().iterator();
        Iterator receiveVector = receiveClock.entrySet().iterator();
        
        while(myVector.hasNext() && receiveVector.hasNext()){
            Map.Entry m = (Map.Entry) myVector.next();
            Map.Entry r = (Map.Entry) receiveVector.next();
            
            String id = (String) r.getKey();
                 
            if(!id.equals(ipReceive)){
                int clock1 = (int) m.getValue();
                int clock2 = (int) r.getValue();
                if(clock2 > clock1)
                    return false;       
            }
        }
        return true;
    }
    
     /*  Metodo que analisa se as mensagens do buffer podem ser entregues ao destinatario  */
    private void ver_entrega_Buffer() {
        for(int i = this.Messages.size()-1; i >= 0 ;i--){
            Message message = this.Messages.get(i);
            
            //verifica se a mensagem pode mas ainda nao foi entregue
            if(this.verificar_Relogio(message.vectorClock, message.user) && !message.delivery) {
                message.delivery = true;
                String m = message.user + ": " + message.Message;
               
                //mostrar na tela daniel
               this.application.deliver(m);
            } 
        }
    }
    /*
        Ordena as mensagens de acordo com a ordem causal
    */
    public void ordenar_mensagem_Receive(Message message) {
        this.Messages.add(message);
        
        if(!message.user.equals(this.IP)){
            this.somar_Relogio();     
        }
        
        // Chama metodo que verifica se o buffer tem mensagens a serem entregues
        this.ver_entrega_Buffer();
    }
     
    /*     Metodo para somar mais um ao relogio no id atual  */
    public void somar_Relogio() {
        Iterator myVector = vectorClock.entrySet().iterator();

        while(myVector.hasNext()){
            Map.Entry m = (Map.Entry) myVector.next();
            if(m.getKey().equals(IP)){
                int clock =  (int) m.getValue();
                vectorClock.replace(IP, clock + 1);
            }
        }  
        this.imprimir_Vetor();
    }
    
    /*  Metodo para retornar os valores do relogio    */ 
    public Map<String, Integer> get_Relogio() {
        return this.vectorClock;
    }
    
    /*  Metodo para imprimir os valores do relogio    */
    public void imprimir_Vetor() {
        for(Map.Entry m  : vectorClock.entrySet()){
            String ip = (String) m.getKey();
            int clock = (int) m.getValue();   
            System.out.println(ip + " [" + clock + "]");
        }
    }
}
