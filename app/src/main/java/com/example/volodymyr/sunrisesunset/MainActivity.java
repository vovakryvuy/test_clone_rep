package com.example.volodymyr.sunrisesunset;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.volodymyr.sunrisesunset.model_response.ResponseModel;
import com.example.volodymyr.sunrisesunset.model_response.city_information.AddressComponent;
import com.example.volodymyr.sunrisesunset.model_response.city_information.ResponseCityInformation;
import com.example.volodymyr.sunrisesunset.model_response.city_information.Result;
import com.example.volodymyr.sunrisesunset.request.ServiceRetrofit;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener{
    private static final String BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/geocode/json";
    private static final int  MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private ImageView imageIconLocality,imageIconSunrise,imageIconSunset;
    private TextView mTextCity,mTextTimeSunrise,mTextTimeSunset,mTextTimeDayLenght,mTextDayLenght;
    private GPS gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageIconLocality = findViewById(R.id.icon_locality);
        imageIconSunrise = findViewById(R.id.icon_sunrise);
        imageIconSunset = findViewById(R.id.icon_sunset);
        mTextCity = findViewById(R.id.text_city_name);
        mTextTimeSunrise = findViewById(R.id.time_sunrise);
        mTextTimeSunset = findViewById(R.id.time_sunset);
        mTextTimeDayLenght = findViewById(R.id.text_time_day_lenght);
        mTextDayLenght = findViewById(R.id.text_day_lenght);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkPermissionLocation();

        gps = new GPS(getApplicationContext());
        currentPlace(gps.getLatitude(),gps.getLongitude());
        requestSunInformation((float) gps.getLatitude(),(float) gps.getLongitude());


        PlaceAutocompleteFragment places= (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        ((EditText)places.getView().findViewById(R.id.place_autocomplete_search_input))
                .setHintTextColor(getResources().getColor(R.color.colorDarkSearchBar));
        places.setHint(getApplicationContext().getString(R.string.text_hint_search));
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                requestSunInformation((float) latitude,(float)longitude);
                imageIconLocality.setVisibility(View.INVISIBLE);
                mTextCity.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onError(Status status) {
                Log.d("LOG", "Errror "+ status.toString());
            }
        });

    }

    private void currentPlace(double latitude, double longitude){
        if(latitude!=0.0 && longitude!=0.0){
            String latlong = String.valueOf(latitude).replace(',','.')
                    +","+String.valueOf(longitude).replace(',','.');
            ServiceRetrofit.getService().getCityInformation(BASE_URL_GOOGLE,
                    latlong, getApplicationContext().getString(R.string.api_key_google))
                    .enqueue(new Callback<ResponseCityInformation>() {
                        @Override
                        public void onResponse(Call<ResponseCityInformation> call, Response<ResponseCityInformation> response) {
                            if(response.isSuccessful()){
                                setTextLocality(response.body());

                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseCityInformation> call, Throwable t) {

                        }
                    });
        }

    }

    public void setTextLocality(ResponseCityInformation cityInformation){
        Result result = cityInformation.getResults().get(0);
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        for (AddressComponent addressComponent : result.getAddressComponents()){
            if(addressComponent.getTypes().contains("locality")||
                    addressComponent.getTypes().contains("political")) {
                stringBuilder.append(addressComponent.getShortName()).append(",");
            }
            if (addressComponent.getTypes().contains("country")){
                stringBuilder.append(addressComponent.getLongName());
            }
        }
        Log.d("LOG", "getNameLocality: "+stringBuilder);
        if (!stringBuilder.toString().equals("")){
            mTextCity.setVisibility(View.VISIBLE);
            mTextCity.setText(stringBuilder.toString());
            imageIconLocality.setVisibility(View.VISIBLE);
        }
    }

    public void setSunInformation(ResponseModel responseModel){
        String timeSunrise = parseTime(responseModel.getResults().getSunrise());
        String timeSunset = parseTime(responseModel.getResults().getSunset());
        String timeSun = responseModel.getResults().getDayLength();
        mTextTimeSunrise.setText(timeSunrise);
        imageIconSunrise.setVisibility(View.VISIBLE);
        mTextTimeSunset.setText(timeSunset);
        imageIconSunset.setVisibility(View.VISIBLE);
        mTextDayLenght.setVisibility(View.VISIBLE);
        mTextTimeDayLenght.setText(timeSun);
    }

    public String parseTime(String dateParse){
        Date date;
        String time = "";
        DateFormat dateFormat = new SimpleDateFormat("h:mm:ss a", Locale.ENGLISH);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat  simpleDateFormat = new SimpleDateFormat("HH:mm");
            try {
                date = dateFormat.parse(dateParse);
                time = simpleDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return time;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
                gps = new GPS(getApplicationContext());
                currentPlace(gps.getLatitude(),gps.getLongitude());
                requestSunInformation((float) gps.getLatitude(),(float) gps.getLongitude());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Request
    public void requestSunInformation(float latitude,float longitude){
        if (latitude != 0.0 && longitude != 0.0){
            ServiceRetrofit.getService().getSunInfo(latitude,longitude,
                    null,null,null).enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful() && response.body().getStatus().equals("OK")){
                        setSunInformation(response.body());
                    }
                }
                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.d("LOG","Error massege = "+ t.getMessage());
                }
            });
        }else {
            Log.d("LOG", "requestSunInformation: error latlng = 0");
        }

    }

    public void  checkPermissionLocation(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("LOG", "onConnectionFailed: "+connectionResult.getErrorMessage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    gps = new GPS(getApplicationContext());
                    currentPlace(gps.getLatitude(),gps.getLongitude());
                    requestSunInformation((float) gps.getLatitude(),(float) gps.getLongitude());

                } else {

                    // permission denied functionality that depends on this permission.
                }
                return;
            }
        }
    }

}
