package cs2340.theratpack.ratapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String TAG = "MY_APP";

    /**
     * Firebase stuff
     */
    private DatabaseReference ref;

    /**
     * UI Stuff
     */
    RecyclerView ratList;
    FirebaseListAdapter adapter;

    /**
     * Whether or not the activity is in two pane mode,
     * i.e. running on a tablet device
     */
    private boolean mTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.ratList);
        assert ratList != null;

        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.rat_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


        ref = FirebaseDatabase.getInstance().getReference();

        /**
         * Logic to logout of the app and sign out of Firebase
         */
        Button logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        readCSV();
    }

    public void setupRecyclerView(@NonNull RecyclerView view) {
        view.setAdapter(new RecyclerViewAdapter(Model.INSTANCE.getRats()));
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private final List<Rat> mValues;

        public RecyclerViewAdapter(List<Rat> items) { mValues = items; }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText("" + mValues.get(position).getUniqueKey());
            holder.mContentView.setText(mValues.get(position).getIncidentAddress());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(RatFragment.ARG_ITEM_ID, holder.mItem.getUniqueKey());
                        RatFragment fragment = new RatFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.rat_detail_container, fragment)
                                .commit();

                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DetailedActivity.class);
                        Log.d("MYAPP", "Switch to detailed view for item: " + holder.mItem.getUniqueKey());
                        intent.putExtra(RatFragment.ARG_ITEM_ID, holder.mItem.getUniqueKey());

                        context.startActivity(intent);
                    }
                }

            });
        }

        @Override
        public int getItemCount() { return mValues.size(); }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Rat mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);

            }

            @Override
            public String toString() {
                return super.toString() + "'" + mContentView.getText();
            }
        }
    }

    private void readCSV() {
        Model model = Model.INSTANCE;

        try {
            InputStream is = getResources().openRawResource(R.raw.rat_sightings);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                Log.d(MainActivity.TAG, line);
                String[] tokens = line.split(",");
                int key = Integer.parseInt(tokens[0]);
                String created = tokens[1];
                String locationType = tokens[7];
                int zip = Integer.parseInt(tokens[8]);
                String address = tokens[9];
                String city = tokens[16];
                String borough = tokens[23];
                Double lat = Double.parseDouble(tokens[49]);
                Double lng = Double.parseDouble(tokens[50]);
                model.addRat(new Rat(key, created, locationType, zip, address, city, borough, lat, lng));
            }
            br.close();
        }
        catch (IOException e) {
            Log.e(MainActivity.TAG, "error reading assets", e);
        }
    }

}
