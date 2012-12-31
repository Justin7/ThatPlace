package com.my.thatplace;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final String TAG = "ThatPlace";
	private AutoCompleteTextView txt_stn;
	private Spinner spn_gateNum;
	private Button btn_add;
	private Button btn_search;
	private AutoCompleteTextView txt_place;
	private ListView lv_result;
	private PlaceDbAdapter mDbAdapter;
	private ArrayList<String> array_result; 
	private String[] mPlaces = new String[] {"one", "two", "twotwo"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*
         * Initialize widgets
         */
        txt_stn = (AutoCompleteTextView)findViewById(R.id.txt_stn);
        spn_gateNum = (Spinner)findViewById(R.id.spn_gateNum);
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_search = (Button)findViewById(R.id.btn_search);
        txt_place = (AutoCompleteTextView)findViewById(R.id.txt_place);
        lv_result = (ListView)findViewById(R.id.list_result);
        array_result = new ArrayList<String>();
        
        createAdapter();
	    
	    /*
	     * Create DB here.
	     */
	    mDbAdapter = new PlaceDbAdapter(this);
	    mDbAdapter.open();
	    
	    /*
	     * Add Place
	     */
	    btn_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String stn = null;
				String gate = null;
				String place = null;
				
				stn = txt_stn.getText().toString();
				gate = spn_gateNum.getSelectedItem().toString();
				place = txt_place.getText().toString();

				if (stn.length() == 0 || gate.length() == 0 || place.length() == 0) {
					Toast.makeText(MainActivity.this, "역/출구/장소를 입력해주세요", Toast.LENGTH_SHORT).show();
				} else {
					mDbAdapter.createPlace(stn, gate, place);
					Toast.makeText(MainActivity.this, "추가완료 : " + stn + " / " + gate + " / " + place, Toast.LENGTH_SHORT).show();
				}
			}
		});
	    
	    /*
	     * Search Place
	     */
	    btn_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String org_stn = null;
				String new_stn = null;
				String gate = null;
				String place = null;
				org_stn = txt_stn.getText().toString();
				place = txt_place.getText().toString();

				if (org_stn != null && place.length() == 0) {
					Cursor c = mDbAdapter.fetchAllPlaces();
					c.moveToFirst();

					while(!c.isAfterLast()){
						new_stn = c.getString(1);
						gate = c.getString(2);
						place = c.getString(3);

						if (new_stn.equals(org_stn)) {
							StringBuffer sb = new StringBuffer();
							sb.append(new_stn)
							.append(" / ")
							.append(gate)
							.append(" / ")
							.append(place);
							array_result.add(sb.toString());
						}
						c.moveToNext();
					}
					c.close();
				}
				ArrayAdapter<String> adapter_result = new ArrayAdapter<String>(
						MainActivity.this, android.R.layout.simple_list_item_1, array_result);
				lv_result.setAdapter(adapter_result);
			}
	    });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void createAdapter() {
    	// Create ArrayAdapters
	    ArrayAdapter<CharSequence> adapter_gateNum = ArrayAdapter.createFromResource(this,
	    		R.array.GateNum, android.R.layout.simple_spinner_item);
	    ArrayAdapter<String> adapter_place = new ArrayAdapter<String>(this,
	    		android.R.layout.simple_dropdown_item_1line, mPlaces);

	    // Specify the layout to use when the list of choices appears
	    adapter_gateNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	    // Apply the adapter to the spinner
	    spn_gateNum.setAdapter(adapter_gateNum);
	    txt_place.setAdapter(adapter_place);
    }

    
}
