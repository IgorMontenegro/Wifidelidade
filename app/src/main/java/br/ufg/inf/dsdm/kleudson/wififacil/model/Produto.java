package br.ufg.inf.dsdm.kleudson.wififacil.model;

import io.realm.RealmObject;

/**
 * Created by Jean on 11/20/16.
 */

public class Produto extends RealmObject{

    private String nome;
    private double preco;

    public Produto() {
    }

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
