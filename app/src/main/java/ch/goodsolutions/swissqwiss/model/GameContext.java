package ch.goodsolutions.swissqwiss.model;

import android.app.Activity;
import android.widget.ProgressBar;
import android.widget.TextView;
import ch.goodsolutions.swissqwiss.R;

public class GameContext {

	private final int questionsCount;
	private int questionsProgressCount = 0;
	private int correctAnswersCount = 0;
	private int wrongAnswersCount = 0;

	public GameContext(int questionsCount) {
		this.questionsCount = questionsCount;
	}

	public void updateStats(Activity activity) {
		((TextView) activity.findViewById(R.id.textCountCorrect)).setText("" + correctAnswersCount);
		((TextView) activity.findViewById(R.id.textCountWrong)).setText("" + wrongAnswersCount);
		((TextView) activity.findViewById(R.id.textProgress)).setText(getFormattedProgress());
		((ProgressBar) activity.findViewById(R.id.progressBar)).setProgress(100 * questionsProgressCount / questionsCount);
	}

	public boolean gameFinished() {
		return (questionsProgressCount == questionsCount);
	}

	public int getQuestionsCount() {
		return questionsCount;
	}

	public int getQuestionsProgressCount() {
		return questionsProgressCount;
	}

	public void incrementQuestionsProgressCount() {
		questionsProgressCount++;

	}

	public int getCorrectAnswersCount() {
		return correctAnswersCount;
	}

	public void incrementCorrectAnswersCount() {
		correctAnswersCount++;
	}

	public int getWrongAnswersCount() {
		return wrongAnswersCount;
	}

	public void incrementWrongAnswersCount() {
		wrongAnswersCount++;
	}

	public String getFormattedProgress() {
		return "(" + questionsProgressCount + "/" + questionsCount + ")";
	}

	@Override
	public String toString() {
		return "GameContext [questionsCount=" + questionsCount + ", questionsProgressCount=" + questionsProgressCount
				+ ", correctAnswersCount=" + correctAnswersCount + ", incorrectAnswersCount=" + wrongAnswersCount + "]";
	}

}
