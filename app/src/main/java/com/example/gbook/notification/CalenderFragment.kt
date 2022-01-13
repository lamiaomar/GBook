package com.example.gbook.notification


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.*
import com.example.gbook.R
import com.example.gbook.databinding.FragmentCalenderBinding
import com.example.gbook.notification.NotifyWork.Companion.NOTIFICATION_ID
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.make
import kotlinx.android.synthetic.main.fragment_calender.*
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class CalenderFragment : Fragment() {

    lateinit var binding : FragmentCalenderBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCalenderBinding.inflate(inflater)

        binding.lifecycleOwner = this

        userInterface()

        return binding.root
    }


    private fun userInterface(){

        val titleNotification = getString(R.string.notification_title)
        collapsing_toolbar_l?.title = titleNotification

        binding.doneFab.setOnClickListener {
            val customCalendar = Calendar.getInstance()
            customCalendar.set(
                date_p.year , date_p.month , date_p.dayOfMonth , time_p.hour , time_p.minute , 0
            )

            val customTime = customCalendar.timeInMillis
            val currentTime = currentTimeMillis()
            if (customTime > currentTime){
                val data = Data.Builder().putInt(NOTIFICATION_ID , 0).build()
                val delay = customTime - currentTime
                Log.e("TAG", "userInterface: $delay", )
                scheduleNotification(delay, data)

                val titleNotificationSchedule = getString(R.string.notification_schedule_title)
                val patternNotificationSchedule = getString(R.string.notification_schedule_pattern)
                make(
                    coordinator_l,

                    titleNotificationSchedule + SimpleDateFormat(
                        patternNotificationSchedule, Locale.getDefault()
                    ).format(customCalendar.time).toString(),
                    LENGTH_LONG
                ).show()
            }else{
                val errorNotificationSchedule = getString(R.string.notification_schedule_error)
                make(coordinator_l , errorNotificationSchedule, LENGTH_LONG).show()
            }
        }
    }


    private fun scheduleNotification(delay: Long, data: Data) {

        val notificationWork = PeriodicWorkRequest.Builder(NotifyWork::class.java, 1, TimeUnit.DAYS)
            .setInitialDelay(delay , TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(NotifyWork.NOTIFICATION_NAME, ExistingPeriodicWorkPolicy.REPLACE, notificationWork)

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

}
