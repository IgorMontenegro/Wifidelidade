package br.ufg.inf.dsdm.kleudson.wififacil.dao;

import android.support.annotation.NonNull;

import br.ufg.inf.dsdm.kleudson.wififacil.model.Campanha;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.log.RealmLog.clear;

/**
 * Created by Kleudson on 23/11/2016.
 */

public class CampanhaDAO {
    private Realm mRealm;

    public CampanhaDAO(@NonNull Realm realm){
        mRealm = realm;
    }
    public void save(final Campanha campanha) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(campanha);
            }
        });
    }
    public RealmResults<Campanha> listAll(){

        return mRealm.where(Campanha.class).findAll();

    }

    public void update ( Campanha campanha) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(campanha);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public void delete (final Campanha campanha) {
        mRealm.beginTransaction();
        campanha.deleteFromRealm();
        mRealm.commitTransaction();
        mRealm.close();
    }

    public void deleteTudo ( ) {
        mRealm.beginTransaction();
        RealmResults<Campanha> results = mRealm.where(Campanha.class).findAll();
        clear();
        mRealm.commitTransaction();
        mRealm.close();
    }

}
