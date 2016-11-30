package br.ufg.inf.dsdm.kleudson.wififacil.dao;

import android.support.annotation.NonNull;

import br.ufg.inf.dsdm.kleudson.wififacil.model.Produto;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.log.RealmLog.clear;

/**
 * Created by Kleudson on 23/11/2016.
 */

public class ProdutoDAO {
    private Realm mRealm;

    public ProdutoDAO(@NonNull Realm realm){
        mRealm = realm;
    }
    public void save(final Produto produto) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(produto);
            }
        });
    }
    public RealmResults<Produto> listAll(){

        return mRealm.where(Produto.class).findAll();

    }

    public void update (final Produto produto) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(produto);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public void delete (final Produto produto) {
        mRealm.beginTransaction();
        produto.deleteFromRealm();
        mRealm.commitTransaction();
        mRealm.close();
    }

    public void deleteTudo ( ) {
        mRealm.beginTransaction();
        RealmResults<Produto> results = mRealm.where(Produto.class).findAll();
        clear();
        mRealm.commitTransaction();
        mRealm.close();
    }
}
