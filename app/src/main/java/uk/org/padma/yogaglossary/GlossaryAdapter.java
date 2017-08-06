package uk.org.padma.yogaglossary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ListIterator;

public class GlossaryAdapter extends ArrayAdapter<GEntry> {
    public GlossaryAdapter(Context context, ArrayList<GEntry> entries) {
        super(context, 0, entries);
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
            //System.out.println(iterator.next());
            //System.out.println(iterator.previous());
            defi = defi + "\u2022 " + iter.next();

            if(iter.hasNext()){
                defi += "\n";
            }

        }
        tvDefinition.setText(defi);
        // Return the completed view to render on screen
        return convertView;
    }
}