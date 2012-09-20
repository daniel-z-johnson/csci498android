package csci498.danjohns.LunchList;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RestaurantHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME = "linchlist.db";
	private static final int SCHEMA_VERSION = 1;
	
	public RestaurantHelper(Context context){
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE restaurants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//no-op, since will not be called until 2nd schema
		//version exits
	}
	
	public void insert(String name, String address, String type, String notes) {
		ContentValues cv = new ContentValues();
		
		cv.put("name", name);
		cv.put("adress", address);
		cv.put("type", type);
		cv.put("notes", notes);
		
		getWritableDatabase().insert("restaurants", "name", cv);
	}

}
