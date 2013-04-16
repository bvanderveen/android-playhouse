package me.wai.AndroidWai;

import android.os.AsyncTask;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.util.functions.Func1;

public class Task<T> extends AsyncTask<Void, Void, T> {

    public interface TaskDelegate<T> {
        T perform();
    }

    public static <T> Observable<T> performInBackground(final TaskDelegate<T> d) {
        return Observable.create(new Func1<Observer<T>, Subscription>() {
            public Subscription call(Observer<T> o) {
                final Task<T> t = new Task<T>(d, o);
                t.execute();
                return new Subscription() {
                    @Override
                    public void unsubscribe() {
                        if (t.getStatus() != AsyncTask.Status.FINISHED)
                            t.cancel(true);
                    }
                };
            }
        });
    }

    TaskDelegate<T> d;
    Observer<T> o;

    public Task(TaskDelegate<T> d, Observer<T> o) {
        this.d = d;
        this.o = o;
    }

    protected T doInBackground(Void... v) {
        try {
            return d.perform();
        }
        catch (Exception e) {
            o.onError(e);
        }
        return null;
    }

    protected void onPostExecute(T result) {
        o.onNext(result);
        o.onCompleted();
    }
}
