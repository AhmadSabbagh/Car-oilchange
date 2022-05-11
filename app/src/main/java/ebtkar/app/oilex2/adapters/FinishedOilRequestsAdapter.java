package ebtkar.app.oilex2.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ebtkar.app.oilex2.MapsActivity;
import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.RateFinishedRequest;
import ebtkar.app.oilex2.models.OilRequest;

/**
 * Created by Luminance on 6/29/2018.
 */

public class FinishedOilRequestsAdapter extends BasicRecycleviewAdapter {

    private class ModifiedView extends MyView{

        TextView oil , phone;
        View body;


        public ModifiedView(View view) {
            super(view);
            oil=view.findViewById(R.id.oilname);
            phone=view.findViewById(R.id.phone);
            body=view.findViewById(R.id.body);

        }




    }


    public FinishedOilRequestsAdapter(ArrayList cats, Activity c) {
        super(cats, c);
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_request_layout, parent, false);
        return new ModifiedView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
       final OilRequest p = (OilRequest)cats.get(position);
        ModifiedView v = (ModifiedView)holder;

        v.oil.setText(context.getString(R.string.oil_brand) +  p.getOil_name());
        v.phone.setText(context.getString(R.string.cus_phone) + p.getPhone());

v.body.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
Intent ii= new Intent(context, RateFinishedRequest.class);
        ii.putExtra("order_id" ,p.getId());
        context.startActivity(ii);
    }
});




    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.enable_gps_question)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused")
                                        final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
