package com.example.yamba;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DialogViewer extends DialogFragment {

    /**
     * Create a new instance of DialogViewer
     */
    static DialogViewer newInstance() {
        DialogViewer f = new DialogViewer();
        f.setStyle(STYLE_NO_TITLE, 0);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.progress_indicator, container, false);
        return v;
    }
}
