package me.wai.AndroidWai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class GenericListAdapter<T> extends ArrayAdapter<T> {

    public interface Delegate<T> {
        public void onItemClick(T item);
        public void initializeView(View v);
        public void bindView(View v, T item);
    }

    Delegate<T> clickHandler;
    int itemLayoutResourceId;

    public GenericListAdapter(Context context, int itemLayoutResourceId, ArrayList<T> items, Delegate<T> clickHandler) {
        super(context, 0, items);
        this.clickHandler = clickHandler;
        this.itemLayoutResourceId = itemLayoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;

        if (result == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            result = inflater.inflate(itemLayoutResourceId,null);

            clickHandler.initializeView(result);
        }

        final T item = this.getItem(position);

        clickHandler.bindView(result, item);

        result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.onItemClick(item);
            }
        });

        return result;
    }
}
