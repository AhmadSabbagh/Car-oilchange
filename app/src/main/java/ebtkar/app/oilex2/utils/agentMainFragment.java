package ebtkar.app.oilex2.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import ebtkar.app.oilex2.R;


public class agentMainFragment extends Fragment  {
    public static final int LOCATION_PERMISSION = 124;
    private GoogleMap mMap;

    LatLng selected = null;
    public agentMainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.activity_agent_main, container, false);



        return rootView;
    }


}
