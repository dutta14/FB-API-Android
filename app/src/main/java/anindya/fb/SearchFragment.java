package anindya.fb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import anindya.fb.datamodel.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment implements LocationListener {

    public static final String BASE_URL = "http://lowcost-env.tigynm9tp6.us-west-2.elasticbeanstalk.com/";
    public static final String DOWNLOAD_COMPLETE = "download";
    public static final String DOWNLOAD_MESSAGE = "data";
    private Button mClear, mSearch;
    private TextView mKeywordView;
    private String mKeyword;

    Context mContext;
    public static Result mResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getContext();
        mClear = (Button) view.findViewById(R.id.clear);
        mKeywordView = (TextView) view.findViewById(R.id.keyword);
        mSearch = (Button) view.findViewById(R.id.search);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeywordView.setText("");
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeyword = mKeywordView.getText().toString();
                if (mKeyword.isEmpty()) {
                    Toast.makeText(mContext, R.string.entry_error, Toast.LENGTH_SHORT).show();
                } else {

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    RetrofitInterface apiService = retrofit.create(RetrofitInterface.class);

                    LocationManager mgr = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
                    Location location = null;
                    boolean isGPS = mgr.isProviderEnabled(LocationManager.GPS_PROVIDER),
                            isNW = mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    } else {
                        String provider = isGPS ? LocationManager.GPS_PROVIDER: isNW? LocationManager.NETWORK_PROVIDER : "";
                        if (isGPS || isNW) {
                            mgr.requestLocationUpdates(provider, 60000, 10, SearchFragment.this);
                            location = mgr != null? mgr.getLastKnownLocation(provider) : null;
                        }
                    }

                    Call<Result> call = location != null ? apiService.getLocationData(mKeyword, location.getLatitude(),
                            location.getLongitude()) : apiService.getData(mKeyword);

                    call.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            mResult = response.body();
                            Intent intent = new Intent(DOWNLOAD_COMPLETE);
                            intent.putExtra(DOWNLOAD_MESSAGE, mResult);
                            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(mContext);
                            manager.sendBroadcast(intent);
                        }

                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.e("anindya", "onFailure: "+ t.getMessage());
                        }
                    });
                    Intent intent = new Intent(mContext, ResultsActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
