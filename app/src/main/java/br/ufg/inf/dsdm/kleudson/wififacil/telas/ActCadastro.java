package br.ufg.inf.dsdm.kleudson.wififacil.telas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.ufg.inf.dsdm.kleudson.wififacil.R;
import br.ufg.inf.dsdm.kleudson.wififacil.model.Campanha;
import br.ufg.inf.dsdm.kleudson.wififacil.model.Usuario;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class ActCadastro extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private Button btnGerarQrCode;
    private EditText edtTxtSSID;
    private EditText edtTxtSenha;

    RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cadastro);

        mToolbar = (Toolbar)findViewById(R.id.tbMain);
        btnGerarQrCode = (Button)findViewById(R.id.btnGerarQrCode);
        edtTxtSSID = (EditText)findViewById(R.id.edtTxtSSID);
        edtTxtSenha = (EditText)findViewById(R.id.edtTxtSenha);

        mToolbar.setTitle("WiFi Fácil");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnGerarQrCode.setOnClickListener(this);
        Realm.init(this);
        realmConfig = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfig);

    }

    public void onClick(View v) {
        Realm realm = Realm.getDefaultInstance();


        String formatoWiFi = "WIFI:S:" + edtTxtSSID.getText().toString() + ";";
        formatoWiFi += "T:;";
        formatoWiFi += "P:" + edtTxtSenha.getText().toString() + ";;";

        realm.beginTransaction();
        Campanha campanha = realm.createObject(Campanha.class);
        Usuario usuario = realm.createObject(Usuario.class);
        campanha.setNome(edtTxtSSID.getText().toString());
        usuario.setNome(edtTxtSenha.getText().toString());

        campanha.setUsuario(usuario);
        realm.commitTransaction();

        RealmQuery<Campanha> query = realm.where(Campanha.class);

        RealmResults<Campanha> result1 = query.findAll();
        for(int i=0; i<result1.size();i++){
            Log.v("campanha nome",result1.get(i).getNome());
            Log.v("campanha usuario",result1.get(i).getUsuario().getNome());
        }


        Intent intent = new Intent(this, ActQrCode.class);
        intent.putExtra("INFORMACOESWIFI",formatoWiFi);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
