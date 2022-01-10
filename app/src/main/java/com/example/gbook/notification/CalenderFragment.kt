package com.example.gbook.notification

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.work.*
import com.example.gbook.R
import com.example.gbook.databinding.FragmentCalenderBinding
import com.example.gbook.notification.NotifyWork.Companion.NOTIFICATION_ID
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar.make
import kotlinx.android.synthetic.main.fragment_calender.*
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.ZonedDateTime
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
//        val notificationWork = OneTimeWorkRequest.Builder(NotifyWork::class.java)
//            .setInitialDelay(delay, MILLISECONDS).setInputData(data).build()
//
//        val instanceWorkManager = WorkManager.getInstance(requireContext())
//        instanceWorkManager.beginUniqueWork(NOTIFICATION_WORK,
//            ExistingWorkPolicy.KEEP, notificationWork).enqueue()


//        val periodicWorkRequest = PeriodicWorkRequest.Builder(NotifyWork::class.java, 1, TimeUnit.MINUTES)
//            .setInitialDelay(delay,TimeUnit.MILLISECONDS).build()
//        WorkManager.getInstance(requireContext()).enqueue(periodicWorkRequest)


        val notificationWork = PeriodicWorkRequest.Builder(NotifyWork::class.java, 1, TimeUnit.DAYS)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(NotifyWork.NOTIFICATION_NAME, ExistingPeriodicWorkPolicy.REPLACE, notificationWork)
//        scheduleNotification(cal.timeInMillis, data)



//        val x = PeriodicWorkRequestBuilder<NotifyWork>(delay,TimeUnit.MINUTES , delay,TimeUnit.MINUTES)
//            .setInitialDelay(delay,TimeUnit.MILLISECONDS).build()
//        WorkManager.getInstance(requireContext()).enqueue(x)
//        WorkManager.getInstance().getWorkInfoByIdLiveData(x.id)
//            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { workInfo ->
//                if ((workInfo != null) && (workInfo.state == WorkInfo.State.ENQUEUED)){
//               val myOutputData = workInfo.outputData.getString("KEY_MY_DATA")
//                }
//            })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun Context.enqueueReminderWorker(
        localTime: LocalTime
    ) {
        val now = ZonedDateTime.now()
        val trigger = now.with(localTime)
        val realTrigger = when {
            trigger <= now -> trigger.plusDays(1)
            else -> trigger
        }

        val initialDelay = maxOf(1, realTrigger.toEpochSecond() - Instant.now().epochSecond)

        val notificationWork = PeriodicWorkRequest.Builder(NotifyWork::class.java, 1, TimeUnit.MINUTES)
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(NotifyWork.NOTIFICATION_NAME, ExistingPeriodicWorkPolicy.REPLACE, notificationWork)
    }
}
