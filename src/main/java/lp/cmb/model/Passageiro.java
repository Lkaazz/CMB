/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.cmb.model;

/**
 *
 * @author Lucas
 */
public class Passageiro {
    private String nome;
    private String email;
    
    public Passageiro(String nome, String email){
        this.nome = nome;
        this.email = email;
    }

    public String getNomePassageiro() {
        return nome;
    }
    public String getEmailPassageiro() {
        return email;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
