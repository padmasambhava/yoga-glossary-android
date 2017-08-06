package uk.org.padma.yogaglossary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GlossaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlossaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
    public ArrayList<GEntry> entries = new ArrayList<GEntry>();
    public GlossaryAdapter mAdapter;

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;
    //public ArrayList<MainActivity.GEntry> entries = new ArrayList<MainActivity.GEntry>();
    //public MainActivity.GlossaryAdapter mAdapter;

    public GlossaryFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GlossaryFragment newInstance() {
        GlossaryFragment fragment = new GlossaryFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
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


        mAdapter = new GlossaryAdapter(getActivity(), entries);

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
            //Log.i("term===", ent.term);
            JSONArray darr = je.optJSONArray("definition");
            for (int ii = 0; ii < darr.length(); ii++) {
                ent.definition.add( darr.optString(ii) );
            }
            entries.add(ent);

        }


    }
}
