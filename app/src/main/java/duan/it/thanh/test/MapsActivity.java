package duan.it.thanh.test;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private GoogleMap mMap;
    private Button btnAdd;
    private Button btnUpdate;
    private Button zoom;
    private Button out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        zoom = (Button) findViewById(R.id.zoom);
        out = (Button) findViewById(R.id.out);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final MapDAO maps = new MapDAO(getApplicationContext());
        final Maps sv = new Maps();
        final List<Maps> students = maps.getMapAll();
        for (int i=0;i<students.size()-1;i++){
            MarkerOptions marker = new MarkerOptions().position(new LatLng( Double.parseDouble(students.get(i).getV()),  Double.parseDouble(students.get(i).getV1()))).title(students.get(i).getTiltles());
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            mMap.addMarker(marker);
            final int vitri = students.get(i).getId();
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    //Update or delete marker
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MapsActivity.this);
                    dialog.setMessage("Bạn muốn xóa hay sửa?");
                    dialog.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dialog dialog1 = new Dialog(MapsActivity.this);
                            dialog1.setContentView(R.layout.dialog_update);
                            final EditText vs = dialog1.findViewById(R.id.v);
                            final EditText v1s = dialog1.findViewById(R.id.v1);
                            final EditText name = dialog1.findViewById(R.id.title);
                            final Button update = dialog1.findViewById(R.id.btn_update);
                            update.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Maps sach = new Maps();
                                    sach.id = vitri;
                                    sach.v = vs.getText().toString();
                                    sach.v1 = v1s.getText().toString();
                                    sach.tiltles = name.getText().toString();
                                    maps.update(sach);
                                    Toast.makeText(getApplicationContext(), "Đã cập nhật"+ String.valueOf(vitri), Toast.LENGTH_LONG).show();
                                }
                            });
                            dialog1.show();
                        }
                    });
                    dialog.setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            maps.delete(vitri);
                            marker.remove();
                        }
                    });
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                    return false;
                }
            });
        }
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng( 14,  -14)).zoom(15).bearing(90).tilt(40).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //them
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(MapsActivity.this);
                dialog.setContentView(R.layout.dialog_custom);
                dialog.setTitle("New Marker");
                // set the custom dialog components - text, image and button
                final EditText vs = dialog.findViewById(R.id.v);
                final EditText v1s = dialog.findViewById(R.id.v1);
                final EditText titles = dialog.findViewById(R.id.title);

                Button dialogButton = (Button) dialog.findViewById(R.id.new_map);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(vs.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Nhập đầy đủ dữ liệu", Toast.LENGTH_LONG).show();
                        }else{
                        sv.v = vs.getText().toString();
                        sv.v1 = v1s.getText().toString();
                        sv.tiltles = titles.getText().toString();
                        maps.insert(sv);
                        MarkerOptions marker = new MarkerOptions().position(new LatLng( Double.parseDouble(vs.getText().toString()),  Double.parseDouble(v1s.getText().toString()))).title(titles.getText().toString());
                        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                        mMap.addMarker(marker);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng( Double.parseDouble(vs.getText().toString()),  Double.parseDouble(v1s.getText().toString()))).zoom(15).bearing(90).tilt(40).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        Toast.makeText(getApplicationContext(), "Đã thêm " + titles.getText().toString(), Toast.LENGTH_LONG).show();
                    }}
                });
                dialog.show();

            }
        });
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
        //show all
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this,ListMapActivity.class);
                startActivity(intent);
            }
        });
    }

}
