/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CausalMulticast;

import java.util.ArrayList;


/**
 *
 * @author xico
 */
public class CausalOrder {

    
    private Integer[] Vetor_rel;
    private int Id;
    private ArrayList<Buffer> Mensagens;
    private final ICausalMulticast application;
    
    //ChatGUI view
    /* construtor da classe */
    public CausalOrder(int id,ICausalMulticast application ){
        this.Vetor_rel = new Integer[20];
        this.Id = id;
        this.Mensagens = new ArrayList();
        this.application =  application;
        this.inicializar_Vet();
    
    }
    /* metodo para inicializar vetor com 0 */
    public void inicializar_Vet(){
        for(int i = 0; i < this.Vetor_rel.length; i++) {
                this.Vetor_rel[i] = 0;   
        }
    }
    
    /* metodo para atualizar o vetor quando é recebido algum valor*/
    private void atualiza_Relogio_no_Receive(Integer[] vetor_Recebido) {
        for(int i = 0; i < Vetor_rel.length; i++) {
            if(i != this.Id) {
                this.Vetor_rel[i] = Math.max(this.Vetor_rel[i], vetor_Recebido[i]);
            }
        }
    }
    /* metodo para verificar se é possível trocar os valores do vetor*/
     private boolean verificar_Relogio(Integer[] vetor_Recebido, int id_Recebido) {
        for(int i = 0; i < vetor_Recebido.length; i++) {
            if(i != id_Recebido) {
                if(vetor_Recebido[i] > this.Vetor_rel[i])
                    return false;
            }
        }
        return true;
    }
    
     /*  Metodo que analisa se as mensagens do buffer podem ser entregues ao destinatario  */
    private void ver_entrega_Buffer() {
        for(int i = this.Mensagens.size()-1; i >= 0 ;i--){
            Buffer b = this.Mensagens.get(i);
            
            //verifica se a mensagem pode mas ainda nao foi entregue
            if(this.verificar_Relogio(b.getVetorRecebido(),b.getId()) && !b.isEntregue()) {
                b.setEntregue(true);
                String m = b.getUser() + ": " + b.getMensagem()+ "\n";
               
                //mostrar na tela daniel
               // this.application.getjTextAreaMensagens().append(m);
            } 
        }
    }
    
    
    /*
        Ordena as mensagens de acordo com a ordem causal
    */
    public void ordenar_mensagem_Receive(String mensagem ,String remetente, int id_Recebido, Integer[] vetorRecebido) {
        // Salva mensagem no buffer
        Buffer b = new Buffer();
        b.setMensagem(mensagem);
        b.setUser(remetente);
        b.setEntregue(false);
        b.setId(Id);
        b.setVetorRecebido(vetorRecebido);
        this.Mensagens.add(b);
        
        
        if(id_Recebido != this.Id) {
            this.Vetor_rel[this.Id]++;
        }
        
        // Chama metodo que verifica se o buffer tem mensagens a serem entregues
        this.ver_entrega_Buffer();
    }
     
    /*     Metodo para somar mais um ao relogio no id atual  */
    public void somar_Relogio() {
         this.Vetor_rel[this.Id]++;     
    }
    
    /*  Metodo para retornar os valores do relogio    */ 
    public Integer[] get_Relogio() {
        return this.Vetor_rel;
    }
    
    /*  Metodo para imprimir os valores do relogio    */
    public void imprimir_Vetor() {
        for (Integer Vetor_rel1 : this.Vetor_rel) {
            System.out.print(Vetor_rel1);
        }
    }
}
