package ch.goodsolutions.swissqwiss.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import ch.goodsolutions.swissqwiss.R;

public class SwissQwissActivity extends Activity implements OnClickListener {

	private static final int DIALOG_ABOUT = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViewById(R.id.startButton).setOnClickListener(this);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.startButton) {
			Intent intent = new Intent(this, QuizzActivity.class);
			startActivity(intent);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_ABOUT:
				LayoutInflater factory = LayoutInflater.from(this);
				final View aboutView = factory.inflate(R.layout.about, null);
				return new AlertDialog.Builder(SwissQwissActivity.this).setIcon(R.drawable.ic_launcher)
						.setTitle(R.string.about_title).setView(aboutView)
						.setPositiveButton(R.string.about_button_dismiss, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}
						}).create();
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public void onSettingsClick(MenuItem settingsMenu) {
		Intent settingsActivity = new Intent(this, PrefsActivity.class);
		startActivity(settingsActivity);
	}

	public void onAboutClick(MenuItem aboutMenu) {
		showDialog(DIALOG_ABOUT);
	}
}