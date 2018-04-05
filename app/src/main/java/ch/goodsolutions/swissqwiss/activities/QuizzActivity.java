package ch.goodsolutions.swissqwiss.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ch.goodsolutions.swissqwiss.R;
import ch.goodsolutions.swissqwiss.model.DBHelper;
import ch.goodsolutions.swissqwiss.model.GameContext;
import ch.goodsolutions.swissqwiss.model.Location;
import ch.goodsolutions.swissqwiss.model.MapTile;
import ch.goodsolutions.swissqwiss.model.Marker;

public class QuizzActivity extends Activity implements OnClickListener {

	private static final int QUESTIONS_COUNT_DEFAULT = 10;
	private static final long ANSWER_SYMBOL_DELAY_MILLIS = 2 * 1000;

	private GameContext gameContext;
	private DBHelper dbHelper;
	private MapTile mapTile;
	private Marker marker;
	private boolean prefsDisplaySolutions;
	private String prefsMapTileInUse;
	private final Random random = new Random();
	private final int locationButtonIds[] = { R.id.buttonAnswerA, R.id.buttonAnswerB, R.id.buttonAnswerC,
			R.id.buttonAnswerD };
	private final List<Location> locations = new ArrayList<Location>(locationButtonIds.length);
	private int selectedLocationPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		findViewById(R.id.buttonAnswerA).setOnClickListener(this);
		findViewById(R.id.buttonAnswerB).setOnClickListener(this);
		findViewById(R.id.buttonAnswerC).setOnClickListener(this);
		findViewById(R.id.buttonAnswerD).setOnClickListener(this);
		findViewById(R.id.imageCorrect).setVisibility(View.INVISIBLE);
		findViewById(R.id.imageWrong).setVisibility(View.INVISIBLE);
	}

	@Override
	public void onStart() {
		super.onStart();
		getPreferences();
		findViewById(R.id.layoutSolution).setVisibility(View.INVISIBLE);
		gameContext = new GameContext(QUESTIONS_COUNT_DEFAULT);
		mapTile = getMapTile();
		marker = getMarker();
		dbHelper = getDBHelper();
		nextQuestion();
	}

	public void onClick(View v) {
		for (int i = 0; i < locationButtonIds.length; i++) {
			if (v.getId() == locationButtonIds[i]) {
				evaluateAnswer(v.getId());
				nextQuestion();
				return;
			}
		}
	}

	private void getPreferences() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefsDisplaySolutions = sharedPrefs.getBoolean("solutions", false);
		prefsMapTileInUse = sharedPrefs.getString("mapTile", "map_ch_lakes");
	}

	private void evaluateAnswer(int selectedButtonId) {
		// check answer
		boolean correctAnswer = (selectedButtonId == locationButtonIds[selectedLocationPosition]);
		if (correctAnswer) {
			gameContext.incrementCorrectAnswersCount();
		} else {
			gameContext.incrementWrongAnswersCount();
		}

		// display feedback
		findViewById(R.id.imageCorrect).setVisibility(correctAnswer ? View.VISIBLE : View.INVISIBLE);
		findViewById(R.id.imageWrong).setVisibility(correctAnswer ? View.INVISIBLE : View.VISIBLE);
		// display solution if required
		if (prefsDisplaySolutions) {
			TextView solutionText = (TextView) findViewById(R.id.textSolution);
			solutionText.setText(getSelectedLocation().getDisplayName());
			findViewById(R.id.layoutSolution).setVisibility(View.VISIBLE);
		}
		// set delayed remove of feedback
		findViewById(R.id.QuizLayout).postDelayed(new Runnable() {

			public void run() {
				findViewById(R.id.imageCorrect).setVisibility(View.INVISIBLE);
				findViewById(R.id.imageWrong).setVisibility(View.INVISIBLE);
				if (prefsDisplaySolutions) {
					findViewById(R.id.layoutSolution).setVisibility(View.INVISIBLE);
				}
			}
		}, ANSWER_SYMBOL_DELAY_MILLIS);
	}

	private void nextQuestion() {
		if (gameContext.gameFinished()) {
			Intent intent = new Intent(this, FinishedActivity.class);
			intent.putExtra(FinishedActivity.INTENT_KEY_SCORE,
					gameContext.getCorrectAnswersCount() + "/" + gameContext.getQuestionsCount());
			startActivity(intent);
			return;
		}
		initializeLocations(dbHelper);
		setAnswerButtons();
		positionLocation(getSelectedLocation(), mapTile, marker);
		gameContext.incrementQuestionsProgressCount();
		gameContext.updateStats(this);
	}

	private DBHelper getDBHelper() {
		DBHelper dbHelper = new DBHelper(getBaseContext());
		try {
			dbHelper.createDataBase();
		} catch (IOException e) {
			displayToast(R.string.error_dbaccess);
			System.err.println(e.getMessage());
		}
		try {
			dbHelper.openDataBase();
		} catch (SQLException sqle) {
			displayToast(R.string.error_dbaccess);
			System.err.println(sqle.getMessage());
		}
		return dbHelper;
	}

	private void displayToast(int resourceId) {
		Toast.makeText(getBaseContext(), resourceId, Toast.LENGTH_SHORT).show();
	}

	private Location getSelectedLocation() {
		return locations.get(selectedLocationPosition);
	}

	private MapTile getMapTile() {
		MapTile mapTile = new MapTile(prefsMapTileInUse);
		mapTile.setMapCoordinateNorth(47.808264);
		mapTile.setMapCoordinateSouth(45.818031);
		mapTile.setMapCoordinateWest(5.956303);
		mapTile.setMapCoordinateEast(10.491944);
		mapTile.setImagePixelWidth(1200);
		mapTile.setImagePixelHeight(814);
		mapTile.setMapOffsetPixelTop(5);
		mapTile.setMapOffsetPixelBottom(2);
		mapTile.setMapOffsetPixelLeft(1);
		mapTile.setMapOffsetPixelRight(3);
		try {
			mapTile.compile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (prefsMapTileInUse.equals("map_ch_empty")) {
			((ImageView) findViewById(R.id.imageMap)).setImageResource(R.drawable.map_ch_empty);
		} else if (prefsMapTileInUse.equals("map_ch_lakes")) {
			((ImageView) findViewById(R.id.imageMap)).setImageResource(R.drawable.map_ch_lakes);
		} else if (prefsMapTileInUse.equals("map_ch_full")) {
			((ImageView) findViewById(R.id.imageMap)).setImageResource(R.drawable.map_ch_full);
		}
		// get rendered size
		Display display = getWindowManager().getDefaultDisplay();
		View rootLayout = findViewById(R.id.QuizLayout); // Screen is a container layout
		rootLayout.measure(display.getWidth(), display.getHeight());
		View mapView = findViewById(R.id.imageMap);
		mapTile.updateRenderedSize(mapView.getMeasuredWidth(), mapView.getMeasuredHeight());
		return mapTile;
	}

	private Marker getMarker() {
		return new Marker("marker", 21, 32, 10, 29);
	}

	private void positionLocation(Location location, MapTile mapTile, Marker marker) {
		mapTile.calculateMarkerOffsets(location, marker);
		ImageView markerImageView = (ImageView) findViewById(R.id.imageMapMarker);
		markerImageView.setPadding(mapTile.getMarkerOffsetPixelLeft(), mapTile.getMarkerOffsetPixelTop(), 0, 0);
		markerImageView.invalidate();
	}

	// locations.add(new Location(1, "Zürich", 47.379022, 8.541001));
	// locations.add(new Location(4, "Genf", 46.200013, 6.149985));
	// locations.add(new Location(2, "Bern", 46.947060123174, 7.4441408200731));
	// locations.add(new Location(3, "Luzern", 47.05, 8.3));
	// locations.add(new Location(2, "Chur", 46.84986, 9.53287));
	private void initializeLocations(DBHelper dbHelper) {
		locations.clear();
		for (int i = 0; i < locationButtonIds.length; i++) {
			locations.add(dbHelper.getRandomLocation());
		}
		selectedLocationPosition = random.nextInt(locationButtonIds.length - 1);
	}

	private void setAnswerButtons() {
		for (int i = 0; i < locationButtonIds.length; i++) {
			Button button = (Button) findViewById(locationButtonIds[i]);
			Location location = locations.get(i);
			button.setText(location.getDisplayName());
		}
	}

}
