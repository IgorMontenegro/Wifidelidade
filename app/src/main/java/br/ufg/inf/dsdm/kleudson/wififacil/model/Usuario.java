package br.ufg.inf.dsdm.kleudson.wififacil.model;

import io.realm.RealmObject;

/**
 * Created by Jean on 11/20/16.
 */


public class Usuario extends RealmObject{

    private String nome;
    private String senha;

    public Usuario() {

    }

    public Usuario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
