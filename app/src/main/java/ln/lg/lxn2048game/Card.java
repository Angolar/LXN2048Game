package ln.lg.lxn2048game;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by LG on 2016/7/2 0002.
 */
public class Card extends FrameLayout {
	public Card(Context context) {
		super(context);
		label=new TextView(getContext());
		label.setTextSize(32);
		label.setGravity(Gravity.CENTER);   //卡片居中显示
		label.setBackgroundColor(0x33ffffff); //设置卡片字体下面的背景色

		LayoutParams lp=new LayoutParams(-1,-1);
		lp.setMargins(10,10,0,0);  //设置卡片间的间隔
		addView(label,lp);
		setNumber(0);
	}
	private TextView label;

	private int number=0;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
		if (number<=0){
			label.setText("");  //如果number<=0,card不显示数字
		}else {
			label.setText(number+"");
		}
	}

	public boolean  equals(Card c){
		return getNumber()==c.getNumber();
	}

}
