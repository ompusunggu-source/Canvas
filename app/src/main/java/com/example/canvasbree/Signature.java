package com.example.canvasbree;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.widget.Toast.LENGTH_SHORT;

public class Signature extends AppCompatActivity {

    LinearLayout mContent;
    signature mSignature;
    Button mClear, mGetSign;
    public static String tempDir;
    public int count = 1;
    public String current = null;
    private Bitmap mBitmap;
    View mView;
    File mypath;
    String path_image;

    Bitmap bitmap;
    ImageView img_signature;
    Button lihatBree;

    Integer [] bala = {R.id.imgvwSignature, R.id.imgvwSignature0, R.id.imgvwSignature1, R.id.imgvwSignature2, R.id.imgvwSignature3};

    private String uniqueId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signature);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Draw Signature");

        tempDir = Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.external_dir) + "/";
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir(getResources().getString(R.string.external_dir), Context.MODE_PRIVATE);

        prepareDirectory();
        uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_" + Math.random();
        current = uniqueId + ".png";
        mypath = new File(directory, current);


        mContent = (LinearLayout) findViewById(R.id.canvas_sign);
        mSignature = new signature(this, null);
        mSignature.setBackgroundColor(Color.WHITE);
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) findViewById(R.id.clear);
        mGetSign = (Button) findViewById(R.id.save);
        mGetSign.setEnabled(false);
        mView = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.e("log_tag", "Panel Saved");
                mView.setDrawingCacheEnabled(true);
//                mSignature.save(mView);
                Toast.makeText(Signature.this, "Signature saved to gallery", LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onDestroy() {
        Log.e("GetSignature", "onDestory");
        super.onDestroy();
    }

    private String getTodaysDate() {

        final Calendar c = Calendar.getInstance();
        int todaysDate = (c.get(Calendar.YEAR) * 10000) +
                ((c.get(Calendar.MONTH) + 1) * 100) +
                (c.get(Calendar.DAY_OF_MONTH));
        Log.e("DATE:", String.valueOf(todaysDate));
        return (String.valueOf(todaysDate));

    }

    private String getCurrentTime() {

        final Calendar c = Calendar.getInstance();
        int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000) +
                (c.get(Calendar.MINUTE) * 100) +
                (c.get(Calendar.SECOND));
        Log.e("TIME:", String.valueOf(currentTime));
        return (String.valueOf(currentTime));

    }


    private boolean prepareDirectory() {
        try {
            if (makedirs()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Could not initiate File System.. Is Sdcard mounted properly?", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean makedirs() {
        File tempdir = new File(tempDir);
        if (!tempdir.exists())
            tempdir.mkdirs();

        if (tempdir.isDirectory()) {
            File[] files = tempdir.listFiles();
            for (File file : files) {
                if (!file.delete()) {
                    System.out.println("Failed to delete " + file);
                }
            }
        }
        return (tempdir.isDirectory());
    }

    // ini yang buat view gambarnya
    public void OnViewSignatureClick(View view) {
        lihatBree = findViewById(R.id.btnViewSignature);
        int lihatBree;

        for (int i=0; i<5; i++){
            ImageView bree = (ImageView) findViewById(bala[i]);
            Uri filePath = Uri.parse(path_image);

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bree.setImageBitmap(bitmap);
        }

       /** for(lihatBree=0; lihatBree<bala.length; lihatBree++){
        //if(i=0; i<5; i++){}
            if (lihatBree == 0){
                //@SuppressLint("WrongViewCast") ImageView tampilin = (ImageView) findViewById(R.id.LinLay01);//kaga bisa force close
                //ImageView tampilin = (ImageView) findViewById(IV[i]);     // kalo gini malah 5 kotak isinya sama
                ImageView tampilin = (ImageView) findViewById(R.id.imgvwSignature);
                Uri filePath = Uri.parse(path_image);
                //tampilin.setImageResource(R.drawable.bala);

                try {
                    //Getting the Bitmap from Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tampilin.setImageBitmap(bitmap);    //Sets a Bitmap as the content of this ImageView.
                //Setting the Bitmap to ImageView
                //img_signature.setImageBitmap(bitmap);

            }


        }**/

    }

    public class signature extends View {
        private static final float STROKE_WIDTH = 15f;                      // 5 tadian
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 6;    // 2 tadian
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v) {
            Log.e("log_tag", "Width: " + v.getWidth());
            Log.e("log_tag", "Height: " + v.getHeight());
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
                ;
            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                FileOutputStream mFileOutStream = new FileOutputStream(mypath);

                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, mFileOutStream);

                mFileOutStream.flush();
                mFileOutStream.close();
                path_image = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "title", null);
                Log.e("log_tag", "url: " + path_image);
                //In case you want to delete the file
                //boolean deleted = mypath.delete();
                //Log.e("log_tag","deleted: " + mypath.toString() + deleted);
                //If you want to convert the image to string use base64 converter

            } catch (Exception e) {
                Log.e("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {
        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}