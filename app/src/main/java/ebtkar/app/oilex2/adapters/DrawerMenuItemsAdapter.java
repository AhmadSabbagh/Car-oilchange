package ebtkar.app.oilex2.adapters;

/**
 * Created by Luminance on 1/6/2018.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;



import java.util.ArrayList;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.models.CostumeMenuItem;

public class DrawerMenuItemsAdapter extends RecyclerView.Adapter<DrawerMenuItemsAdapter.MyView> {

    ArrayList cats;
    public Activity context;
    public class MyView extends RecyclerView.ViewHolder {


        public View body;
        public TextView title;
        public ImageView icon;


        public MyView(View view) {
            super(view);

                body =  view.findViewById(R.id.btn);
                title =  view.findViewById(R.id.title);
                icon =  view.findViewById(R.id.icon);


        }
    }


    public DrawerMenuItemsAdapter(ArrayList cats , Activity c) {
        this.cats = cats;
        this.context=c;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_nav_drawer_item, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

       final CostumeMenuItem p = ((CostumeMenuItem)cats.get(position));

        holder.title.setText(p.getTitle());
        Glide.with(context).load(p.getIcon()).into(holder.icon);

holder.body.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
p.doAction(v);
    }
});

    }
    @Override
    public int getItemCount() {
        return cats.size();
    }
}