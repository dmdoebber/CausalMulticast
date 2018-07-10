/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

/**
 *
 * @author xico
 */
public class Buffer {
    private String mensagem;
    private String user;
    private int id;
    private boolean entregue;
    private Integer[] vetorRecebido;

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
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
    public Integer[] getVetorRecebido() {
        return vetorRecebido;
    }

    /**
     * @param vetorRecebido the vetorRecebido to set
     */
    public void setVetorRecebido(Integer[] vetorRecebido) {
        this.vetorRecebido = vetorRecebido;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
