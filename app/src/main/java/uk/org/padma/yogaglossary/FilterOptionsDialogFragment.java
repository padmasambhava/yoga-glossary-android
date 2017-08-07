package uk.org.padma.yogaglossary;


import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;


public class FilterOptionsDialogFragment extends DialogFragment {

    private String filter_in = GlossaryAdapter.FILTER_ALL;

    public FilterOptionsDialogFragment() {
        // Required empty public constructor
    }

    public static FilterOptionsDialogFragment newInstance(String filter_in) {
        FilterOptionsDialogFragment fragment = new FilterOptionsDialogFragment();

        Bundle args = new Bundle();
        args.putString("filter_in", filter_in);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filter_in = getArguments().getString("filter_in");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_options, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RadioButton rad = (RadioButton) view.findViewById(R.id.radio_all);
        rad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });
        rad.setChecked(filter_in == GlossaryAdapter.FILTER_ALL);

        rad = (RadioButton) view.findViewById(R.id.radio_terms);
        rad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });
        rad.setChecked(filter_in == GlossaryAdapter.FILTER_TERM);

        rad = (RadioButton) view.findViewById(R.id.radio_definitions);
        rad.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });
        rad.setChecked(filter_in == GlossaryAdapter.FILTER_DEFINITION);

        getDialog().setTitle("Filter by");

        // Show soft keyboard automatically and request focus to field
        //mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(
        //        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void onRadioButtonClicked(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.radio_terms:
                if (checked)
                    filter_in = GlossaryAdapter.FILTER_TERM;
                break;

            case R.id.radio_definitions:
                if (checked)
                    filter_in = GlossaryAdapter.FILTER_DEFINITION;
                break;

            case R.id.radio_all:
                if (checked){
                    filter_in = GlossaryAdapter.FILTER_ALL;
                }
        }
        Log.i("YES", filter_in);
        sendBackResult();
    }

    // Defines the listener interface
    public interface FilterOptionsDialogListener {
        void onFinishEditDialog(String filter_in);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        FilterOptionsDialogListener listener = (FilterOptionsDialogListener) getTargetFragment();
        listener.onFinishEditDialog(filter_in);
        dismiss();
    }
}
