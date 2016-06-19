package com.concepts.maps;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button B3;
    Marker M1,M2;
    private static final float DEFAULTZOOM = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       // SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
       //         .findFragmentById(R.id.map);
       // mapFragment.getMapAsync(this);

         if(initmap()){
             Toast.makeText(MapsActivity.this, "Map Ready", Toast.LENGTH_SHORT).show();
             gotoloc(22.5726, 88.3639, DEFAULTZOOM);
         }

        B3 = (Button) findViewById(R.id.button3);
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlaceAll.class);
                startActivity(intent);
            }
        });

    }

    private void gotoloc(double lat, double lng, float dz)
    {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, dz);
        mMap.moveCamera(update);
    }

    private boolean initmap()
    {
        if (mMap == null ) {
            final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            if (mMap != null) {
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
                        TextView tvlocality = (TextView) v.findViewById(R.id.tv_locality);
                        TextView tvlat = (TextView) v.findViewById(R.id.tv_lat);
                        TextView tvlng = (TextView) v.findViewById(R.id.tv_lng);
                        TextView tvsnippet = (TextView) v.findViewById(R.id.tv_snippet);

                        LatLng ll = marker.getPosition();
                        tvlocality.setText(marker.getTitle());
                        tvlat.setText("Latitude = " + ll.latitude);
                        tvlng.setText("Longitude = " + ll.longitude);
                        tvsnippet.setText(marker.getSnippet());

                        return v;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        View v = getLayoutInflater().inflate(R.layout.info_window, null);
                        TextView tvlocality = (TextView) v.findViewById(R.id.tv_locality);
                        TextView tvlat = (TextView) v.findViewById(R.id.tv_lat);
                        TextView tvlng = (TextView) v.findViewById(R.id.tv_lng);
                        TextView tvsnippet = (TextView) v.findViewById(R.id.tv_snippet);

                        LatLng ll = marker.getPosition();
                        tvlocality.setText(marker.getTitle());
                        tvlat.setText("Latitude = " + ll.latitude);
                        tvlng.setText("Longitude = " + ll.longitude);
                        tvsnippet.setText(marker.getSnippet());

                        return v;


                    }

                });
            }
        }
        return (mMap != null);
    }

    public  void gotoloc(double lat, double lng){
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mMap.moveCamera(update);
    }


    /*protected void onPlacePActivityResult(int request_code, int result_code, Intent data)
    {
      if (request_code == PLACE_PICKER_REQUEST)
      {
          if (result_code == RESULT_OK)
          {
              Place place = PlacePicker.getPlace(this, data);
              String address = String.format("Place: @s", place.getAddress());

          }
      }
    }*/

   public void Click(View v){
       hidesoftkeyboard(v);

       EditText E1 = (EditText) findViewById(R.id.editText);
       String Search = E1.getText().toString();

       List<Address> addressList = null;

       if (!E1.equals("")) {
           if (Search != null || !Search.equals("")) {
               Geocoder geocoder = new Geocoder(this);
               try {
                   addressList = geocoder.getFromLocationName(Search, 1);
               } catch (IOException e) {
                   e.printStackTrace();
               }
               Address address = addressList.get(0);
               String Locality = address.getLocality();
               String Country = address.getCountryName();
               LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
               mMap.addMarker(new MarkerOptions().position(latLng).title(Locality + " & " + Country + ".").
                       icon(BitmapDescriptorFactory.fromResource(R.drawable.markera)).draggable(true));
               mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
               Toast.makeText(MapsActivity.this, Locality, Toast.LENGTH_LONG).show();
               mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                   @Override
                   public void onMapLongClick(LatLng latLng) {
                       Geocoder gc = new Geocoder(MapsActivity.this);
                       List<Address> list = null;
                       try {
                           list = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
                       } catch (IOException e) {
                           e.printStackTrace();
                           return;
                       }
                       Address add = list.get(0);
                       String Locality1 = add.getLocality();
                       String Country1 = add.getCountryName();
                       LatLng l = new LatLng(add.getLatitude(), add.getLongitude());
                       mMap.addMarker((new MarkerOptions().position(l).title(Locality1 + " & " + Country1).
                               icon(BitmapDescriptorFactory.fromResource(R.drawable.markera)).draggable(true)));
                   }
               });
           }
       }
       if (Search.equals("") || Search == null || E1.equals("") || E1 == null)
       {
           Toast.makeText(MapsActivity.this, "The field is vacant or There is some Error!!", Toast.LENGTH_SHORT).show();
       }
   }

    public void hidesoftkeyboard(View v){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void Change(View v){
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else
        {
         mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in India and move the camera
        LatLng India = new LatLng(22.5726, 88.3639);
        mMap.addMarker(new MarkerOptions().position(India).title("Marker!").
                icon(BitmapDescriptorFactory.fromResource(R.drawable.markera)).draggable(true));
        //icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(India));


        mMap.setMyLocationEnabled(true);

    }
}
