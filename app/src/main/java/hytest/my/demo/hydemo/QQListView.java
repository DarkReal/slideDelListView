package hytest.my.demo.hydemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.ListView;

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
    private int Move;//手指移动时的y坐标
    private Button delBtn;

    private LayoutInflater mInflater;//解析器
    //构造器
    public QQListView(Context context, AttributeSet attrs) {

        super(context, attrs);

        mInflater = LayoutInflater.from(context);//获得当前页面的解析器
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();//触发移动事件的最短距离，小于这个距离就不会触发滑动事件

        View view = mInflater.inflate(R.layout.delete_btn,null);//获得删除按钮的解析器
        delBtn = (Button) view.findViewById(R.id.id_item_btn);


    }
}
