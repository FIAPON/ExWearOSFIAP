package com.estudo.android.exwearosfiap

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import androidx.wear.widget.CircularProgressLayout
import kotlinx.android.synthetic.main.activity_confirm_screen.*

class ConfirmScreenActivity : WearableActivity(), CircularProgressLayout.OnTimerFinishedListener,
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_screen)

        setAmbientEnabled()

        circular_progress.apply {
            onTimerFinishedListener = this@ConfirmScreenActivity
            setOnClickListener(this@ConfirmScreenActivity)

            totalTime = 2000
            startTimer()
        }

    }

    override fun onTimerFinished(layout: CircularProgressLayout?) {}

    override fun onClick(view: View?) {
        circular_progress?.stopTimer()
    }
}