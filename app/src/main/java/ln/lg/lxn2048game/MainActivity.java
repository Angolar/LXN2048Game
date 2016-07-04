package ln.lg.lxn2048game;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

	private TextView tvScore;
	private GameView gameView;
	private int score=0;
	private static MainActivity mainActivity=null;

	public MainActivity(){
		mainActivity=this;

	}

	public static MainActivity getMainActivity() {
		return mainActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tvScore = (TextView) findViewById(R.id.tv_score);
		gameView = (GameView) findViewById(R.id.gameView);

	}

	/**
	 * 清空分数
	 */
	public void clearScore(){
		score=0;
		showScore();
	}

	/**
	 * 显示分数
	 */
	public void showScore(){
		tvScore.setText(score+"");
	}

	/**
	 * 分数递增
	 */
	public void addScore(int s){
		score+=s;
		showScore();
	}

}
