/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.util.List;




/**
 *
 * @author chico
 */
public class CausalOrder {
    public List<Message> MessageBuffer;
    private ICausalMulticast application;
    
    
    public CausalOrder(ICausalMulticast application){
        this.application = application;
    }
    
    
    private void MessageToGUI(){
        //application.deliver(user, msg);
    } 
}
