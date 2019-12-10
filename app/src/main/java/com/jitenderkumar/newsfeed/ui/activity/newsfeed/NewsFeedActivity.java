package com.jitenderkumar.newsfeed.ui.activity.newsfeed;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.models.FeedListResponse;
import com.jitenderkumar.newsfeed.models.Note;
import com.jitenderkumar.newsfeed.models.Result;
import com.jitenderkumar.newsfeed.network.DataManager;
import com.jitenderkumar.newsfeed.ui.activity.NewsDetailActivity;
import com.jitenderkumar.newsfeed.ui.adapter.FeedAdapter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class NewsFeedActivity extends AppCompatActivity  {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    public static String INTENT_DATA = "intent_data";
    private String TAG = "DISPOSE";
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        compositeDisposable = new CompositeDisposable();

        initViews();

        Observer<String> animalsObserver = getAnimalsObserver();

        Observable<String> animalsObservable = Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");

        compositeDisposable.add( animalsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<String>() {

                    @Override
                    public boolean test(String s) throws Exception {
                        return s.contains("a");
                    }
                })
                .map(new Function<String, String>() {

                    @Override
                    public String apply(String s) throws Exception {
                        return s.toUpperCase();
                    }
                })
                .subscribeWith(getAnimalCapsObserver())
        );

        compositeDisposable.add(
                         getNotesObservable()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(new Function<Note, Note>() {
                            @Override
                            public Note apply(Note note) throws Exception {

                                note.setAuthorName(note.getAuthorName().toUpperCase());
                                return note;
                            }
                        })
                        .subscribeWith(getNotesObserver())
        );
    }

    private void initViews() {
        mRecyclerView = findViewById(R.id.recyler_view);
        mProgressBar = findViewById(R.id.progress_circular);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getNewsFeed();
    }

     private void getNewsFeed() {
        mProgressBar.setVisibility(View.VISIBLE);
        DataManager.getInstance().getFeedList(new DataManager.DataManagerListener() {
            @Override
            public void onSuccess(Object response) {
                mProgressBar.setVisibility(View.GONE);
                if (response instanceof FeedListResponse) {
                    FeedListResponse feedListResponse = (FeedListResponse) response;
                    setAdapter((ArrayList<Result>) feedListResponse.getResults());
                }
            }

            @Override
            public void onError(Object error) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
     }

     private Observable<Note> getNotesObservable() {
        final ArrayList<Note> notesArraylist = getNotesData();

        return Observable.create(new ObservableOnSubscribe<Note>() {
            @Override
            public void subscribe(ObservableEmitter<Note> emitter) throws Exception {
                for (Note note : notesArraylist) {
                    if (!emitter.isDisposed()) {
                        emitter.onNext(note);
                    }
                }

                if (!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

     private ArrayList<Note> getNotesData() {
        ArrayList arrayList = new  ArrayList<Note>();
        arrayList.add(new Note("hello", "jitu"));
        arrayList.add(new Note("hello", "jitu"));
        arrayList.add(new Note("hello", "jitu"));
        return arrayList;
     }

     private Observer<String> getAnimalsObserver() {
            return new Observer<String>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Log.d(TAG, "onSubscribe");
                }

                @Override
                public void onNext(String s) {
                    Log.d(TAG, "Name: " + s);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "onError: " + e.getMessage());
                }

                @Override
                public void onComplete() {
                    Log.d(TAG, "All items are emitted!");
                }
            };
     }


     private DisposableObserver<String> getAnimalCapsObserver() {
        DisposableObserver disposableObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: " + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete: ");
            }
        };

        return disposableObserver;
     }

    private DisposableObserver<Note> getNotesObserver() {
        DisposableObserver disposableObserver = new DisposableObserver<Note>() {
            @Override
            public void onNext(Note s) {
                Log.d(TAG, "Name: " + s.getAuthorName());
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error: " + e);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "complete: ");
            }
        };

        return disposableObserver;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void setAdapter(ArrayList<Result> listFeeds) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(NewsFeedActivity.this));
        FeedAdapter feedAdapter = new FeedAdapter(this, listFeeds, new FeedAdapter.IFeedCallback() {
            @Override
            public void onItemClicked(Result result) {
                Intent intent = new Intent(NewsFeedActivity.this,
                        NewsDetailActivity.class);
                intent.putExtra(INTENT_DATA, result);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(feedAdapter);
    }
}