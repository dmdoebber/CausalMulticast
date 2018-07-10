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
    public String user;
    
    public Message(String user, String Message){
        this.delivery = false;
        this.Message = Message;
        this.user = user;
    }
    
    @Override
    public String toString(){
        return Message;
    }
}
