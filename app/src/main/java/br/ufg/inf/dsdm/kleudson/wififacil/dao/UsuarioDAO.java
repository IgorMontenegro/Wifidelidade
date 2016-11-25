package br.ufg.inf.dsdm.kleudson.wififacil.dao;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.dsdm.kleudson.wififacil.model.Usuario;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.realm.log.RealmLog.clear;

/**
 * Created by Kleudson on 23/11/2016.
 */

public class UsuarioDAO {
    private Realm mRealm;

    public UsuarioDAO(@NonNull Realm realm){
        mRealm = realm;
    }
    public void save(final Usuario usuario) {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(usuario);
            }
        });
    }
    public RealmResults<Usuario> listAll(){
        List<Usuario> listaUsuarios = new ArrayList<Usuario>();
        return mRealm.where(Usuario.class).findAll();

    }

    public void update ( Usuario usuario) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(usuario);
        mRealm.commitTransaction();
        mRealm.close();
    }

    public void delete (final Usuario usuario) {
        mRealm.beginTransaction();
        usuario.deleteFromRealm();
        mRealm.commitTransaction();
        mRealm.close();
    }

    public void deleteTudo ( ) {
        mRealm.beginTransaction();
        RealmResults<Usuario> results = mRealm.where(Usuario.class).findAll();
        clear();
        mRealm.commitTransaction();
        mRealm.close();
    }
}
