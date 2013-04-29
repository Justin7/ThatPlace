package com.my.thatplace;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class PlaceActivity extends Activity {
	private final String TAG = "ThatPlace";
	private AutoCompleteTextView txt_stn;
	private Spinner spn_gateNum;
	private Button btn_add;
	private Button btn_search;
	private AutoCompleteTextView txt_place;
	private ListView lv_result;
	public ArrayList<String> array_result; 
	private String[] mPlaces;
	private PlaceController pController;
	private static String stn, gate, place;
	private String mCurItem;
	private boolean mWillBeDeleted = false;
	private final int DLG_DEL_ITEM = 0;

    public PlaceActivity() {
		super();
		// TODO Auto-generated constructor stub
		mPlaces = new String[] {"one", "two", "twotwo"};
	}

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
		
		createUiAdapter();
				
		pController = new PlaceController(this);
		
	
	    /*
	     * Click add
	     */
	    btn_add.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( readCurrentEditText(Util.NORMAL) == Util.NG) {
					return;
				}

				pController.addPlace(stn, gate, place);
				Toast.makeText(PlaceActivity.this, "추가완료 : " + stn + " / " + gate + " / " + place, Toast.LENGTH_SHORT).show();
				txt_place.setText("");
			}
		});

	    
	    /*
	     * Click Search
	     */
	    btn_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( readCurrentEditText(Util.ONLY_PLACE) == Util.NG) {
					return;
				}
				
				array_result = pController.searchPlace(stn, place);
				final ArrayAdapter<String> adapter_result = new ArrayAdapter<String>(
				PlaceActivity.this, android.R.layout.simple_list_item_1, array_result);
				adapter_result.notifyDataSetChanged();
				lv_result.setAdapter(adapter_result);
				
//				ArrayAdapter<String> adapter_result = new ArrayAdapter<String>(
//						PlaceActivity.this, android.R.layout.simple_list_item_1, array_result);
//				lv_result.setAdapter(adapter_result);
			}
	    });
	    
		/*
		 * ListView에서 Item 선택 시 Dialog 띄움
		 */
		lv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				
				mCurItem = array_result.get(pos);
				showDialog(DLG_DEL_ITEM);
				if (mWillBeDeleted = true) {
					pController.deletePlace(pos);
				}
				
				
			}
		});
	    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void createUiAdapter() {
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
    
    public int readCurrentEditText(int req) {
    	stn = txt_stn.getText().toString();
		gate = spn_gateNum.getSelectedItem().toString();
		place = txt_place.getText().toString();
		if (stn.equals("") || (place.equals("")) && (req == Util.NORMAL)) {
			Toast.makeText(PlaceActivity.this, "역/장소를 입력해주세요", Toast.LENGTH_SHORT).show();
			return Util.NG;
		}
		return Util.OK;
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
    	// TODO Auto-generated method stub
    	AlertDialog dlg = null;

    	switch(id) {
    	case DLG_DEL_ITEM :
    		dlg = new AlertDialog.Builder(this)
    		.setIcon(R.drawable.ic_launcher)
    		.setTitle("삭제할까요?")
    		.setMessage(mCurItem)
    		.setPositiveButton("삭제", new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				mWillBeDeleted = true;
    			}
    		})
    		.setNegativeButton("취소", new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				mWillBeDeleted = false;;
    			}
    		})		
    		.create();
    		break;

    	default :
    		break;
    	}
    	return dlg;    	
    }
    
}
