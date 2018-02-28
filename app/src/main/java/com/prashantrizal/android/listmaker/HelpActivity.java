package com.prashantrizal.android.listmaker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Collections;

public class HelpActivity extends AppCompatActivity {
    SurfaceView cameraView;
    EditText editText;
    CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        cameraView = (SurfaceView) findViewById(R.id.surface_view);
        editText = (EditText) findViewById(R.id.edit_text);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if(!textRecognizer.isOperational()){
            Log.w("HelpActivity","Detector Dependencies are not yet available");
        }
        else{
            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try{
                        cameraSource.start(cameraView.getHolder());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });
        }

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }


            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if(items.size()!= 0){
                    editText.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < items.size(); i++) {
                                TextBlock item = items.valueAt(i);
                                builder.append(item.getValue());
                                builder.append("\n");
                            }
                            editText.setText(builder.toString());
                        }
                    });
                }
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuhelpfile, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_to_list:
                String toAdd = editText.getText().toString();
                String breakList[];
                String delimiter = "\n";
                breakList = toAdd.split(delimiter);
                for(int i = 0; i < breakList.length; i++) {
                    MainActivity.list_of_items.add(breakList[i]);
                }
                    MainActivity.listView.invalidateViews();
                Collections.sort(MainActivity.list_of_items);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void destroyCamera(View v){
        cameraSource.stop();
        cameraView.setVisibility(View.INVISIBLE);
//        Intent i = new Intent(HelpActivity.this, MainActivity.class);
//        i.putExtra("listData", editText.getText().toString());
//        startActivity(i);
    }
}
