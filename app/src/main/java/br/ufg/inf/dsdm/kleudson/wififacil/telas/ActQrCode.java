package br.ufg.inf.dsdm.kleudson.wififacil.telas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
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

    protected final static int TIMER_RUNTIME = 4000;
    protected boolean mbActive;
    protected ProgressBar mProgressBar;

    Bitmap bm;
    Bitmap bitmap;

    private Toolbar mToolbarBottom;
    ImageView qrCodeImageview;
    String QRcode;
    public final static int WIDTH = 500;

    @Override
    protected void onCreate(Bundle icicle) {

        super.onCreate(icicle);
        setContentView(R.layout.act_qr_code);


        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
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


        Toast.makeText(getApplicationContext(), "Gerando QR Code...", Toast.LENGTH_LONG).show();

        getID();

// create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
// this is the msg which will be encode in QRcode
                String formatoWiFi = new String();
                Bundle bundle = getIntent().getExtras();
                if (bundle.containsKey("INFORMACOESWIFI")) {
                    formatoWiFi = bundle.getString("INFORMACOESWIFI");
                }
                QRcode = formatoWiFi;
                try {
                    synchronized (this) {
                        wait(4000);
// runOnUiThread method used to do UI task in main thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    bitmap = codificarQRCode(QRcode);
                                    qrCodeImageview.setImageBitmap(bitmap);

                            }

                            private Bitmap codificarQRCode(String code){
                                return codificarQRCode(code);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        final Thread timerThread = new Thread() {
            @Override
            public void run() {
                mbActive = true;
                try {
                    int waited = 0;
                    while (mbActive && (waited < TIMER_RUNTIME)) {
                        sleep(100);
                        if (mbActive) {
                            waited += 100;
                            updateProgressBar(waited);
                        }
                    }
                } catch (InterruptedException e) {
                    //Erro
                } finally {
                    onContinue();
                }
            }
        };
        timerThread.start();

    }

    private void caseSalvar() {
        Toast.makeText(getApplicationContext(), "Salvando em Arquivo", Toast.LENGTH_SHORT).show();
        save();
    }

    public void updateProgressBar(final int timePassed) {
        if (null != mProgressBar) {
            final int progress = mProgressBar.getMax() * timePassed / TIMER_RUNTIME;
            mProgressBar.setProgress(progress);
        }
    }

    public void onContinue() {
        Log.d("Mensagem Final", "Barra de Loading Carregada");
    }


    private void getID() {
        qrCodeImageview = (ImageView) findViewById(R.id.img_qr_code_image);
    }

    // this is method call from on create and return bitmap image of QRCode.
    Bitmap encodeAsBitmap(String str) throws WriterException {

        BitMatrix result;

        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];

        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }

        }

        Bitmap bitmapPixels = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmapPixels.setPixels(pixels, 0, 500, 0, 0, w, h);

        mToolbarBottom.inflateMenu(R.menu.menu_bottom);

        return bitmapPixels;
    }

    /// end of this method


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

