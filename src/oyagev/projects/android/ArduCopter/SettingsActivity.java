package oyagev.projects.android.ArduCopter;


import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	public static final String PREFS_NAME = "YCommPrefs";
	public static final String TAG = "Settings";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		Button addButton = (Button)findViewById(R.id.settings_add);
		addButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                addControl(null,null,0);
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	public void onPause(){
		super.onPause();
		storeControls();
		
	}
	public void onResume(){
		super.onResume();
		wakeupControls();
	}
	
	public LinearLayout addControl(String name, String type, int cmdVal){
		
		LinearLayout row = new LinearLayout(this); 
		row.setOrientation(LinearLayout.HORIZONTAL);
		row.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		
		EditText text = new EditText(this);
		text.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		text.setEms(5);
		text.setTag("name");
		if (name != null){
			text.setText(name.toString());
		}
		
		EditText cmd = new EditText(this);
		cmd.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		cmd.setEms(2);
		cmd.setTag("cmd");
		cmd.setText(String.valueOf(cmdVal));
		
		Spinner spinner = new Spinner(this);
		spinner.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		
		spinner.setTag("type");
		
		
		Button rm = new Button(this);
		rm.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		rm.setText("X");
		rm.setEms(1);
		rm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				((LinearLayout)findViewById(R.id.rows_layout)).removeView((View)v.getParent());
				
			}
		});
		
		row.addView(text);
		row.addView(cmd);
		row.addView(spinner);
		row.addView(rm);
		((LinearLayout)findViewById(R.id.rows_layout)).addView(row);
		
		//Spinner spinner = (Spinner) findViewById(R.id.types_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.types_spinner, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		if (type != null){
			int pos = adapter.getPosition(type);
			spinner.setSelection(pos);
		}
		
		return row;
	}
	
	private void storeControls(){
		try{
			LinearLayout lay = (LinearLayout)findViewById(R.id.rows_layout);
			JSONObject prefs = new JSONObject();
			JSONArray controls = new JSONArray();
			
			//Vector<JSONObject> controls = new Vector<JSONObject>();
			
			for (int i=0; i<lay.getChildCount(); i++){
				LinearLayout row = (LinearLayout)lay.getChildAt(i);
				JSONObject rowObj = new JSONObject();
				
				EditText name = (EditText)row.findViewWithTag("name");
				rowObj.put("name", name.getText());
				
				Spinner type = (Spinner)row.findViewWithTag("type");
				rowObj.put("type", type.getSelectedItem().toString());
				
				EditText cmd = (EditText)row.findViewWithTag("cmd");
				rowObj.put("cmd", cmd.getText());
				controls.put(rowObj);
			}
			prefs.put("controls", controls);
			
			//Toast.makeText(this, prefs.toString(), Toast.LENGTH_LONG).show();
			Log.d(TAG, prefs.toString());
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		    SharedPreferences.Editor editor = settings.edit();
		    editor.putString("prefs", prefs.toString());
		      // Commit the edits!
		    editor.commit();
		}catch(Exception e){
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	private void wakeupControls(){
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
		String strPrefs = settings.getString("prefs", "{}");
		try {
			JSONObject prefs = new JSONObject(strPrefs);
			JSONArray controls = prefs.getJSONArray("controls");
			for (int i=0;i<controls.length();i++){
				JSONObject row = (JSONObject) controls.get(i);
				addControl(row.getString("name"), row.getString("type"), row.getInt("cmd"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
			Log.e(TAG, e.getMessage().toString());
		}
		
		
	}

}
