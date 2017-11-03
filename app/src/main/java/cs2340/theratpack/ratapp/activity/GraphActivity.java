package cs2340.theratpack.ratapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Calendar;

import cs2340.theratpack.ratapp.R;

public class GraphActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Button testButton = (Button) findViewById(R.id.test_button);
        //for the time being its just going to stupidly reload all rats even if there is range overlap
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        GraphActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth,int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(year, monthOfYear, dayOfMonth);
        Calendar endDate = Calendar.getInstance();
        endDate.set(yearEnd, monthOfYearEnd, dayOfMonthEnd);
        ratModel.deleteAll();
        if (startDate != null && endDate != null) {
            ratsInRange(startDate.getTimeInMillis(),endDate.getTimeInMillis());
        }
        Log.d(TAG, "" +startDate.getTimeInMillis());

    }
}
