package ln.lg.lxn2048game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2016/7/1 0001.
 */
public class GameView extends GridLayout {

	public GameView(Context context) {
		super(context);
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initGameView();
	}

	public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initGameView();
	}

	private void initGameView(){

		setColumnCount(4);  // 指定4列

		setBackgroundColor(0xffbbada0);     //设置游戏区的背景色

		setOnTouchListener(new OnTouchListener() {
			private float startX,startY,offsetX,offsetY;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()){
					case MotionEvent.ACTION_DOWN:
						startX=event.getX();
						startY=event.getY();
						break;
					case MotionEvent.ACTION_UP:
						offsetX=event.getX()-startX;
						offsetY=event.getY()-startY;

						if (Math.abs(offsetX)>Math.abs(offsetY)){
							if (offsetX<-5){
								swipeLeft();
							}else if(offsetX>5){
								swipeRight();
							}
						}else {
							if (offsetY<-5){
								swipeUp();
							}else if(offsetY>5){
								swipeDown();
							}
						}
						break;
				}
				return true;
			}
		});

	}

	//在AndroidManifest.xml文件<activity 中设置android:screenOrientation="portrait"，使软件只竖屏显示
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//获取手机屏幕的长宽，由于游戏区域为4×4的方形，边上流出10dp的空区，card宽高须除以4
		int cardWidth=(Math.min(w,h)-10)/4;
		addCards(cardWidth,cardWidth);
		startGame();
	}

	private void startGame() {
		//每次开始游戏时，分数应该清零
		MainActivity.getMainActivity().clearScore();
		for (int y=0;y<4;y++){
			for (int x=0;x<4;x++){
				cardsMap[x][y].setNumber(0);
			}
		}
		//每次初始添加进来的数字是两个，所以添加两次
		addRandomNumber();
		addRandomNumber();
	}

	/**
	 * 游戏区添加卡片
	 * @param cardWidth  卡片的宽度
	 * @param cardHeight  卡片的高度
	 */
	private void addCards(int cardWidth, int cardHeight) {
		Card c;
		for (int y=0;y<4;y++){
			for (int x=0;x<4;x++){
				c=new Card(getContext());
				c.setNumber(0);
				addView(c,cardWidth,cardHeight);
				cardsMap[x][y]=c;
			}
		}
	}

	/**
	 * 以一定的概率随机产生2,4
	 */
	private void addRandomNumber(){
		emptyPoints.clear();
		for (int y=0;y<4;y++){
			for (int x=0;x<4;x++){
				if (cardsMap[x][y].getNumber()<=0){
					emptyPoints.add(new Point(x,y));
				}
			}
		}
		Point p=emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
		cardsMap[p.x][p.y].setNumber(Math.random()>0.1?2:4);    //随机产生2和4的概率为9:1，因为Math.random()产生0-1之间的随机数
	}

	/**
	 * 向左滑动
	 */
	private void swipeLeft() {

		//判断是否需要添加随机数，false：不添加，ture：添加
		boolean merge=false;

		for (int y=0;y<4;y++){
			for (int x=0;x<4;x++){
				for (int x1=x+1;x1<4;x1++){
					if (cardsMap[x1][y].getNumber()>0){
						if (cardsMap[x][y].getNumber()<=0){
							cardsMap[x][y].setNumber(cardsMap[x1][y].getNumber());
							cardsMap[x1][y].setNumber(0);
							//当前内容为空时，处于到底3格，和第四格合并后，若第二格为空，第一格不为空
							//此时，原本第四格的内容合并到第二格后，若第一格和第二格数字相同，
							// 第一格和第二格的内容就不能进行合并，所以此处添加x--后，就能再次进行循环
							x--;
							merge=true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])){
							cardsMap[x][y].setNumber(cardsMap[x][y].getNumber()*2);
							cardsMap[x1][y].setNumber(0);
							//获取mainActivity，实现向左滑动时，相同数字叠加后，分数递增
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNumber());
							merge=true;
						}
						break;
					}
				}
			}
		}
		//判断merge，当前游戏区域是否需要产生新的随机数
		//判断游戏是否结束
		if (merge){
			addRandomNumber();
			checkComplete();
		}
	}

	/**
	 * 向右滑动
	 */
	private void swipeRight() {

		//判断是否需要添加随机数，false：不添加，ture：添加
		boolean merge=false;

		for (int y=0;y<4;y++){
			for (int x=3;x>=0;x--){
				for (int x1=x-1;x1>=0;x1--){
					if (cardsMap[x1][y].getNumber()>0){
						if (cardsMap[x][y].getNumber()<=0){
							cardsMap[x][y].setNumber(cardsMap[x1][y].getNumber());
							cardsMap[x1][y].setNumber(0);
							x++;
							merge=true;
						}else if (cardsMap[x][y].equals(cardsMap[x1][y])){
							cardsMap[x][y].setNumber(cardsMap[x][y].getNumber()*2);
							cardsMap[x1][y].setNumber(0);
							//获取mainActivity，实现向右滑动时，相同数字叠加后，分数递增
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNumber());
							merge=true;
						}
						break;
					}
				}
			}
		}
		//判断merge，当前游戏区域是否需要产生新的随机数
		//判断游戏是否结束
		if (merge){
			addRandomNumber();
			checkComplete();
		}
	}

	/**
	 * 向上滑动
	 */
	private void swipeUp() {

		//判断是否需要添加随机数，false：不添加，ture：添加
		boolean merge=false;

		for (int x=0;x<4;x++){
			for (int y=0;y<4;y++){
				for (int y1=y+1;y1<4;y1++){
					if (cardsMap[x][y1].getNumber()>0){
						if (cardsMap[x][y].getNumber()<=0){
							cardsMap[x][y].setNumber(cardsMap[x][y1].getNumber());
							cardsMap[x][y1].setNumber(0);
							y--;
							merge=true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])){
							cardsMap[x][y].setNumber(cardsMap[x][y].getNumber()*2);
							cardsMap[x][y1].setNumber(0);
							//获取mainActivity，实现向上滑动时，相同数字叠加后，分数递增
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNumber());
							merge=true;
						}
						break;
					}
				}
			}
		}
		//判断merge，当前游戏区域是否需要产生新的随机数
		//判断游戏是否结束
		if (merge){
			addRandomNumber();
			checkComplete();
		}
	}

	/**
	 * 向下滑动
	 */
	private void swipeDown() {

		//判断是否需要添加随机数，false：不添加，ture：添加
		boolean merge=false;

		for (int x=0;x<4;x++){
			for (int y=3;y>=0;y--){
				for (int y1=y-1;y1>=0;y1--){
					if (cardsMap[x][y1].getNumber()>0){
						if (cardsMap[x][y].getNumber()<=0){
							cardsMap[x][y].setNumber(cardsMap[x][y1].getNumber());
							cardsMap[x][y1].setNumber(0);
							y++;
							merge=true;
						}else if (cardsMap[x][y].equals(cardsMap[x][y1])){
							cardsMap[x][y].setNumber(cardsMap[x][y].getNumber()*2);
							cardsMap[x][y1].setNumber(0);
							//获取mainActivity，实现向下 滑动时，相同数字叠加后，分数递增
							MainActivity.getMainActivity().addScore(cardsMap[x][y].getNumber());
							merge=true;
						}
						break;
					}
				}
			}
		}
		//判断merge，当前游戏区域是否需要产生新的随机数
		if (merge){
			addRandomNumber();
			checkComplete();
		}
	}


	/**
	 * 判断游戏结束
	 * complete=true:游戏结束
	 * complete=false：游戏继续
	 */
	private void checkComplete(){

		boolean complete=true;
ALL:
		for (int x=0;x<4;x++){
			for (int y=0;y<4;y++){
				/**
				 * 游戏还能继续的条件：
				 * 1.游戏区还有空位置
				 * 2.游戏区中卡片的相邻位置（上、下、左、右）还有可以合并的卡片
				 */
				if(cardsMap[x][y].getNumber()==0||
						(x>0&&cardsMap[x][y].equals(cardsMap[x-1][y]))||
						(x<3&&cardsMap[x][y].equals(cardsMap[x+1][y]))||
						(y>0&&cardsMap[x][y].equals(cardsMap[x][y-1]))||
						(y<3&&cardsMap[x][y].equals(cardsMap[x][y+1]))){
					complete=false;
					break ALL;
				}
			}
		}
		if (complete){
			new AlertDialog.Builder(getContext()).setTitle("您好！").setMessage("游戏结束！").setPositiveButton("重新开始", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					startGame();
				}
			}).show();
		}
	}

	private Card[][] cardsMap=new Card[4][4];  //记录每个卡片的为二维数组
	private List<Point> emptyPoints=new ArrayList<Point>();

}
