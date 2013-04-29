package com.my.thatplace;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class PlaceController {
	private final String TAG = "ThatPlace_Controller";
	private String mStn;
	private String mGate;
	private String mPlace;
	public PlaceDbAdapter mDbAdapter;
    private final Context mCtx;
	
	public PlaceController(Context ctx) {
		super();
		// TODO Auto-generated constructor stub
		mCtx = ctx;
		mDbAdapter = new PlaceDbAdapter(mCtx);
		mDbAdapter.open();
	}
	public String getStn() {
		return mStn;
	}
	public void setStn(String stn) {
		this.mStn = stn;
	}
	public String getGate() {
		return mGate;
	}
	public void setGate(String gate) {
		this.mGate = gate;
	}
	public String getPlace() {
		return mPlace;
	}
	public void setPlace(String place) {
		this.mPlace = place;
	}
	
	public void addPlace(String stn, String gate, String place) {
		mDbAdapter.createPlace(stn, gate, place);
	}
	
	public void deletePlace(int id) {
		mDbAdapter.deletePlace(id);
	}
	
	public ArrayList<String> searchPlace(String input_stn, String input_place) {
		String db_stn = null;
		String db_gate = null;
		String db_place = null;
		ArrayList<String> ret = new ArrayList<String>();
		Cursor c = null;

		// Note : PlaceActivity:readCurrentEditText() 에서 input_stn과 input_place 의 validation check을 하므로
		// 			 현 시점에서 두 String이 모두 null인 case는 존재하지 않음.
		if (input_stn.length() != 0 && input_place.length() == 0) {
			Log.i(TAG,"/" +  input_stn + "/");
			c = mDbAdapter.fetchPlaceByStn(input_stn);
//			c = mDbAdapter.fetchAllPlaces();
		} else if (input_stn.length() == 0 && input_place.length() != 0) {
			c = mDbAdapter.fetchPlaceByPlace(input_place);
		} else {
			c = mDbAdapter.fetchPlaceByStnAndPlace(input_stn, input_place);
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
			Log.i(TAG, sb.toString());
			ret.add(sb.toString());

			c.moveToNext();
		}
		c.close();
		
		if (ret.isEmpty()) {
			ret.add("No result");
		}
		return ret;
	}
}
