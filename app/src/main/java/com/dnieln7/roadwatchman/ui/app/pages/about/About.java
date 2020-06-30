package com.dnieln7.roadwatchman.ui.app.pages.about;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dnieln7.roadwatchman.R;

/**
 * Shows a short description about the app.
 *
 * @author dnieln7
 */
public class About extends Fragment {

    public About() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }
}
