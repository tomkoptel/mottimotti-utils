package com.mottimotti.android.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AnimationListActivity extends AnimationSherlockListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<AnimationHolder> adapter = new ArrayAdapter<AnimationHolder>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getItems());
        setListAdapter(adapter);
    }

    public List<AnimationHolder> getItems() {
        List<AnimationHolder> items = new ArrayList<AnimationHolder>();
        items.add(new AnimationHolder(FROM_LEFT_TO_RIGHT, "FROM_LEFT_TO_RIGHT"));
        items.add(new AnimationHolder(FROM_RIGHT_TO_LEFT, "FROM_RIGHT_TO_LEFT"));
        items.add(new AnimationHolder(FROM_TOP_TO_BOTTOM, "FROM_TOP_TO_BOTTOM"));
        items.add(new AnimationHolder(FROM_BOTTOM_TO_TOP, "FROM_BOTTOM_TO_TOP"));
        return items;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        ArrayAdapter<AnimationHolder> adapter = (ArrayAdapter<AnimationHolder>) listView.getAdapter();
        AnimationHolder holder = adapter.getItem(position);
        Intent intent = new Intent(this, SampleActivity.class);
        startActivityWithAnimation(intent, holder.type);
    }

    private class AnimationHolder {
        int type;
        String name;

        AnimationHolder(int type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
