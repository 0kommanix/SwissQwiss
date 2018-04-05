package ch.goodsolutions.swissqwiss.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static final String DB_PATH = "/data/data/ch.goodsolutions.swissqwiss/databases/";
	private static final String DB_NAME = "SwissQwissDB";
	private static final String LOCATION_TABLE = "locations";
	private static final String LOCATION_TABLE_ID = "_id";
	private static final String LOCATION_TABLE_NAME = "name";
	private static final String LOCATION_TABLE_LAT = "lat";
	private static final String LOCATION_TABLE_LONG = "long";
	private static final String LOCATION_TABLE_FEATURECLASS = "featureclass";
	private static final String LOCATION_TABLE_COUNTRY = "country";
	private static final String LOCATION_TABLE_POPULATION = "population";
	private static final String LOCATION_TABLE_LASTMODIFIED = "last_modified";
	private static final String[] LOCATON_TABLE_ATTRIBUTES = { LOCATION_TABLE_ID, LOCATION_TABLE_NAME,
			LOCATION_TABLE_LAT, LOCATION_TABLE_LONG, LOCATION_TABLE_FEATURECLASS, LOCATION_TABLE_COUNTRY,
			LOCATION_TABLE_POPULATION, LOCATION_TABLE_LASTMODIFIED };

	private final Context context;
	private final Random random;
	private SQLiteDatabase database;
	private int rowCount = -1;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.context = context;
		this.random = new Random();
	}

	public Location getRandomLocation() {
		int randomRowID = random.nextInt(getRowCount());
		return getLocationByRowID(randomRowID);
	}

	public int getRowCount() {
		if (rowCount != -1) {
			return rowCount;
		}
		final Cursor cursor = database.query(LOCATION_TABLE, new String[] { "_id" }, null, null, null, null, null);
		rowCount = cursor.getCount();
		cursor.close();
		return rowCount;
	}

	public Location getLocationByRowID(int rowId) {
		Cursor cursor = null;
		try {
			cursor = database.rawQuery("select * from " + LOCATION_TABLE + " limit 1 offset " + rowId, null);
			if (cursor.getCount() < 1) {
				return null;
			}
			cursor.moveToFirst();
			int id = cursor.getInt(0);
			String displayName = cursor.getString(1);
			double latitude = cursor.getDouble(2);
			double longitude = cursor.getDouble(3);
			return new Location(id, displayName, latitude, longitude);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into the
			// default system path
			// of your application so we are gonna be able to overwrite that database
			// with our database.
			getReadableDatabase();

			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database.");
			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each time
	 * you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		// Open the database
		String myPath = DB_PATH + DB_NAME;
		database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	}

	@Override
	public synchronized void close() {
		if (database != null)
			database.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}