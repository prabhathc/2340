package cs2340.theratpack.ratapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    public static RatModel ratModel = RatModel.INSTANCE;

    /**
     * UI Stuff
     */
    EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView ratListView;
    RecyclerAdapter adapter;

    private int pageNumber = 0;
    private int ratsPerPage = 20; //this number can be adjusting for cacheing/speed purposes
    private String anchorKey; //see loadNextPage

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_layout);
        //setSupportActionBar(toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button logout = (Button) findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                ratModel.deleteAll();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        Button addRat = (Button) findViewById(R.id.add_button);
        addRat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "i work");
                startActivity(new Intent(MainActivity.this, AddRatActivity.class));
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(TAG, "AuthState changed");

            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        recyclerSetup();
    }

    private void recyclerSetup() {
        ratListView = (RecyclerView) findViewById(R.id.recycler_view);
        ratListView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ratListView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(ratModel.getRats(), R.layout.ratlist_content);
        ratListView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextPage();//TODO: make this work lol
            }
        };
        loadNextPage();
        ratListView.addOnScrollListener(scrollListener);
    }

    private void loadNextPage() {
        //if its the first page, you want to order by key, but you dont have to start at a specific key
        if (pageNumber == 0) {
            mDatabase.child("rat sightings").orderByKey().limitToFirst(ratsPerPage)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> ratSnaps = dataSnapshot.getChildren();

                            for (DataSnapshot ratSnap : ratSnaps) {
                                ratModel.add(new Rat(ratSnap));
                                anchorKey = ratSnap.getKey();
                            }

                            Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()) + " rats loaded");
                            pageNumber++;
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        } else {
            //if youre on any later query, you want to start at the last key of the last query
            //make the limit+1, then skip the first key (you already have it in your prev query)
            mDatabase.child("rat sightings").orderByKey().startAt(anchorKey).limitToFirst(ratsPerPage + 1)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> ratSnaps = dataSnapshot.getChildren();
                            for (DataSnapshot ratSnap : ratSnaps) {
                                if (!ratSnap.getKey().equals(anchorKey)) {
                                    ratModel.add(new Rat(ratSnap));
                                    anchorKey = dataSnapshot.getKey();
                                }
                            }

                            Log.d(TAG, String.valueOf(dataSnapshot.getChildrenCount()) + " rats loaded");
                            pageNumber++;
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private List<Rat> ratsList;
        private int itemLayout;

        public RecyclerAdapter(List<Rat> ratsList, int itemLayout) {
            this.ratsList = ratsList;
            this.itemLayout = itemLayout;
            Log.d(TAG, "Recycler Adapter Created");
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.rat = ratsList.get(position);
            holder.idView.setText(holder.rat.getUniqueKey());
            holder.mContentView.setText(String.valueOf(ratsList.get(position).getLatitude()));
        }

        @Override public int getItemCount() {
            return ratsList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View view;
            public final TextView idView;
            public final TextView mContentView;
            public Rat rat;

            public ViewHolder(View view) {
                super(view);
                this.view = view;
                idView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

}
