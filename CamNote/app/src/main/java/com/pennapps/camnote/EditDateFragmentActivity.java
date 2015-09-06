package com.pennapps.camnote;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EditDateFragmentActivity extends FragmentActivity
        implements DatePickerFragment.OnDateSetListener, TimePickerFragment.OnTimeSetListener {

    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_date_fragment);
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        DialogFragment dialogFragment = new TimePickerFragment();
        dialogFragment.show(getFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(int hour, int minute) {
        // do sth
        Intent resultIntent = new Intent();
        resultIntent.putExtra("year", this.year);
        resultIntent.putExtra("month", this.month);
        resultIntent.putExtra("day", this.day);
        resultIntent.putExtra("hour", hour);
        resultIntent.putExtra("minute", minute);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void onCancel() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
        finish();
    }

    @Override
    public void onTimeCancel(){
        onCancel();
    }
    @Override
    public void onDateCancel(){
        onCancel();
    }

}
