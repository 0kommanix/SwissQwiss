package ch.goodsolutions.swissqwiss.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ch.goodsolutions.swissqwiss.R;

public class FinishedActivity extends Activity implements OnClickListener {

	public static final String INTENT_KEY_SCORE = "score";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finished);
		findViewById(R.id.buttonFinished).setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		String score = getIntent().getStringExtra(INTENT_KEY_SCORE);
		((TextView) findViewById(R.id.textFinishedScore)).setText(score);
	}

	public void onClick(View v) {
		Intent intent = new Intent(this, SwissQwissActivity.class);
		startActivity(intent);
	}
}