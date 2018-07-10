/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author danie
 */
public class Message implements Serializable{
    public Integer[] vectorClock;
    public String Message;
    public String Group;
    
    public Message(String Message, String Group, Integer[] vectorClock){
        this.Message = Message;
        this.Group = Group;
        this.vectorClock = vectorClock;
    }
    
    
    @Override
    public String toString(){
        return Message + " " +  Arrays.toString(vectorClock);
    }
}
