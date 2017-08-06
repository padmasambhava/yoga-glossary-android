package uk.org.padma.yogaglossary;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.util.Log;
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

    public ArrayList<GEntry> entries = new ArrayList<GEntry>();
    private Filter mFilter;

    public GlossaryAdapter(Context context, ArrayList<GEntry> entries) {
        super(context, 0, entries);
        this.entries = entries;
    }

    /*static SpannableString createIndentedText(String text, int marginFirstLine, int marginNextLines) {
        SpannableString result=new SpannableString(text);
        result.setSpan(new LeadingMarginSpan.Standard(marginFirstLine, marginNextLines),0,text.length(),0);
        return result;
    } */
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
            //defi += createIndentedText( "\u2022 " +  iter.next(), 20, 20);
            //defi += "<li>" + iter.next() + "</li>";
            if(iter.hasNext()){
                defi += "\n";
            }
        }
        //tvDefinition.setText(Html.fromHtml(defi));
        tvDefinition.setText(defi);
        // Return the completed view to render on screen
        return convertView;
    }


    private class GlossFilter<T> extends Filter {

        private ArrayList<GEntry> sourceObjects;

        public GlossFilter(List<GEntry> objects) {
            sourceObjects = new ArrayList<GEntry>();
            synchronized (this) {
                sourceObjects.addAll(objects);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<GEntry> filter = new ArrayList<GEntry>();

                for (GEntry ent : sourceObjects) {
                    // the filtering itself:
                    if (ent.term.toString().toLowerCase().contains(filterSeq))
                        filter.add(ent);
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
            ArrayList<T> filtered = (ArrayList<T>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((GEntry) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null)
            mFilter = new GlossFilter<GEntry>(entries);
        return mFilter;
    }
}