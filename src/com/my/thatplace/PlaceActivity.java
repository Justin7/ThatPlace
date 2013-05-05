package com.my.thatplace;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class PlaceActivity extends Activity {
	private final String TAG = "ThatPlace";
	
	// UI Resources
	private AutoCompleteTextView txt_stn;
	private Spinner spn_gateNum;
	private Button btn_add;
	private Button btn_search;
	private AutoCompleteTextView txt_place;
	private ListView lv;
	
	// Data
	public ArrayList<String> items; 
	ArrayAdapter<String> adapter;
	private String[] mPlaces;
	private static String mStn, mGate, mPlace;
	private String mCurItem;
	private int mPos;
	private final int DLG_DEL_ITEM = 0;
	
	// DB
	public PlaceDbAdapter mDbAdapter;

    public PlaceActivity() {
		super();
		// TODO Auto-generated constructor stub
		mPlaces = new String[] {"one", "two", "twotwo"};
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		 // Initialize widgets
		txt_stn = (AutoCompleteTextView)findViewById(R.id.txt_stn);
		spn_gateNum = (Spinner)findViewById(R.id.spn_gateNum);
		btn_add = (Button)findViewById(R.id.btn_add);
		btn_search = (Button)findViewById(R.id.btn_search);
		txt_place = (AutoCompleteTextView)findViewById(R.id.txt_place);
		lv = (ListView)findViewById(R.id.list_result);
		createUiAdapter();
		
		// This is important!
		items = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(
				PlaceActivity.this, android.R.layout.simple_list_item_1, items);
		lv.setAdapter(adapter);
		
		// DB
		mDbAdapter = new PlaceDbAdapter(this);
		mDbAdapter.open();
		
		/*
		 * Event Handlers
		 */		
		
	    // Click "Add"
	    btn_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( readCurrentEditText(Util.NORMAL) == Util.NG) {
					return;
				}
				mDbAdapter.createPlace(mStn, mGate, mPlace);
				Toast.makeText(PlaceActivity.this, "추가완료 : " + mStn + " / " + mGate + " / " + mPlace, Toast.LENGTH_SHORT).show();
				txt_place.setText("");
			}
		});

	    
	   // Click "Search"
	    btn_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( readCurrentEditText(Util.ONLY_PLACE) == Util.NG) {
					return;
				}
				
				String db_stn = null;
				String db_gate = null;
				String db_place = null;
				Cursor c = null;

				if (mStn.length() != 0 && mPlace.length() == 0) {
					Log.d(TAG, "fetchPlaceByStn : " + mStn);
					c = mDbAdapter.fetchPlaceByStn(mStn);
				} else if (mStn.length() == 0 && mPlace.length() != 0) {
					Log.d(TAG, "fetchPlaceByPlace : " + mPlace);
					c = mDbAdapter.fetchPlaceByPlace(mPlace);
				} else {
					Log.d(TAG, "fetchPlaceByStnAndPlace : " + mStn + ", " + mPlace);
					c = mDbAdapter.fetchPlaceByStnAndPlace(mStn, mPlace);
				}
				c.moveToFirst();
				while(!c.isAfterLast()){
					StringBuffer sb = new StringBuffer();
					db_stn = c.getString(1);
					db_gate = c.getString(2);
					db_place = c.getString(3);
					
					sb.append(db_stn)
					.append(" / ")
					.append(db_gate)
					.append(" / ")
					.append(db_place);
					Log.i(TAG, "Search result : " + sb.toString());
					items.add(sb.toString());

					c.moveToNext();
				}
				c.close();
				
				if (items.isEmpty()) {
					items.add("No result");
				}
				adapter.notifyDataSetChanged();
			}
	    });
	    
		// Delete an item on the list
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> adapterView, View v, int pos,
					long id) {
				// TODO Auto-generated method stub
				mCurItem = items.get(pos);
				if ("No result".equals(mCurItem)) {
					return;
				}
				mPos = pos;
				showDialog(DLG_DEL_ITEM);
			}
		});
	    
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
		mStn = txt_stn.getText().toString();
		mGate = spn_gateNum.getSelectedItem().toString();
		mPlace = txt_place.getText().toString();
		items.clear();
		adapter.notifyDataSetChanged();
		if (mStn.equals("") || (mPlace.equals("")) && (req == Util.NORMAL)) {
			Toast.makeText(PlaceActivity.this, "역/장소를 입력해주세요", Toast.LENGTH_SHORT).show();
			return Util.NG;
		}
		return Util.OK;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    // Dialog
    @Override
    protected Dialog onCreateDialog(int id) {
    	// TODO Auto-generated method stub
    	AlertDialog dlg = null;

    	switch(id) {
    	case DLG_DEL_ITEM :
    		dlg = new AlertDialog.Builder(this)
    		.setIcon(R.drawable.ic_launcher)
    		.setTitle(mCurItem)
    		.setMessage("정말로 삭제할까요?")
    		.setPositiveButton("삭제", new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				//mWillBeDeleted = true;
    				Log.i(TAG, "Dialog. Clicked 삭제");
    				
    				long  col_id =  -1;
    				Cursor c = null;
    				String[] tokens = {null, null, null};
    				
    				int i = 0;
    				for (String token : mCurItem.split(" / ")) {
    					tokens[i++] = token;
    				}
    				Log.i(TAG, "Extracted Tokens : " + tokens[0] + ", " + tokens[1] + ", " + tokens[2]);
    				c = mDbAdapter.fetchPlaceByStnAndPlace(tokens[0],tokens[2]);	
    				c.moveToFirst();
    				
    				// TODO : No result 등 클릭 시 죽는 예외처리
    				
    				col_id = c.getLong(c.getColumnIndex("_id"));
    				if (col_id >= 0) {
    					Log.i(TAG, "Delete : " + mCurItem + " at colum id : " + col_id);
    					mDbAdapter.deletePlace(col_id);
    					items.remove(mPos);
    					adapter.notifyDataSetChanged();
    				} else {
    					Log.e(TAG, "Delete failed : " + mCurItem + ". colum id is less than zero : " + col_id);
    				}
    				c.close();
    			}
    		})
    		.setNegativeButton("취소", new DialogInterface.OnClickListener() {

    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				Log.i(TAG, "Dialog. Clicked 취소");
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
