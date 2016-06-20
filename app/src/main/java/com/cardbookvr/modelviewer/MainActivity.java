package com.cardbookvr.modelviewer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.cardbookvr.renderbox.IRenderBox;
import com.cardbookvr.renderbox.RenderBox;
import com.cardbookvr.renderbox.Transform;
import com.cardbookvr.renderbox.math.Quaternion;
import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;

public class MainActivity extends GvrActivity implements IRenderBox {
    private static final String TAG = "ModelViewer";
    GvrView gvrView;

    Transform model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gvrView = (GvrView) findViewById(R.id.gvr_view);
        gvrView.setRenderer(new RenderBox(this, this));
        setGvrView(gvrView);
    }

    @Override
    public void setup() {
        RenderBox.instance.mainCamera.headTracking = false;

        ModelObject modelObj;
        Uri intentUri = getIntent().getData();
        if (intentUri != null) {
            Log.d(TAG, "!!!! intent " + intentUri.getPath());
            modelObj = new ModelObject(intentUri.getPath());
        } else {
            // default object
            modelObj = new ModelObject(R.raw.teapot);
        }

        float scalar = modelObj.normalScalar();
        model = new Transform()
                .setLocalPosition(0, 0, -3)
                .setLocalScale(scalar, scalar, scalar)
                .addComponent(modelObj);
    }

    @Override
    public void preDraw() {
        float[] hAngles = RenderBox.instance.headAngles;
        Quaternion rot = new Quaternion();
        rot.setEulerAnglesRad(hAngles[0], hAngles[1], hAngles[2]);
        model.setLocalRotation(rot.conjugate());
    }

    @Override
    public void postDraw() {

    }
}
