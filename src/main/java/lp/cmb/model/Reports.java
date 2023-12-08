/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lp.cmb.model;

/**
 *
 * @author Lucas
 */
public class Reports {
        private final String nomePassageiro;
        private final String descricao;
        private final String dataHora;

        public Reports(String nomePassageiro, String descricao, String dataHora) {
            this.nomePassageiro = nomePassageiro;
            this.descricao = descricao;
            this.dataHora = dataHora;
        }

        public String getNomePassageiro() {
            return nomePassageiro;
        }

        public String getDescricao() {
            return descricao;
        }

        public String getDataHora() {
            return dataHora;
        }
    }

