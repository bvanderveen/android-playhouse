package me.wai.AndroidWai;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import rx.util.functions.Action0;
import rx.util.functions.Action1;

import java.util.ArrayList;

public class MainActivity extends ListActivity implements GenericListAdapter.Delegate<NewsItem> {
    ArrayList<NewsItem> items;
    Client client;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);

        HttpClient httpClient = new HttpClientImpl("http://apify.heroku.com");
        Client client = new ClientImpl(httpClient);

        getHackerNews();
    }

    void reloadData() {
        setListAdapter(new GenericListAdapter<NewsItem>(this,
                R.layout.list_item_with_disclosure, items, (GenericListAdapter.Delegate<NewsItem>) this));
    }

    void getHackerNews() {
        client.getHackerNews().subscribe(
                new Action1<ArrayList<NewsItem>>() {
                    @Override
                    public void call(ArrayList<NewsItem> x) {
                        items = x;
                    }
                },
                new Action1<Exception>() {
                    @Override
                    public void call(Exception e) {
                        Errors.handleError(e);
                    }
                },
                new Action0() {
                    @Override
                    public void call() {
                        reloadData();
                    }
                }
        );
    }

    void alert(String message) {
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Hey");
        alert.setMessage(message);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    @Override
    public void initializeView(View v) {
        ImageView disclosure = (ImageView) v.findViewById(R.id.disclosure);
        disclosure.setImageResource(R.drawable.disclosure);
    }

    @Override
    public void bindView(View v, NewsItem item) {
        TextView label = (TextView) v.findViewById(R.id.term);
        label.setText(item.getTitle());
    }

    @Override
    public void onItemClick(NewsItem i) {
        alert(i.getTitle());
    }

}