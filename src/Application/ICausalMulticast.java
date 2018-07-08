/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Application;

/**
 *
 * @author danie
 */
public interface ICausalMulticast {

    /**
     *
     * @param user usuario que está enviando a mensagem 
     * @param msg mensagem enviada pelo usuario
     */
    public void deliver(String user, String msg);
}
