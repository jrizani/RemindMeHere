package id.juliannr.remindmehere.module.reminderdetail;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import id.juliannr.remindmehere.R;
import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.module.base.BaseActivity;

public class ReminderDetailActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker    marker;
    private LatLng    latLong;
    private Reminder  data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_detail);
        data = getIntent().getParcelableExtra("data");
        setView();
    }

    private void setView() {
        setToolbar("Reminder Detail", true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        latLong = new LatLng(Double.parseDouble(data.getLatitude()), Double.parseDouble(data
                .getLongitude()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 15));
        marker = mMap.addMarker(new MarkerOptions().position(latLong));
    }
}
