package br.ufg.inf.dsdm.kleudson.wififacil.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Jean on 11/20/16.
 */

public class Campanha extends RealmObject {

    private String Nome;
    private Usuario usuario;

    private RealmList<Produto> listaProdutos;

    public Campanha() {
    }

    public Campanha(String nome, Usuario usuario, ArrayList<Produto> listaProdutos) {
        Nome = nome;
        this.usuario = usuario;
      //  this.listaProdutos = listaProdutos;
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

    public RealmList<Produto> getListaProdutos() {
        return listaProdutos;
    }

    public void setListaProdutos(RealmList<Produto> listaProdutos) {
        this.listaProdutos = listaProdutos;
    }
    public void deletarProdutos(){
        listaProdutos.removeAll(listaProdutos);
    }
    public void deletarProduto(int index){
        listaProdutos.remove(index);
    }
}
