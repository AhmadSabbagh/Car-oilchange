package ebtkar.app.oilex2.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ebtkar.app.oilex2.R;
import ebtkar.app.oilex2.models.CarModel;
import ebtkar.app.oilex2.models.Question;

/**
 * Created by Luminance on 6/29/2018.
 */

public class SupportAdapter extends BasicRecycleviewAdapter implements Filterable {
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
                    List<Question> filteredList = new ArrayList<>();
                    for (Object row : original) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (((CarModel)row).getName().toLowerCase().contains(charString.toLowerCase())
                                ) {
                            filteredList.add((Question)row);
                        }
                    }

                    contactListFiltered = (ArrayList<Question>) filteredList;
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

        TextView question,replay,date;


        public ModifiedView(View view) {
            super(view);
            question=view.findViewById(R.id.question);
            replay=view.findViewById(R.id.reply);
            date=view.findViewById(R.id.date);

        }




    }

    public SupportAdapter(ArrayList cats, Activity c) {
        super(cats, c);
        this.original = cats;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_support_layout, parent, false);
        return new ModifiedView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
       final Question p = (Question)cats.get(position);
        ModifiedView v = (ModifiedView)holder;

        v.question.setText(p.getRequest());
        v.date.setText(p.getDate());
        v.replay.setText(p.getReply());




    }
}
