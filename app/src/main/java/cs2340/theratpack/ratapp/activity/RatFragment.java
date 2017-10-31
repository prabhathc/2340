package cs2340.theratpack.ratapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

import cs2340.theratpack.ratapp.model.Rat;
import cs2340.theratpack.ratapp.model.RatModel;
import cs2340.theratpack.ratapp.R;

//import android.app.Fragment;


/**
 * Created by Jamal Paden on 10/12/2017.
 */

public class RatFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "rat_key";

    private static final String TAG = "RatFragment";

    /**
     * The content this fragment is presenting
     */
    private Rat ratSighting;

    /**
     * Empty constructor
     */
    public RatFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String key = getArguments().getString(ARG_ITEM_ID);
            Log.d(TAG, "Opening details for " + key);
            ratSighting = RatModel.INSTANCE.findRatById(key);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout layout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (layout != null) {
                layout.setTitle(ratSighting.getIncidentAddress());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View rootView = inflater.inflate(R.layout.rat_detail, container, false);
        Log.d(TAG, "About to set data");

        //Show the data in a TextView
        if (ratSighting != null) {
            Log.d(TAG, "About to set key");
            ((TextView) rootView.findViewById(R.id.id2)).setText("" + ratSighting.getUniqueKey());
            Log.d(TAG, "Getting ready to set Created Date");
            ((TextView) rootView.findViewById(R.id.createdDate)).setText(new Date(ratSighting.getCreatedDate()).toString());

            ((TextView) rootView.findViewById(R.id.location_type)).setText(ratSighting.getLocationType());
            ((TextView) rootView.findViewById(R.id.zip)).setText(ratSighting.getIncidentZip());

            Log.d(TAG, "Getting ready to set address");
            ((TextView) rootView.findViewById(R.id.address)).setText(ratSighting.getIncidentAddress());

            ((TextView) rootView.findViewById(R.id.city)).setText(ratSighting.getCity());

            ((TextView) rootView.findViewById(R.id.borough)).setText(ratSighting.getBorough());

            ((TextView) rootView.findViewById(R.id.lat)).setText("" + ratSighting.getLatitude());

            ((TextView) rootView.findViewById(R.id.lng)).setText("" + ratSighting.getLongitude());

        }
        return rootView;
    }
}
