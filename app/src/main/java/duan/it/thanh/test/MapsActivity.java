package duan.it.thanh.test;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import duan.it.thanh.test.dao.MapDAO;
import duan.it.thanh.test.model.Maps;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private EditText title;
    private EditText va;
    private EditText v1;
    private Button btnAdd;
    private Button zoom;
    private Button out;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        title = (EditText) findViewById(R.id.title);
        va = (EditText) findViewById(R.id.v);
        v1 = (EditText) findViewById(R.id.v1);
        btnAdd = (Button) findViewById(R.id.btn_add);
        zoom = (Button) findViewById(R.id.zoom);
        out = (Button) findViewById(R.id.out);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //sửa

        //xóa

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final MapDAO maps = new MapDAO(MapsActivity.this);
        final Maps sv = new Maps();
        final List<Maps> students = maps.getMapAll();
        for (int i = 0; i <students.size()-1; i++){
            MarkerOptions marker = new MarkerOptions().position(new LatLng( Double.parseDouble(students.get(i).getV()),  Double.parseDouble(students.get(i).getV1()))).title(students.get(i).getTiltles());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            mMap.addMarker(marker);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                va.setText(String.valueOf(marker.getPosition().latitude));
                v1.setText(String.valueOf(marker.getPosition().longitude));
                title.setText(marker.getTitle());
                final int vitri = Integer.parseInt(marker.getId().replaceAll("m",""));
                out.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("remove",marker.getId());
                        maps.delete(marker.getPosition().latitude);
                        marker.remove();

                    }
                });
                zoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Maps sach = new Maps();
                        sach.id = vitri;
                        Log.e("test", String.valueOf(vitri));
                        sach.v = va.getText().toString();
                        sach.v1 = v1.getText().toString();
                        sach.tiltles = title.getText().toString();
                        maps.update(sach);
                        final List<Maps> studentss = maps.getMapAll();
                        for (int i = 0; i <studentss.size()-1; i++){
                            MarkerOptions markerss = new MarkerOptions().position(new LatLng( Double.parseDouble(students.get(i).getV()),  Double.parseDouble(students.get(i).getV1()))).title(students.get(i).getTiltles());
                            markerss.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                            mMap.addMarker(markerss);
                        }
                    }
                });
                return false;
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(va.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nhập đầy đủ dữ liệu", Toast.LENGTH_LONG).show();
                }else{
                    mMap.clear();
                    sv.v = va.getText().toString();
                    sv.v1 = v1.getText().toString();
                    sv.tiltles = title.getText().toString();
                    maps.insert(sv);
                    MarkerOptions marker = new MarkerOptions().position(new LatLng( Double.parseDouble(va.getText().toString()),  Double.parseDouble(v1.getText().toString()))).title(title.getText().toString());
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    mMap.addMarker(marker);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng( Double.parseDouble(va.getText().toString()),  Double.parseDouble(v1.getText().toString()))).zoom(15).bearing(90).tilt(40).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    Toast.makeText(getApplicationContext(), "Đã thêm " + title.getText().toString(), Toast.LENGTH_LONG).show();
                    final List<Maps> students = maps.getMapAll();
                    for (int i = 0; i <students.size()-1; i++){
                        MarkerOptions markers = new MarkerOptions().position(new LatLng( Double.parseDouble(students.get(i).getV()),  Double.parseDouble(students.get(i).getV1()))).title(students.get(i).getTiltles());
                        markers.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                        mMap.addMarker(markers);
                    }
                }}
        });

    }

}
