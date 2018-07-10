/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.util.Map;

/**
 *
 * @author xico
 */
public class Buffer {
    private String mensagem;
    private String user;
    private boolean entregue;
    private Map<String, Integer> vetorRecebido;

    /**
     * @return the mensagem
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * @param mensagem the mensagem to set
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    /**
     * @return the id
     */
    public String getUser() {
        return user;
    }

    /**
     * @param id the id to set
     */
    public void setUser(String id) {
        this.user = id;
    }

    /**
     * @return the entregue
     */
    public boolean isEntregue() {
        return entregue;
    }

    /**
     * @param entregue the entregue to set
     */
    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    /**
     * @return the vetorRecebido
     */
    public  Map<String, Integer> getVetorRecebido() {
        return vetorRecebido;
    }

    /**
     * @param vetorRecebido the vetorRecebido to set
     */
    public void setVetorRecebido(Map<String, Integer> vetorRecebido) {
        this.vetorRecebido = vetorRecebido;
    }
}
