package com.pennapps.instanote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by QingxiaoDong on 9/5/15.
 */
public class EditFragment extends Fragment {

    public EditFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =  inflater.inflate(R.layout.fragment_event_edit, container, false);


        return rootView;
    }
}
