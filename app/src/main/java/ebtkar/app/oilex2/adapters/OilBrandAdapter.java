package ebtkar.app.oilex2.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.activities.RequestDetailActivity;
import ebtkar.app.oilex2.models.OilBrand;

/**
 * Created by Luminance on 6/29/2018.
 */

public class OilBrandAdapter extends BasicRecycleviewAdapter implements Filterable{

    ArrayList original;

    ArrayList contactListFiltered;
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = original;
                } else {
                    List<OilBrand> filteredList = new ArrayList<>();
                    for (Object row : original) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (((OilBrand)row).getName().toLowerCase().contains(charString.toLowerCase())
                                ) {
                            filteredList.add((OilBrand)row);
                        }
                    }

                    contactListFiltered = (ArrayList<OilBrand>) filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cats = (ArrayList<OilBrand>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ModifiedView extends MyView{

        TextView title ,reco ,price;
        View body;


        public ModifiedView(View view) {
            super(view);
            title=view.findViewById(R.id.name);
            reco=view.findViewById(R.id.oil_reco);
            price=view.findViewById(R.id.oil_price);
            body=view.findViewById(R.id.body);

        }




    }


    public OilBrandAdapter(ArrayList cats, Activity c) {
        super(cats, c);
        this.original = cats;

    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_oil_layout, parent, false);
        return new ModifiedView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
       final OilBrand p = (OilBrand)cats.get(position);
        ModifiedView v = (ModifiedView)holder;

        v.title.setText(p.getName());
        v.reco.setText(p.getRecommend());
        v.price.setText(p.getPrice()+" "+context.getString(R.string.j_d)) ;
//        v.reco.setVisibility(View.VISIBLE);
//        v.price.setVisibility(View.VISIBLE);

v.body.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        RequestDetailActivity.oil_id = p.getId();
        RequestDetailActivity.oil_name = p.getName();
        context.finish();



    }
});




    }
}
