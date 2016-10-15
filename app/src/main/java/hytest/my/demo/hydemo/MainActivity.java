package hytest.my.demo.hydemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import hytest.my.demo.hydemo.QQListView.DelButtonClickListener;//删除的接口导入


public class MainActivity extends Activity
{
	private QQListView mListView;
	private ArrayAdapter<String> mAdapter;
	private List<String> mDatas;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mListView = (QQListView) findViewById(R.id.id_listview);

		mDatas = new ArrayList<String>(Arrays.asList("HelloWorld", "Welcome", "Java", "Android", "Servlet", "Struts",
				"Hibernate", "Spring", "HTML5", "Javascript", "Lucene"));
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDatas);
		mListView.setAdapter(mAdapter);

		mListView.setDelButtonClickListener(new DelButtonClickListener()
		{
			@Override
			public void clickHappend(final int position)
			{
				Toast.makeText(MainActivity.this, position + " : " + mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
				mAdapter.remove(mAdapter.getItem(position));
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Toast.makeText(MainActivity.this, position + " : " + mAdapter.getItem(position), 1).show();
			}
		});
	}
}
