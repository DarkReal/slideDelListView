package hytest.my.demo.hydemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * 用弹出popupwindow的方式展现滑动
 * Created by DarkReal on 2016/10/15.
 */
public class QQListView extends ListView {

    private static final String TAG = "QQListView";
    //用户滑动的最小距离
    private int touchSlop;

    private boolean isSliding;//是否响应滑动
    private int xDown;//手指按下时的x坐标
    private int yDown;//手指按下时的y坐标
    private int xMove;//手指移动时的x坐标
    private int yMove;//手指移动时的y坐标
    private Button delBtn;
    private int mpopupHeight;
    private int mpopupWidth;
    private int mcurrentViewPois;//当前手指触摸的位置
    private View mCurrentView;//当前所在的view
    private DelButtonClickListener mListener;//删除按钮的回调接口

    private LayoutInflater mInflater;//解析器
    private PopupWindow mpopupwindow;//弹出的popupwindow
    //构造器
    public QQListView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mInflater = LayoutInflater.from(context);//获得当前页面的解析器
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//触发移动事件的最短距离，小于这个距离就不会触发滑动事件

        View view = mInflater.inflate(R.layout.delete_btn,null);//获得删除按钮的解析器
        delBtn = (Button) view.findViewById(R.id.id_item_btn);

        mpopupwindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);//宽高都是取系统自带值

        //一定要这样才能获取到高度和宽度，赋值是0,如果是>0的值的话，就会直接赋予精确值？
        mpopupwindow.getContentView().measure(0,0);
        mpopupHeight = mpopupwindow.getHeight();
        mpopupWidth = mpopupwindow.getWidth();
    }

    //触摸事件的分发
    //按下的时候收起popupwindow,滑动的时候拉出popupwindow
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        //判断触摸的时候进行的操作
        switch (action){
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                yDown = y;
//                如果当前popupwindow显示状态，那么点击的时候要隐藏popup
                if(mpopupwindow.isShowing()&&mpopupwindow!=null){
                    mpopupwindow.dismiss();
//                    return false;//禁止了事件下传
                }
                //根据x,y得到position的值，但此时得到的值也包含可能未显示的listview的position，所以要减掉
                mcurrentViewPois = pointToPosition(xDown,yDown);//将按下的坐标转换为值
                //获得当前按下的item
                //当前的position减掉第一个可见的item的position得到相对于第一个item的相对的position的值，从而获取到这个item;
                //因为getChildAt()返回的一定是可见的LIstview的子项目，所以对于不可见的内容或者position无法找到的内容，返回是null
                View view = getChildAt(mcurrentViewPois - getFirstVisiblePosition());
                mCurrentView = view;
                break;
            case MotionEvent.ACTION_MOVE:
//                LogUtils.v("ACTION_MOVE---dispatchTouchEvent");
                xMove = x;
                yMove = y;
                //获取移动的距离
                int dx = xMove-xDown;
                int dy = yMove-yDown;
                //一定要是从右向左滑动才会触发操作
                if(xMove<xDown&&Math.abs(dx)>touchSlop&&dy<touchSlop){//水平滑动的距离大于最小触摸判断距离，垂直的距离小于最小滑动判断距离
                    isSliding = true;
                }
//                LogUtils.v("ACTION_MOVE---"+isSliding);
                break;
        }
        return super.dispatchTouchEvent(ev);//此时没有对于触摸事件进行拦截，子类可以接收触摸事件的反馈
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if(isSliding){//如果触发了滑动事件
            LogUtils.v("ACTION_MOVE---onTouchEvent");
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    int[] location = new int[2];
                    //返回当前位置的location数组
                    mCurrentView.getLocationOnScreen(location);

                    mpopupwindow.setAnimationStyle(R.animator.delete_btn_show);//展现的动画形式
                    mpopupwindow.update();
                    mpopupwindow.showAtLocation(mCurrentView, Gravity.LEFT | Gravity.TOP, location[0] + mCurrentView.getWidth(), location[1]);

                    //绑定里面每一个ListView的回调事件，然后这里只是单纯的绑定，实现需要在页面中具体填写内容
                    delBtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mListener != null) {
                                mListener.clickHappend(mcurrentViewPois);
                                mpopupwindow.dismiss();
                            }
                        }
                    });
                    break;
                case MotionEvent.ACTION_UP://当单击抬起的时候，取消滑动事件
                    isSliding = false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public void setDelButtonClickListener(DelButtonClickListener listener)
    {
        mListener = listener;
    }
    //自定义一个接口然后定义一些事件，在实现的时候具体填充事件内容，多用于自定义控件
    interface DelButtonClickListener
    {
        public void clickHappend(int position);//删除的回调
    }
}