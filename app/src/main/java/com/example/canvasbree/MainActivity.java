package com.example.canvasbree;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // Add a different request code for every activity you are starting from here makanya pake int trus ada valuenya
    public static final int SIGNATURE_ACTIVITY = 1;
    Toolbar toolbar;
    FloatingActionButton fab;
    Bitmap bitmap;
    ImageView img_signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        img_signature = (ImageView) findViewById(R.id.img_signature);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_signature.setImageResource(0);
                Intent intent = new Intent(getApplicationContext(), Signature.class);
                startActivityForResult(intent, SIGNATURE_ACTIVITY);
            }
        });
    }

    // buat ngambil gambarnya trus gk kena pesan error
/*    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     // int requestCOde, int resultCode ,Intent data
        // pake switch biar lgsg eksekusi aje satu pilihan itu casenya
        switch (requestCode) {                                                          // buat ngecek hasilnya /hasil dari request click fab
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status = bundle.getString("status");     // nangkep data dari signature.java class dengan kunci status
                    String path_image = bundle.getString("image");  // ini juga kayanya dah
                    Uri filePath = Uri.parse(path_image);                // ngambil gambar pake lokasi(lokasi absolut) yg udah disimpen diatas

                    if (status.equalsIgnoreCase("done")) {  // mesti sama kaya yg di signature.java biar bisa ambil gambarnya
                        Toast.makeText(this, "Signature saved to gallery.", Toast.LENGTH_SHORT).show();

                        try {
                            //Getting the Bitmap from Gallery
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                            //Setting the Bitmap to ImageView
                            img_signature.setImageBitmap(bitmap);

                            // ini buat ngasihin tau exception(error/debug) buat ngecek di logcat ada error kaga
                            // biar pas ngambil gambar satu gambar itu aje trus kelarin programnya buat ngambil gambar lagi
                        } catch (IOException e) {       // catch (Exception e)
                            e.printStackTrace();        // Log.e(TAG,Log.getStackTraceString(e));  ini buat pesan errornya secara detail
                        }                               // method yang mane variabel apaan baris ke brape <e.printStackTrace()>
                    }
                }
                break;
        }

    }*/

}
