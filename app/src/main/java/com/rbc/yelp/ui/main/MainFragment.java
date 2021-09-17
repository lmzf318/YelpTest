package com.rbc.yelp.ui.main;

import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.rbc.yelp.databinding.MainFragmentBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainFragment extends Fragment {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;

    private MainViewModel mainViewModel;
    private MainFragmentBinding binding;
    private MainListAdapter mainListAdapter;

    // FIXME By default set city to Toronto
    private String city = "Toronto";
    private boolean locationPermissionGranted = false;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLocationPermission();
        updateCity();

        mainListAdapter = new MainListAdapter();
        binding.listviewResult.setAdapter(mainListAdapter);
        binding.listviewResult.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Log.d(TAG, "Clicked on group " + groupPosition + " and child " + childPosition);
            return true;
        });

        binding.searchBadge.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "Query term " + query + ", city " + city);
                mainViewModel.get(query, city).observe(requireActivity(), searchResult -> {
                    // collapse list to let user know that data has changed
                    int count =  mainListAdapter.getGroupCount();
                    for (int i = 0; i <count ; i++) binding.listviewResult.collapseGroup(i);
                    // notify list adapter to refresh UI
                    mainListAdapter.submit(searchResult.getBusinesses());
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        // switch for future case if we need more permissions
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }

    private void updateCity() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                Log.i(TAG, "Current location is:" + location.toString());
                Geocoder geoCoder = new Geocoder(requireActivity(), Locale.getDefault());
                try {
                    List<Address> list = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && !list.isEmpty()) {
                        city = TextUtils.isEmpty(list.get(0).getAddressLine(0)) ? city : list.get(0).getAddressLine(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}