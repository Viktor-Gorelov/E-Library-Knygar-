package com.vyhorelov.android.bookassistantapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.util.UUID

class MainActivity : AppCompatActivity(), MainMenuFragment.Callbacks, BooksScreenFragment.Callbacks{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null){
            val fragment = MainMenuFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun readCSVFIle() : MutableList<BookName> {
        val bufferReader = BufferedReader(assets.open("db.csv").reader())
        val csvParser = CSVParser.parse(
            bufferReader,
            CSVFormat.DEFAULT
        )

        val listOfBooks = mutableListOf<BookName>()
        csvParser.forEach {
            it?.let {
                val item = BookName(
                    id = it.get(0),
                    title = it.get(1),
                    genre = it.get(2),
                    author = it.get(3),
                    description = it.get(4),
                    review = it.get(5),
                    isFavorite = it.get(6).toBoolean(),
                    isComplete = it.get(7).toBoolean()
                )
                listOfBooks.add(item)
            }
        }
        return listOfBooks
    }

    override fun showTimePicker(view: View, context: Context) {

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(context,R.style.MyCustomTimePicker,
            { view, selectedHour, selectedMinute ->
                setReminder(selectedHour, selectedMinute,0, context)
            }, hour, minute, true)
        timePickerDialog.show()
    }

    override fun setReminder(hour: Int, minute: Int,second: Int, context: Context){

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager?
        val intent = Intent(context, MainMenuFragment.ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)

        alarmManager?.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        Toast.makeText(context, "Сповіщення встановлене на $hour:$minute", Toast.LENGTH_SHORT).show()
    }

    override fun booksScreenChosen() {
        val fragment = BooksScreenFragment.newInstance()
        supportFragmentManager.popBackStack()
        supportFragmentManager.popBackStack()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun addingBookScreenChoosen() {
        val fragment = AddBookFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBookSelected(bookId: UUID) {
        val fragment = BookDetailInfoFragment.newInstance(bookId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}