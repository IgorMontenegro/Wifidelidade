package br.ufg.inf.dsdm.kleudson.wififacil.model;

import java.util.ArrayList;

/**
 * Created by Jean on 11/20/16.
 */

public class Campanha {
    private String Nome;
    private Usuario usuario;
    private ArrayList<Produto> listaProdutos;

    public Campanha(String nome, Usuario usuario, ArrayList<Produto> listaProdutos) {
        Nome = nome;
        this.usuario = usuario;
        this.listaProdutos = listaProdutos;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ArrayList<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(ArrayList<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }
    public void deletarProdutos(){
        listaProdutos.removeAll(listaProdutos);
    }
    public void deletarProduto(int index){
        listaProdutos.remove(index);
    }
}
