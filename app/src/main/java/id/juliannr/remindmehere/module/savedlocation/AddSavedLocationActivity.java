package id.juliannr.remindmehere.module.savedlocation;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.rengwuxian.materialedittext.MaterialEditText;

import id.juliannr.remindmehere.R;
import id.juliannr.remindmehere.module.base.BaseActivity;

public class AddSavedLocationActivity extends BaseActivity implements SavedLocationView.AddView{

    private MaterialEditText          locationName;
    private Button                    save;
    private AddSavedLocationPresenter presenter;
    private LatLng                    latLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_saved_location_dialog);
        initView();
        initLocation();
        presenter = AddSavedLocationPresenter.getInstance();
        presenter.setView(this);
    }

    @SuppressLint("MissingPermission")
    private void initLocation() {
        //location
        LocationManager lm        = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        assert lm != null;
        Location       location  = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double[] longitude = {location.getLongitude()};
        final double[] latitude  = {location.getLatitude()};

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude[0] = location.getLongitude();
                latitude[0] = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        latLong = new LatLng(latitude[0], longitude[0]);
    }

    private void initView() {
        save = findViewById(R.id.save);
        locationName = findViewById(R.id.locationName);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    presenter.addData(locationName.getText().toString(), latLong.latitude,
                            latLong.longitude);
                }
            }
        });
    }



    private boolean validate() {
        if(latLong!=null){
            if(!locationName.getText().toString().isEmpty()){
                return true;
            }
        }else{
            Toast.makeText(this, "Your Location cannot be found", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onSucceedSave() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onFailedSave(String message) {
        setResult(RESULT_CANCELED, new Intent().putExtra("message", message));
        finish();
    }
}
