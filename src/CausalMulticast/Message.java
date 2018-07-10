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
    public String Message;
    public String Group;
    
    public Message(String Message, String Group){
        this.Message = Message;
        this.Group = Group;
    }
    
    
    @Override
    public String toString(){
        return Message + " " + "clock";
    }
}
