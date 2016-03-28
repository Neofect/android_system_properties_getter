package com.neofect.app.system_properties_getter;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private static final String LOG_TAG = MainActivity.class.getSimpleName();

	private ArrayList<String> properties = null;

	private static ArrayList<String> getProperties() throws IOException {
		ArrayList<String> properties = new ArrayList<>();
		Process process = process = Runtime.getRuntime().exec("getprop");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line = null;
		final StringBuffer buffer = new StringBuffer(2048);
		while ((line = bufferedReader.readLine()) != null) {
			properties.add(line);
		}
		return properties;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Add the change listener
		EditText filter = (EditText)findViewById(R.id.editTextFilter);
		filter.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s != null) {
					filterProperties(s.toString());
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
		});


		// Get system properties
		try {
			properties = getProperties();
		} catch (Exception e) {
			updateConsoleWithException(e);
			return;
		}

		// Show Build.MANUFACTURER, Build.MODEL
		fillBuildData();

		// Update the console
		filterProperties(null);
	}

	private void fillBuildData() {
		String data = "";
		data += "[Build.MANUFACTURER]: [" + Build.MANUFACTURER + "]\n";
		data += "[Build.MODEL]: [" + Build.MODEL + "]";

		TextView buildData = (TextView) findViewById(R.id.editTextBuildData);
		buildData.setText(data);
	}

	private void filterProperties(String keyword) {
		String content = "";
		for (String line : properties) {
			if (keyword != null && line.toLowerCase().indexOf(keyword.toLowerCase()) < 0) {
				continue;
			}
			if (!content.equals("")) {
				content += "\n";
			}
			content += line;
		}
		updateConsole(content);
	}

	private void updateConsoleWithException(Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		e.printStackTrace(new PrintWriter(writer));
		printWriter.flush();
		updateConsole(writer.toString());
	}

	private void updateConsole(String content) {
		TextView systemProperties = (TextView) findViewById(R.id.editTextProperties);
		systemProperties.setText(content);
	}

}
