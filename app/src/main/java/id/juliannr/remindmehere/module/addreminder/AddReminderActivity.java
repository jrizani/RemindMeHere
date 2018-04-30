package id.juliannr.remindmehere.module.addreminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import id.juliannr.remindmehere.R;
import id.juliannr.remindmehere.data.model.Reminder;
import id.juliannr.remindmehere.data.model.SavedLocation;
import id.juliannr.remindmehere.module.base.BaseActivity;
import id.juliannr.remindmehere.util.DateHelper;

public class AddReminderActivity extends BaseActivity implements OnMapReadyCallback,
        AddReminderView.View, DatePickerDialog.OnDateSetListener {

    private GoogleMap                 mMap;
    private Marker                    marker;
    private LatLng                    latLong;
    private MaterialEditText          locationName;
    private MaterialEditText          description;
    private MaterialEditText          dueDate;
    private AppCompatButton           save;
    private AddReminderPresenter      presenter;
    private PlaceAutocompleteFragment search;
    private MaterialBetterSpinner     savedLocation;
    private SwitchCompat              switchLocation;
    private boolean checked = true;
    private boolean isValid = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        setView();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        setMap();
        initSwitch();

        setButtonAction();
    }

    private void setButtonAction() {
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    String location = "";
                    if (checked) location = savedLocation.getText().toString();
                    else location = locationName.getText().toString();
                    Reminder data = new Reminder(UUID.randomUUID().toString(),
                            location,
                            String.valueOf(latLong.latitude),
                            String.valueOf(latLong.longitude),
                            description.getText().toString(),
                            DateHelper.toDueDate(dueDate.getText().toString()));
                    presenter.save(data);
                } else {
                    Toast.makeText(AddReminderActivity.this, "Please check all input", Toast
                            .LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setView() {
        presenter = AddReminderPresenter.getInstance();
        presenter.setView(this);
        this.dueDate = (MaterialEditText) findViewById(R.id.dueDate);
        this.description = (MaterialEditText) findViewById(R.id.description);
        this.locationName = (MaterialEditText) findViewById(R.id.locationName);
        savedLocation = findViewById(R.id.savedLocation);
        switchLocation = findViewById(R.id.switchLocation);
        setToolbar("Add Reminder", true);

        dueDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showCalendar();
            }
        });
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendar();
            }
        });
        switchLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (canSwitch()) {
                        checked = true;
                        locationName.setVisibility(View.GONE);
                        savedLocation.setVisibility(View.VISIBLE);
                        mMap.getUiSettings().setAllGesturesEnabled(false);
                    } else {
                        switchLocation.setChecked(false);
                    }
                } else {
                    checked = false;
                    locationName.setVisibility(View.VISIBLE);
                    savedLocation.setVisibility(View.GONE);
                    mMap.getUiSettings().setAllGesturesEnabled(true);
                }
            }
        });
        savedLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                latLong = presenter.getSavedLocationLatLong(savedLocation.getText().toString());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 12));
            }
        });
    }

    private void initSwitch() {
        if (canSwitch()) {
            switchLocation.setChecked(true);
            List<String>        spinnerArray = new ArrayList<String>();
            List<SavedLocation> locations    = presenter.getSavedLocation();
            for (SavedLocation location : locations) {
                spinnerArray.add(location.getLocationName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            savedLocation.setAdapter(arrayAdapter);
//            mMap.getUiSettings().setAllGesturesEnabled(false);
//            checked = true;
//            locationName.setVisibility(View.GONE);
//            savedLocation.setVisibility(View.VISIBLE);
        } else {
            switchLocation.setChecked(false);
//            mMap.getUiSettings().setAllGesturesEnabled(false);
//            checked = false;
//            locationName.setVisibility(View.VISIBLE);
//            savedLocation.setVisibility(View.GONE);
        }
    }

    private boolean canSwitch() {
        if (presenter.checkSavedLocation() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void showCalendar() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance
                (AddReminderActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show(getFragmentManager(), "DatePickerDialog");
    }

    private boolean validate() {
        if (latLong != null) {
            if (!locationName.getText().toString().isEmpty() || !savedLocation.getText().toString()
                    .isEmpty()) {
                if (!description.getText().toString().isEmpty()) {
                    if (!dueDate.getText().toString().isEmpty()) {
                        if (DateHelper.toDueDate(dueDate.getText().toString()) != null) {
                            isValid = true;
                        }
                    }
                }
            }
        }
        return isValid;
    }

    @SuppressLint("MissingPermission")
    private void setMap() {
        search = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.search);
        search.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                latLong = place.getLatLng();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(place.getViewport(), 12));
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(AddReminderActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //location
        LocationManager lm        = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location        location  = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double[]  longitude = {location.getLongitude()};
        final double[]  latitude  = {location.getLatitude()};

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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 12));

        marker = mMap.addMarker(new MarkerOptions().position(latLong)
                .title("Set location").snippet("Drag the map for select the location"));
        if (canSwitch()) {
            mMap.getUiSettings().setAllGesturesEnabled(false);
        } else {
            mMap.getUiSettings().setAllGesturesEnabled(true);
        }
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                marker.setPosition(mMap.getCameraPosition().target);
                latLong = marker.getPosition();
                search.setText("");
            }
        });


    }

    @Override
    public void saveSuccess() {
        Toast.makeText(this, "Reminder Tersimpan", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void saveFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        dueDate.setText(date);
    }
}
