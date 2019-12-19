package com.jitenderkumar.newsfeed.rxJava

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.jitenderkumar.newsfeed.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.functions.Predicate
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*

class RXDebounceMainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initBufferRX()
    }

    private fun initBufferRX() {
        var observableInt = Observable.just(1,2,3,4,5,6)

        observableInt
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .buffer(3)
            .subscribe(object : Observer<MutableList<Int>?> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: MutableList<Int>) {
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    fun observeEdittext(): Observable<String> {
        var publishSubject = PublishSubject.create<String>()
        edit_query.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               publishSubject.onNext(s.toString())
            }
        })

        return publishSubject
    }

    fun rxRangeOperator() {
        Observable.range(1, 20)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .filter(object : Predicate<Int?> {
                override fun test(t: Int): Boolean {
                    return t%2 == 0
                }
            })
            .map(object : Function<Int, String> {

                override fun apply(t: Int): String {
                    return ""
                }
            })
            .subscribe(object : Observer<String?> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onNext(t: String) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onError(e: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
    }
}