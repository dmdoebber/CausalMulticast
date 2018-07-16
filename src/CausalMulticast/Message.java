/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author danie
 */
public class Message implements Serializable{
    public Map<String, Integer> vectorClock;
    public boolean delivery;
    public String Message;
    public String IP;
    
    public Message(String IP, String Message, Map<String, Integer> vectorClock){
        this.IP = IP;
        this.Message = Message;
        this.vectorClock = vectorClock;        
        this.delivery = false;
    }
    
    @Override
    public String toString(){
        return Message + " ";
    }
}
