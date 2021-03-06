package com.mlvolt.protienboothuser;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlvolt.protienboothuser.Model.TodayModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class Today extends Fragment {



    public Today() {
        // Required empty public constructor
    }

    TextView workout, weight, postworkout, preworkout;
    TextView todayDate;
    BarGraph barGraph;
    DatabaseReference databaseReference;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date;
// ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        final String userContactNo = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        weight = view.findViewById(R.id.weight);
        workout = view.findViewById(R.id.workout);
        preworkout = view.findViewById(R.id.preworkout);
        postworkout = view.findViewById(R.id.postworkout);
        todayDate = view.findViewById(R.id.today_date);
        barGraph = new BarGraph();
        barGraph.setBarChart((BarChart) view.findViewById(R.id.barchart));

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("MMM d");
        date = simpleDateFormat.format(calendar.getTime());
        todayDate.setText(date);


        ValueEventListener valueEventListener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TodayModel todayModel = dataSnapshot.child(userContactNo).getValue(TodayModel.class);
                weight.setText(todayModel.getWeight());
                workout.setText(todayModel.getWorkout());
                preworkout.setText(todayModel.getPreworkout());
                postworkout.setText(todayModel.getPostworkout());
                barGraph.fun(1000, Integer.parseInt(todayModel.getYourProteinQuantity()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addValueEventListener(valueEventListener);


        return view;
    }
}
