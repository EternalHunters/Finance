package com.lnl.finance.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.lnl.finance.util.AppUtil;

public class XYChartView extends View{
	
	private int mWidth;					//X轴长度
	private int mHeight;				//Y轴长度
	
	private int countX = 12;			//X轴坐标数
	private int countY = 8;				//Y轴坐标数
	
	private String unitX = "日";			//X轴单位
	private String unitY = "kWh";		//Y轴单位
	
	private String[] coordinateX;		//X轴坐标
	
	private float maxDataY = 1;				//Y轴坐标最大值
	
	private int spaceLeft = 60;			//与左边缘的距离
	private int spaceBottom = 60;		//与下边缘的距离
	private int spaceTop = 60;			//与上边缘的距离
	private int spaceRight = 60;		//与右边缘的距离
	
	private int paintColor = 0;			//画笔颜色
	private int countLine = 0;			//计算线数量
	
	private ArrayList<XYChartData> lineList; 	//线列表
	
	private Activity activity;
	
	private boolean hasCursor = false;
	private CursorChangeListener cursorChangeListener;
	private float fingerPosition;
	private float currentPosition;
	private int lastRefreshPosition;
	private Paint cursorPaint;
	
	
	/*
     * 自定义控件一般写两个构造方法 CoordinatesView(Context context)用于java硬编码创建控件
     * 如果想要让自己的控件能够通过xml来产生就必须有第2个构造方法 CoordinatesView(Context context,
     * AttributeSet attrs) 因为框架会自动调用具有AttributeSet参数的这个构造方法来创建继承自View的控件
     */
	public XYChartView(Context context,Activity activity) {
		super(context, null);
		lineList = new ArrayList<XYChartData>();
		cursorPaint= new Paint();
		cursorPaint.setStrokeWidth(2);
		cursorPaint.setColor(Color.rgb(255, 255, 0));
		cursorPaint.setAlpha(255);
		this.activity = activity;
    }

