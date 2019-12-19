package com.jitenderkumar.newsfeed.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jitenderkumar.newsfeed.R;
import com.jitenderkumar.newsfeed.models.Ticket;
import com.jitenderkumar.newsfeed.network.DataManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class RxMainActivity extends AppCompatActivity {

    String flightNumber;
    String from;
    String to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_main);

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        ConnectableObservable<List<Ticket>> connectableObservable = DataManager
                .getInstance()
                .getSearchTicket(from, to)
                .replay();

        compositeDisposable.add(
                connectableObservable
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeOn(Schedulers.io())
               .subscribeWith(new DisposableObserver() {
                   @Override
                   public void onNext(Object o) {
                   }

                   @Override
                   public void onError(Throwable e) {

                   }

                   @Override
                   public void onComplete() {

                   }
               })
        );

        compositeDisposable.add(
                connectableObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        /**
                         * Converting List<Ticket> emission to single Ticket emissions
                         * */
                        .flatMap(new Function<List<Ticket>, ObservableSource<Ticket>>() {
                            @Override
                            public ObservableSource<Ticket> apply(List<Ticket> tickets) throws Exception {
                                return Observable.fromIterable(tickets);
                            }
                        })
                        /**
                         * Fetching price on each Ticket emission
                         * */
                        .flatMap(new Function<Ticket, ObservableSource<Ticket>>() {
                            @Override
                            public ObservableSource<Ticket> apply(Ticket ticket) throws Exception {
                                return DataManager.getInstance().getPriceObservable(ticket);
                            }
                        })
                        .subscribeWith(new DisposableObserver<Ticket>() {

                            @Override
                            public void onNext(Ticket ticket) {
                               // int position = ticketsList.indexOf(ticket);

                               /* if (position == -1) {
                                    // TODO - take action
                                    // Ticket not found in the list
                                    // This shouldn't happen
                                    return;
                                }*/

                                //ticketsList.set(position, ticket);
                                //mAdapter.notifyItemChanged(position);
                            }

                            @Override
                            public void onError(Throwable e) {
                               // showError(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        }));

        // Calling connect to start emission
        connectableObservable.connect();
    }
}
