package uk.org.padma.yogaglossary;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GlossaryAdapter extends ArrayAdapter<GEntry>  implements Filterable {

    public static final String FILTER_ALL = "FILTER_ALL";
    public static final String FILTER_TERM = "FILTER_TERM";
    public static final String FILTER_DEFINITION = "FILTER_DEFINITION";



    public ArrayList<GEntry> entries = new ArrayList<GEntry>();
    public Filter mFilter;
    public String filter_field;

    public GlossaryAdapter(Context context, ArrayList<GEntry> entries) {
        super(context, 0, entries);
        this.entries = entries;
    }

    //private String filter_in;
    public void setFilterField(String txt){
        filter_field = txt;
        notifyDataSetInvalidated();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        GEntry entry = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.glossary_listview_row, parent, false);
        }
        // Lookup view for data population
        TextView tvTerm = (TextView) convertView.findViewById(R.id.tvTerm);
        TextView tvDefinition = (TextView) convertView.findViewById(R.id.tvDefinition);

        // Populate the data into the template view using the data object
        tvTerm.setText(entry.term);

        String defi = "";
        ListIterator<String> iter = entry.definition.listIterator();
        while(iter.hasNext()){
            defi = defi + "\u2022 " + iter.next();
            if(iter.hasNext()){
                defi += "\n";
            }
        }
        tvDefinition.setText(defi);

        return convertView;
    }


    public class GlossFilter<T> extends Filter {

        public ArrayList<GEntry> sourceObjects;
        //public String filter_field;
        private GlossaryAdapter mParent;

        public GlossFilter(List<GEntry> objects, GlossaryAdapter parent) {
             mParent = parent;
            sourceObjects = new ArrayList<GEntry>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }
        //public void setFilterField(String txt){
        //    this.filter_field = txt;
       // }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {

            String filterSeq = chars.toString().toLowerCase();

            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<GEntry> filter = new ArrayList<GEntry>();
                //String filter_field = mParent.filter_field;
                //for (GEntry ent : sourceObjects) {
                for (int idx = 0; idx < sourceObjects.size(); idx++){
                    GEntry ent = sourceObjects.get(idx);
                    // the filtering itself:
                    switch(mParent.filter_field) {

                        case GlossaryAdapter.FILTER_TERM:
                            if (ent.term.toLowerCase().contains(filterSeq)) {
                                filter.add(ent);
                            }
                            break;

                        case GlossaryAdapter.FILTER_DEFINITION:
                            for(int i = 0; i < ent.definition.size(); i++) {
                                if (ent.definition.get(i).toLowerCase().contains(filterSeq)) {
                                    filter.add(ent);
                                }
                            }
                            break;

                        case GlossaryAdapter.FILTER_ALL:
                            if (ent.search.toLowerCase().contains(filterSeq)) {
                                filter.add(ent);
                            }

                            break;
                    }
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceObjects;
                    result.count = sourceObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<GEntry> filtered = (ArrayList<GEntry>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((GEntry) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }



    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new GlossFilter<GEntry>(entries, this);
        }
        return mFilter;
    }
}