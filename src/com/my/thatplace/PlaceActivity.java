package com.my.thatplace;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
	private final String[] STATION;
	private static String mStn, mGate, mPlace;
	private String mCurItem;
	private int mPos;
	private final int DLG_DEL_ITEM = 0;
	
	// DB
	public PlaceDbAdapter mDbAdapter;
	
	// Etc
	InputMethodManager imm;

    public PlaceActivity() {
		super();
		// TODO Auto-generated constructor stub
		STATION = new String[] {"가락시장", "가산디지털단지", "가양", "가좌", "가평", "간석", "갈매", "강남", "강남구청", "강동", "강동구청", "강변", "강촌", "개롱", "개봉", "개포동", "개화", "개화산", "거여", "건대입구", "검암", "경마공원", "경복궁", "경원대", "경찰병원", "계양", "고덕", "고려대", "고속터미널", "고잔", "곡산", "공단", "공덕", "공릉", "공항시장", "과천", "관악", "광나루", "광명", "광명사거리", "광화문", "광흥창", "교대", "구로", "구로디지털단지", "구룡", "구리", "구반포", "구산", "구성", "구의", "구일", "구파발", "국수", "국제화물청사", "국회의사당", "군자", "군포", "굴봉산", "굽은다리", "금곡", "금릉", "금정", "금천구청", "금촌", "금호", "기흥", "길동", "길음", "김유정", "김포공항", "까치산", "낙성대", "남구로", "남부터미널", "남성총신대입구)", "남영", "남춘천", "남태령", "남한산성입구", "내방", "노들", "노량진", "노원", "녹번", "녹사평", "녹양", "녹천", "논현", "능곡", "단대오거리", "답십리", "당고개", "당산", "대곡", "대공원", "대림", "대모산입구", "대방", "대성리", "대야미", "대청", "대치", "대화", "대흥", "덕계", "덕소", "덕정", "도곡", "도농", "도림천", "도봉", "도봉산", "도심", "도원", "도화", "독립문", "독바위", "독산", "돌곶이", "동대문", "동대문역사문화공원", "동대입구", "동두천", "동두천중앙", "동묘앞", "동암", "동인천", "동작", "두정", "둔촌동", "등촌", "뚝섬", "뚝섬유원지", "마곡", "마곡나루", "마두", "마들", "마석", "마장", "마천", "마포", "마포구청", "망우", "망원", "망월사", "매봉", "먹골", "면목", "명동", "명일", "명학", "모란", "목동", "몽촌토성", "무악재", "문래", "문산", "문정", "미금", "미아", "미아삼거리", "반월", "반포", "발산", "방배", "방이", "방학", "방화", "배방", "백마", "백석", "백양리", "백운", "버티고개", "범계", "병점", "보라매", "보문", "보산", "보정", "복정", "봉명", "봉천", "봉화산", "부개", "부천", "부평", "불광", "사가정", "사당", "사릉", "사평", "산본", "산성", "삼각지", "삼성", "삼송", "상계", "상도", "상록수", "상봉", "상수", "상왕십리", "상월곡", "상일동", "상천", "새절", "샛강", "서대문", "서동탄", "서빙고", "서울", "서울대입구", "서정리", "서초", "서현", "석계", "석수", "석촌", "선릉", "선바위", "선유도", "성균관대", "성북", "성산", "성수", "성신여대입구", "성환", "세류", "세마", "소사", "소요산", "송내", "송정", "송탄", "송파", "수내", "수락산", "수리산", "수색", "수서", "수원", "수유", "수진", "숙대입구", "숭실대입구", "시청", "신갈", "신금호", "신길", "신길온천", "신논현", "신답", "신당", "신대방", "신대방삼거리", "신도림", "신림", "신목동", "신반포", "신방화", "신사", "신설동", "신용산", "신원", "신이문", "신정", "신정네거리", "신창", "신천", "신촌", "신풍", "신흥", "쌍문", "쌍용", "아산", "아신", "아차산", "아현", "안국", "안산", "안암", "안양", "암사", "압구정", "애오개", "야탑", "약수", "양수", "양원", "양재", "양재시민의숲", "양정", "양주", "양천구청", "양천향교", "양평", "어린이대공원", "여의나루", "여의도", "역곡", "역삼", "역촌", "연신내", "염창", "영등포", "영등포구청", "영등포시장", "오금", "오류동", "오리", "오목교", "오빈", "오산", "오산대", "오이도", "옥수", "온수", "온양온천", "올림픽공원", "왕십리", "외대앞", "용답", "용두", "용마산", "용문", "용산", "우장산", "운길산", "운서", "운정", "원당", "원덕", "월계", "월곡", "월드컵경기장", "월릉", "을지로3가", "을지로4가", "을지로입구", "응봉", "응암", "의왕", "의정부", "이대", "이매", "이수", "이촌", "이태원", "인덕원", "인천", "인천국제공항", "일산", "일원", "잠실", "잠실나루", "잠원", "장승배기", "장암", "장지", "장한평", "정발산", "정부과천청사", "정왕", "정자", "제기동", "제물포", "종각", "종로3가", "종로5가", "종합운동장", "주안", "주엽", "죽전", "중계", "중곡", "중동", "중랑", "중앙", "중화", "증미", "증산", "지제", "지축", "지행", "직산", "진위", "창동", "창신", "천안", "천왕", "천호", "철산", "청계산입구", "청구", "청담", "청량리지상)", "청량리지하)", "청평", "춘천", "충무로", "충정로", "탄현", "탕정", "태릉입구", "태평", "퇴계원", "파주", "판교", "팔당", "평내호평", "평촌", "평택", "풍기", "풍산", "하계", "학동", "학여울", "한강진", "한남", "한대앞", "한성대입구", "한양대", "한티", "합정", "행당", "행신", "혜화", "홍대입구", "홍제", "화곡", "화랑대", "화서", "화전", "화정", "회기", "회룡", "회현", "효창공원앞", "흑석", "dmc"};
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
				search();
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			}
		});

	    
	   // Click "Search"
	    btn_search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				search();
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
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
		ArrayAdapter<String> adapter_stn = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, STATION);
		ArrayAdapter<CharSequence> adapter_gateNum = ArrayAdapter.createFromResource(this,
				R.array.GateNum, android.R.layout.simple_spinner_item);
