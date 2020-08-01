package com.dnieln7.roadwatchman.ui.app.pages.about;

import android.content.Intent;
import android.net.Uri;
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
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        root.findViewById(R.id.about_gmail).setOnClickListener(v -> openEmail());
        root.findViewById(R.id.about_github).setOnClickListener(v -> goToGithub());

        return root;
    }

    private void openEmail() {
        Intent gmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "dnieln7@gmail.com"));
        startActivity(Intent.createChooser(gmailIntent, ""));
    }

    private void goToGithub() {
        Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/dnieln7"));
        startActivity(Intent.createChooser(githubIntent, ""));
    }
}
