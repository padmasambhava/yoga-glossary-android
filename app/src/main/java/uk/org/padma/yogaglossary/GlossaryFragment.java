package uk.org.padma.yogaglossary;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;



public class GlossaryFragment extends Fragment implements FilterOptionsDialogFragment.FilterOptionsDialogListener {

    public GlossaryAdapter mAdapter;

    EditText txtFilter;
    Button buttClear;
    Button buttFilterOptions;


    public GlossaryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GlossaryFragment newInstance() {
        GlossaryFragment fragment = new GlossaryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_glossary, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttFilterOptions = (Button) getActivity().findViewById(R.id.butt_filter_options);
        buttFilterOptions.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());

                 FragmentManager fm = getActivity().getSupportFragmentManager();
                 FilterOptionsDialogFragment optsDialog = FilterOptionsDialogFragment.newInstance(mSettings.getString("filter_in", GlossaryAdapter.FILTER_ALL));
                 optsDialog.setTargetFragment(GlossaryFragment.this, 300);
                 optsDialog.show(fm, "fragment_edit_name");
             }
         });
        // Setup Clear Button
        buttClear = (Button) getActivity().findViewById(R.id.butt_clear);
        buttClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtFilter.setText("");
            }
        });

        // Setup Filter Text
        txtFilter = (EditText) getActivity().findViewById(R.id.txt_filter);
        int leadingMargin = 16;
        //txtFilter.setSpan(new BulletSpan(leadingMargin), start, end, 0);
        txtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
             }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("ontext", String.valueOf(s));
                if (s.length() == 0) {
                    buttClear.setVisibility(View.INVISIBLE);
                } else {
                    buttClear.setVisibility(View.VISIBLE);
                }

                mAdapter.getFilter().filter(s.toString());
            }
        });



        // Setup adapter and filter
        mAdapter = new GlossaryAdapter(getActivity(), new ArrayList<GEntry>());
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mAdapter.setFilterField(mSettings.getString("filter_in", GlossaryAdapter.FILTER_ALL));

        ListView mListView = (ListView) getActivity().findViewById(R.id.glossary_listview);
        mListView.setAdapter(mAdapter);

        String json_str = null;
        try {

            InputStream inp_stream = getContext().getAssets().open("yoga-glossary.json");
            int size = inp_stream.available();
            byte[] buffer = new byte[size];
            inp_stream.read(buffer);
            inp_stream.close();
            json_str = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        // Decode JSON
        JSONArray m_glossary = null;
        try {
            m_glossary = new JSONArray(json_str);

        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JSONObject je;
        for (int i = 0; i < m_glossary.length(); i++) {
            try {
                je = m_glossary.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            GEntry ent = new GEntry() ;
            ent.term = je.optString("term");
            ent.search = ent.term;
            //Log.i("term===", ent.term);
            JSONArray darr = je.optJSONArray("definition");
            for (int ii = 0; ii < darr.length(); ii++) {
                ent.definition.add( darr.optString(ii) );
                ent.search += darr.optString(ii).replace("'","").replace(",", "").replace(" ", "");
            }
            mAdapter.entries.add(ent);
            //entries.add(ent);

        }


    }

    // This is called when the dialog is completed and the results have been passed
    @Override
    public void onFinishEditDialog(String nfilter_in) {
        //Log.i("YES RETURN", nfilter_in);
        switch(nfilter_in){
            case GlossaryAdapter.FILTER_ALL:
                buttFilterOptions.setText("Everything");
                break;
            case GlossaryAdapter.FILTER_TERM:
                buttFilterOptions.setText("Terms");
                break;
            case GlossaryAdapter.FILTER_DEFINITION:
                buttFilterOptions.setText("Defintions");
                break;
        }
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString("filter_in", nfilter_in);
        editor.apply();
        mAdapter.setFilterField(nfilter_in);
        //mAdapter.getFilter().filter(txtFilter.toString());

    }
}
