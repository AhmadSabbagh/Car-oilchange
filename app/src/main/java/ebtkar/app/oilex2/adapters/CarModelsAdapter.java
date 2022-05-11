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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.activities.RequestDetailActivity;
import ebtkar.app.oilex2.models.CarModel;

/**
 * Created by Luminance on 6/29/2018.
 */

public class CarModelsAdapter extends BasicRecycleviewAdapter implements Filterable {

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
                    List<CarModel> filteredList = new ArrayList<>();
                    for (Object row : original) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (((CarModel)row).getName().toLowerCase().contains(charString.toLowerCase())
                                ) {
                            filteredList.add((CarModel)row);
                        }
                    }

                    contactListFiltered = (ArrayList<CarModel>) filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cats = (ArrayList<CarModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ModifiedView extends MyView{

        TextView title;
        ImageView pic;
        View body;


        public ModifiedView(View view) {
            super(view);
            title=view.findViewById(R.id.name);
            pic=view.findViewById(R.id.logo);
            body=view.findViewById(R.id.body);

        }




    }

String dad_pic;
    public CarModelsAdapter(ArrayList cats, Activity c ,String dad_pic) {
        super(cats, c);
       this. dad_pic=dad_pic;
        this.original = cats;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_car_model_layout, parent, false);
        return new ModifiedView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
       final CarModel p = (CarModel)cats.get(position);
        ModifiedView v = (ModifiedView)holder;

        v.title.setText(p.getName());
        if(!dad_pic.equals(""))
        Glide.with(context).load(dad_pic)
              //  .thumbnail(Glide.with(context).load(R.drawable.loader))
                .into(v.pic);
v.body.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
//        Intent ii = new Intent(context, SelectOilCaliberActivity.class);
//        ii.putExtra("car_id" ,context.getIntent().getStringExtra("car_id"));
//        ii.putExtra("car_model_id" ,p.getId());
//        ii.putExtra("lng" ,context.getIntent().getStringExtra("lng"));
//        ii.putExtra("lat" ,context.getIntent().getStringExtra("lat"));
//        context.startActivity(ii);

        RequestDetailActivity.car_id = context.getIntent().getStringExtra("car_id");
        RequestDetailActivity.car_name = context.getIntent().getStringExtra("car_name");
        RequestDetailActivity.car_icon = context.getIntent().getStringExtra("icon");
        RequestDetailActivity.car_model_id = p.getId();
        context.finish();

//        Intent ii = new Intent(context, SelectCarModel.class);
//        ii.putExtra("car_id" ,p.getId());
//        context.startActivity(ii);

       // Toast.makeText(context, "لا بيانات حالياً", Toast.LENGTH_SHORT).show();
//        BrandProductsFragment f = new BrandProductsFragment();
//        Bundle b = new Bundle();
//        b.putString("id", p.getId());
//        f.setArguments(b);
//        ((MainActivity) context).openFragment(f, false);

    }
});




    }
}
