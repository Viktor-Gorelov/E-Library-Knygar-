package com.vyhorelov.android.bookassistantapp
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class MainMenuFragment: Fragment() {

    private var callbacks: Callbacks? = null
    private lateinit var libraryButton: Button
    private lateinit var setReminderButton: Button

    interface Callbacks{
        fun booksScreenChosen()
        fun readCSVFIle() : MutableList<BookName>
        fun showTimePicker(view: View, context: Context)
        fun setReminder(hour: Int, minute: Int,second: Int,context: Context)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        libraryButton = view.findViewById(R.id.library_btn) as Button
        setReminderButton = view.findViewById(R.id.set_reminder_btn) as Button
        return view
    }

    class ReminderReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Час читання!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        libraryButton.setOnClickListener{view: View ->
            callbacks?.booksScreenChosen()
        }

        setReminderButton.setOnClickListener{view: View ->
            callbacks?.showTimePicker(view,requireContext())
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): MainMenuFragment{
            return MainMenuFragment()
        }
    }
}