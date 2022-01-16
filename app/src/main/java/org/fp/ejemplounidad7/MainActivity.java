package org.fp.ejemplounidad7;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
    URLs para probar
   ------------------
    https://www.xtrafondos.com/wallpapers/bote-en-lago-8639.jpg
    https://www.xtrafondos.com/wallpapers/mariposa-sobre-hoja-8115.jpg
    https://www.xtrafondos.com/wallpapers/mariquita-en-diente-de-leon-7783.jpg
 */

public class MainActivity extends AppCompatActivity {

    private EditText url;
    private String labelURL;
    private RecyclerView urlList;
    private URLAdapter adapter;
    private Button descargar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        descargar = findViewById(R.id.buttonDownload);
        descargar.setOnClickListener(this::onDownload);
        url = findViewById(R.id.txtURL);
        urlList = findViewById(R.id.urlList);
        urlList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        urlList.setAdapter(adapter = new URLAdapter());
        String [] p = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(p, 0);
    }

    public void download() {
        HttpURLConnection con = null;
        final URLAdapter.URLdescarga urlDescarga  = new URLAdapter.URLdescarga(url.toString(),0);
        runOnUiThread(() -> adapter.add(urlDescarga));
        try {
            URL url = new URL(labelURL);
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + Uri.parse(url.toString()).getLastPathSegment());
            con = (HttpURLConnection) url.openConnection();
            try  {
                BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                int b;
                while ((b = bis.read()) != -1){
                    bos.write(b);
                }
                urlDescarga.setEstado(1);
                runOnUiThread(() -> adapter.sustituir(urlDescarga));
            } catch (IOException e) {
                e.printStackTrace();
                urlDescarga.setEstado(-1);
                runOnUiThread(() -> adapter.sustituir(urlDescarga));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    public void onDownload(View v) {
        labelURL = url.getText().toString();
        url.setText("");
        new Thread(this::download).start();
    }
}