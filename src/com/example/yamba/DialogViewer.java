package com.example.yamba;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DialogViewer extends DialogFragment {

	static int layout;

	static DialogViewer newInstance(int l) {
        DialogViewer f = new DialogViewer();
        f.setStyle(STYLE_NO_TITLE, 0);
        layout = l;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = null;
		try {
			v = inflater.inflate(layout, container, false);
		} catch (InflateException e) {
			e.printStackTrace();
		}
        return v;
    }
}
