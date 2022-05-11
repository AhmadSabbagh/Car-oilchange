package ebtkar.app.oilex2.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.RateFinishedRequest;
import ebtkar.app.oilex2.activities.RateUserActivity;
import ebtkar.app.oilex2.models.Order;

/**
 * Created by Luminance on 6/29/2018.
 */

public class MyOrdersAdapter extends BasicRecycleviewAdapter {

    private class ModifiedView extends MyView {

        TextView oil, agent, price, date_time;
        View body;
        String order_id;


        public ModifiedView(View view) {
            super(view);
            oil = view.findViewById(R.id.oil_name);
            agent = view.findViewById(R.id.agent_name);
            price = view.findViewById(R.id.price);
            date_time = view.findViewById(R.id.date_time);
            body = view.findViewById(R.id.body);




        }


    }


    public MyOrdersAdapter(ArrayList cats, Activity c) {
        super(cats, c);
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_order_layout, parent, false);
        return new ModifiedView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        final Order p = (Order) cats.get(position);
        ModifiedView v = (ModifiedView) holder;




        v.oil.setText(context.getString(R.string.oil_brand) + p.getOil_name()
                + " (" + ((p.isStatus()) ? context.getString(R.string.finshed) :
                context.getString(R.string.pinding))
        +").");
        if(!p.isStatus())
        v.agent.setText(context.getString(R.string.agent_name) + p.getAgent_name());
        else
            v.agent.setVisibility(View.GONE);



        v.price.setText(context.getString(R.string.price) + p.getPrice());
        v.date_time.setText(context.getString(R.string.time) + p.getDate() + " " + p.getTime());
        if(p.isStatus()){
        v.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


    Intent intent = new Intent(context, RateUserActivity.class);
     intent.putExtra("message" ,context.getString(R.string.rate_agent)+p.getAgent_name());
     intent.putExtra("message" ,context.getString(R.string.rate_agent)+p.getAgent_name());

              //  Intent ii= new Intent(context, RateUserActivity.class);
                intent.putExtra("order_id" ,p.getId());
                context.startActivity(intent);





//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    context.startActivity(intent);


    //Toast.makeText(context, "Rating..", Toast.LENGTH_SHORT).show();


            }
        });
        }else{
v.body.setVisibility(View.GONE);
        }
    }
}
