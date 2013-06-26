package com.mottimotti.android.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockListActivity;
import com.mottimotti.android.R;
import com.mottimotti.android.utils.SwipeDismissListener;

public class SwipeDismissListActivity extends SherlockListActivity implements SwipeDismissListener.OnDismissCallback {
    private Toast toast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_swipe_list);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        SwipeDismissListener listTouchListener = new SwipeDismissListener(getListView(), this);
        getListView().setOnTouchListener(listTouchListener);
        getListView().setOnScrollListener(listTouchListener.makeScrollListener());

        View header = getLayoutInflater().inflate(R.layout.sample_swipe_header_list, getListView(), false);
        getListView().addHeaderView(header, null, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getItems());
        setListAdapter(adapter);
    }

    private String[] getItems() {
        String[] items = new String[15];
        for (int i = 0; i < 15; i++) {
            items[i] = "item" + i;
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onDismiss(ListView listView, int[] reverseSortedPositions) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
        for (int position : reverseSortedPositions) {
            String item = adapter.getItem(position);
            toast.setText(item);
            toast.show();
        }
    }
}
