package br.ufg.inf.dsdm.kleudson.wififacil.telas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufg.inf.dsdm.kleudson.wififacil.R;

public class ActQrCode extends AppCompatActivity {

    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;

    protected final static int TIMER_RUNTIME = 4000;

    Bitmap bm;
    Bitmap bitmap;

    private Toolbar mToolbarBottom;
    ImageView imgQrCode;
    String qrCode;

    @Override
    protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.act_qr_code);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tbMain);
        mToolbar.setTitle("WiFi Fácil");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbarBottom = (Toolbar) findViewById(R.id.inc_tb_bottom);
        mToolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_salvar:
                        caseSalvar();
                        break;
                    case R.id.action_email:
                        email();
                        break;
                    default:
                        return ActQrCode.super.onOptionsItemSelected(item);
                }
                return true;
            }
        });




        getID();

        String formatoWiFi = new String();
        Bundle bundle = getIntent().getExtras();
        if (bundle.containsKey("INFORMACOESWIFI")) {
            formatoWiFi = bundle.getString("INFORMACOESWIFI");
        }
        qrCode = formatoWiFi;

        try {
            bitmap = encodeAsBitmap(qrCode);
            imgQrCode.setImageBitmap(bitmap);
            Toast.makeText(getApplicationContext(), "QR Code Gerado com Sucesso!!!", Toast.LENGTH_LONG).show();
        } catch (RuntimeException e) {
            Toast.makeText(getApplicationContext(), "Não foi possível gerar o QrCode :(", Toast.LENGTH_LONG).show();
            throw e;
        } catch (Exception e) {
            throw new RuntimeExecutionException(e);
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        mToolbarBottom.inflateMenu(R.menu.menu_bottom);

        return bitmap;


    }

    private void caseSalvar() {
        Toast.makeText(getApplicationContext(), "Salvando em Arquivo", Toast.LENGTH_SHORT).show();
        save();
    }


    private void getID() {
        imgQrCode = (ImageView) findViewById(R.id.img_qr_code_image);
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

    public void save() {

        File file = obterArquivo();
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            startActivity(Intent.createChooser(intent, "Salvar ou Compartilhar"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActQrCode.this, "Você não possui nenhum cliente de Email Instalado.", Toast.LENGTH_SHORT).show();
        }
    }

    public void email() {

        File file = obterArquivo();
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{""});
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "WiFi Fácil - QrCode WiFi");
        intent.putExtra(Intent.EXTRA_TEXT, "O Arquivo QrCode Wifi gerado pelo " +
                "Aplicativo WiFi Fácil está em Anexo.\n\n" +
                "Equipe WiFi Fácil");
        try {
            startActivity(Intent.createChooser(intent, "Envio de Email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActQrCode.this, "Você não possui nenhum cliente de Email Instalado.", Toast.LENGTH_SHORT).show();
        }
    }

    private File obterArquivo() {

        bm = bitmap;
        File diretorio = new File(getExternalCacheDir().getPath());
        if (!diretorio.exists())
            diretorio.mkdirs();

        String formatoData = new SimpleDateFormat("yyyyMMddHHmmss",
                java.util.Locale.getDefault()).format(new Date());

        File file = new File(diretorio, formatoData + ".png");

        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}

