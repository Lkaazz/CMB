/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.cmb.model;

public final class Linhas {
    private String numeroLinhas;
    private String destino;
    
    public Linhas(String numeroLinhas, String destino){
        this.numeroLinhas = numeroLinhas;
        this.destino = destino;
    }

    public String getNumeroLinhas() {
        return numeroLinhas;
    }
    
    public String getDestino() {
        return destino;
    }
}