//		ArrayAdapter<String> adapter_place = new ArrayAdapter<String>(this,
//				android.R.layout.simple_dropdown_item_1line, mPlaces);
		
		// Specify the layout to use when the list of choices appears
		adapter_gateNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		txt_stn.setAdapter(adapter_stn);
		spn_gateNum.setAdapter(adapter_gateNum);
//		txt_place.setAdapter(adapter_place);
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
    		// 삭제버튼
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
    		// 취소 버튼
    		.setNegativeButton("취소", new DialogInterface.OnClickListener() {
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				Log.i(TAG, "Dialog. Clicked 취소");
    			}
    		})
    		// 카카오 공유
    		.setNeutralButton("공유", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					// Text 전송할 때
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT, "[ThatPlace]");
					intent.putExtra(Intent.EXTRA_TEXT, mCurItem);
					intent.setPackage("com.kakao.talk");
					startActivity(intent);
				}
			})
    		.create();
    		break;

    	default :
    		break;
    	}
    	return dlg;    	
    }
    
    void search() {
    	String db_stn = null;
		String db_gate = null;
		String db_place = null;
		Cursor c = null;

		if ( readCurrentEditText(Util.ONLY_PLACE) == Util.NG) { // input information is not enough then show all places
			Log.d(TAG, "fetchAll");
			c = mDbAdapter.fetchAllPlaces();
		} else {
			if (mStn.length() != 0 && mPlace.length() == 0) { // otherwise, show corresponding places
				Log.d(TAG, "fetchPlaceByStn : " + mStn);
				c = mDbAdapter.fetchPlaceByStn(mStn);
			} else if (mStn.length() == 0 && mPlace.length() != 0) {
				Log.d(TAG, "fetchPlaceByPlace : " + mPlace);
				c = mDbAdapter.fetchPlaceByPlace(mPlace);
			} else {
				Log.d(TAG, "fetchPlaceByStnAndPlace : " + mStn + ", " + mPlace);
				c = mDbAdapter.fetchPlaceByStnAndPlace(mStn, mPlace);
			}
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
    
}