	public XYChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		cursorPaint= new Paint();
		cursorPaint.setStrokeWidth(2);
		cursorPaint.setColor(Color.rgb(255, 255, 0));
		cursorPaint.setAlpha(255);
		lineList = new ArrayList<XYChartData>();
    }
	
	//获取X轴的坐标数
	public int getCountX() {
		return countX;
	}

	//设置X轴的坐标数
	public void setCountX(int countX) {
		this.countX = countX;
	}

	//获取Y轴的坐标数
	public int getCountY() {
		return countY;
	}

	//设置Y轴的坐标数
	public void setCountY(int countY) {
		this.countY = countY;
	}

	//返回X坐标轴单位
	public String getUnitX() {
		return unitX;
	}

	//设置X坐标轴单位
	public void setUnitX(String unitX) {
		this.unitX = unitX;
	}
	
	//返回X坐标轴单位
	public String getUnitY() {
		return unitY;
	}

	//设置X坐标轴单位
	public void setUnitY(String unitY) {
		this.unitY = unitY;
	}

	
	//设置Y值最大值
	public void findMaxDataY() {
		
		for(int j = 0; j <lineList.size(); j++){
			for(int i = 0; i < lineList.get(j).getCoordinateY().length; i++){
				if(maxDataY < lineList.get(j).getCoordinateY()[i])
					maxDataY = lineList.get(j).getCoordinateY()[i];
				
			}	
		}
		while(((int)maxDataY) % (countY*10) != 0){
//			System.out.println("maxDataY  "+maxDataY);
			maxDataY ++;
		}

	}
	
	public int getPaintColor() {
		return paintColor;
	}

	public void setPaintColor(int paintColor) {
		this.paintColor = paintColor;
	}
	
	/**
	 * @return the hasCursor
	 */
	public boolean isHasCursor() {
		return hasCursor;
	}

	/**
	 * @param hasCursor the hasCursor to set
	 */
	public void setHasCursor(boolean hasCursor) {
		this.hasCursor = hasCursor;
	}
	
	/**
	 * @return the fingerPosition
	 */
	public float getFingerPosition() {
		return fingerPosition;
	}

	/**
	 * @param fingerPosition the fingerPosition to set
	 */
	public void setFingerPosition(float fingerPosition) {
		this.fingerPosition = fingerPosition;
	}

	/**
	 * @return the cursorChangeListener
	 */
	public CursorChangeListener getCursorChangeListener() {
		return cursorChangeListener;
	}

	/**
	 * @param cursorChangeListener the cursorChangeListener to set
	 */
	public void setCursorChangeListener(CursorChangeListener cursorChangeListener) {
		this.cursorChangeListener = cursorChangeListener;
	}

	private boolean haveData(){
		if(lineList.size()==0){
			return false;
		}
		return true;
	}
	
	/*
     * 控件创建完成之后，在显示之前都会调用这个方法，此时可以获取控件的大小 并得到原点坐标的点。
     */
    @Override
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    	mWidth = width - spaceLeft - spaceRight;					//宽度
    	mHeight = height - spaceTop - spaceBottom;				//高度
        System.out.println(mWidth+" drawBase");
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }
	
	/*
     * 自定义控件一般都会重载onDraw(Canvas canvas)方法，来绘制自己想要的图形
	*/
    @Override
    public void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	
    	if(!haveData()){
    		return;
    	}
        
        // 画坐标轴
        if (canvas != null) {

            // 画坐标轴刻度线
        	if(countX==1||countY==0){
        		return;
        	}
        	int pieceX = countX==1?mWidth-AppUtil.dip2px(activity, 10):(mWidth-AppUtil.dip2px(activity, 10)) / (countX-1), pieceY = mHeight / countY;
            System.out.println(""+pieceX);
            drawBase(canvas, pieceX, pieceY);
            for(int i = 0; i < countLine; i++){
            	drawLine(canvas, pieceX, lineList.get(i));
            }
            
            //画游标
            if(hasCursor){                                                                                                                                                                           
            	//计算最靠近哪一个点
            	canvas.drawLine(fingerPosition, spaceTop, fingerPosition,mHeight + spaceTop , cursorPaint);
            }
        }
        
    }
    
    /**
     * 
     * @Title: drawLine 
     * @Description: TODO(画线) 
     * @param @param canvas
     * @param @param pieceX
     * @param @param line    设定文件 
     * @return void    返回类型 
     * @throws
     */

    private Point[] points;
    private void drawLine(Canvas canvas, int pieceX, XYChartData line){
    	

        float[] y = line.getCoordinateY();   
    	
    	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    	paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(AppUtil.dip2px(activity, 3));
        paint.setColor(line.getPaintColor()); 
        
        Paint paint1 = new Paint();
        paint1.setTextSize(AppUtil.sp2px(activity, 15));
        paint1.setColor(line.getPaintColor()); 
        
        points = new Point[countX];
        
    	for(int i = 0; i < countX; i++){  
    		if(cursorChangeListener==null){
    			
    			System.out.println("point :"+(spaceLeft + pieceX * (i))+"  "+(mHeight + spaceTop - mHeight * y[i] / maxDataY));
    			canvas.drawCircle(spaceLeft + pieceX * (i), mHeight + spaceTop - mHeight * y[i] / maxDataY, AppUtil.dip2px(activity, 5), paint1);

    			paint1.setTextSize(AppUtil.sp2px(activity, 15-(int)(0.5*(y[i]+"").length())));
    			paint1.setTextAlign(Paint.Align.CENTER);
    			canvas.drawText(y[i]+"", spaceLeft + pieceX *(i), mHeight + spaceTop - mHeight * y[i] / maxDataY - AppUtil.dip2px(activity, 8), paint1);
				
    		}else{
    			//当前游标的那一点画圈
            	if(i==lastRefreshPosition){
            		System.out.println("point :"+(spaceLeft + pieceX * (i))+"     "+ (mHeight + spaceTop - mHeight * y[i] / maxDataY));
            		canvas.drawCircle(spaceLeft + pieceX * (i), mHeight + spaceTop - mHeight * y[i] / maxDataY, AppUtil.dip2px(activity, 10), paint1);
            	}
    		}
    		points[i] = new Point(spaceLeft + pieceX * (i), (int)(mHeight + spaceTop - mHeight * y[i] / maxDataY));
         }
    	
    	//画曲线
		Path p = new Path();
    	Point mid = new Point();
    	for (int i = 1; i < points.length; i++) {
    		Point start = points[i-1];
    		Point end = points[i];
    		
    		mid.set((start.x + end.x) / 2, (start.y + end.y) / 2);

        	// Draw line connecting the two points:
        	p.moveTo(start.x, start.y);
        	p.quadTo((start.x + mid.x) / 2, start.y, mid.x, mid.y);
        	p.quadTo((mid.x + end.x) / 2, end.y, end.x, end.y);
		}
    	canvas.drawPath(p, paint);
    }
    
    /**
     * 
     * @Title: drawBase 
     * @Description: TODO(画基本坐标轴) 
     * @param @param canvas
     * @param @param pieceX	
     * @param @param pieceY    
     * @return void    返回类型 
     * @throws
     */
    private void drawBase(Canvas canvas, int pieceX, int pieceY){
    	Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setColor(Color.rgb(209, 74, 74));
        paint.setTextSize(AppUtil.sp2px(activity,15));
        paint.setAlpha(255);
        
        //画X轴刻度
        for(int i = 0; i < countX; i++){
        	canvas.drawLine(spaceLeft + pieceX * i, mHeight +spaceTop - 5, spaceLeft + pieceX * i, mHeight +spaceTop + 5 , paint);	
        	paint.setTextAlign(Paint.Align.CENTER);
        	canvas.drawText(coordinateX[i], spaceLeft + pieceX *(i), mHeight + spaceTop +AppUtil.dip2px(activity, 20), paint); 
        }
        
        //画Y轴刻度
        for(int j = 0; j < countY; j++){
//        	canvas.drawLine(spaceLeft - 5, mHeight + spaceTop - pieceY * j,  spaceLeft + 5, mHeight + spaceTop - pieceY * j , paint);
        	
//        	paint.setTextAlign(Paint.Align.CENTER);
//        	canvas.drawText(maxDataY * j / countY + "", spaceLeft - 18 , mHeight + spaceTop - pieceY * j + 5, paint);
        }
    }
    
    /**
     * 画三角形 用于画坐标轴的箭头
     */
    private void drawTriangle(Canvas canvas, Point p1, Point p2, Point p3) {
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.close();

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
        // 绘制这个多边形
        canvas.drawPath(path, paint);
    }

	/**
	 * 
	 * @Title: addLine 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param coordinateX
	 * @param @param coordinateY
	 * @param @param dataY
	 * @param @param color    设定文件 
	 * @return void    返回类型 
	 * @throws
	 */
	public void addLine(String lineName, String[] coordinateX, float[] dataY){
		int paintColor = Color.rgb(255, 255, 255);
		countX = coordinateX.length;
		
		switch(countLine % 2){
			case 0:
				paintColor = Color.rgb(209, 74, 74);
				break;
			case 1:
				paintColor = Color.rgb(255, 246, 0);
				break;
			default:
				break;
		}
		
		countLine++;	
		lineList.add(new XYChartData(lineName, coordinateX, dataY, paintColor));
		
		findMaxDataY();	  //找出纵坐标的最大值
		this.coordinateX = coordinateX;
	}
	
	public void redraw(){
		invalidate();
	}
	
	public  void clearLineList() {
		lineList.clear(); 
		countLine = 0;
		invalidate();
	}
}


