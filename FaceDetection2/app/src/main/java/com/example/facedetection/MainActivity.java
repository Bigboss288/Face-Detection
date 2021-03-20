package com.example.facedetection;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.facedetection.helper.GraphicOverlay;
import com.example.facedetection.helper.RectOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
      Button b1;
      GraphicOverlay graphic;
      CameraView camera;
      AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=findViewById(R.id.btn1);
        graphic=findViewById(R.id.graphic);
        camera=findViewById(R.id.camera);
 //       alert=new SpotsDialog.Builder().setContext(this)
 //               .setMessage("Loading").setCancelable(false).build();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.start();
                camera.captureImage();
                graphic.clear();
            }
        });
        camera.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap=cameraKitImage.getBitmap();
                bitmap=Bitmap.createScaledBitmap(bitmap,camera.getWidth(),camera.getHeight(),false);
                camera.stop();

                process(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });
    }

    private void process(Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
       FirebaseVisionFaceDetectorOptions fireopt=new FirebaseVisionFaceDetectorOptions.Builder().build();
       FirebaseVisionFaceDetector firedetect= FirebaseVision.getInstance().getVisionFaceDetector(fireopt);
       firedetect.detectInImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionFace>>() {
           @Override
           public void onSuccess(List<FirebaseVisionFace> firebaseVisionFaces) {
               getFace(firebaseVisionFaces);
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(MainActivity.this, "error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
           }
       });
    }

    private void getFace(List<FirebaseVisionFace> firebaseVisionFaces) {
        int counter=0;
        for(FirebaseVisionFace face:firebaseVisionFaces){
            Rect rect=face.getBoundingBox();
            RectOverlay rectOverlay=new RectOverlay(graphic,rect);
            graphic.add(rectOverlay);
        }
        alert.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }
}