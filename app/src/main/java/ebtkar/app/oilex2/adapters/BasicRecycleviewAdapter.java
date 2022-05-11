package ebtkar.app.oilex2.adapters;

/**
 * Created by Luminance on 1/6/2018.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public abstract class BasicRecycleviewAdapter extends RecyclerView.Adapter<BasicRecycleviewAdapter.MyView> {

    ArrayList cats;
    public Activity context;
    public class MyView extends RecyclerView.ViewHolder {




        public MyView(View view) {
            super(view);
        }
    }


    public BasicRecycleviewAdapter(ArrayList cats , Activity c) {
        this.cats = cats;
        this.context=c;
    }
    @Override
    public int getItemCount() {
        return cats.size();
    }

}