package com.example.plombier

import android.annotation.TargetApi
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.dashboard_content.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
class Dashboard : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var noteManager: NoteManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteManager = NoteManager(Room.databaseBuilder(applicationContext,DBNote::class.java,"DB_Note").build())
        setContentView(R.layout.dashboard)
        toolbar = findViewById(R.id.dashboard_toolbar)
        setSupportActionBar(toolbar)
        rv_dashboard.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener { view ->
            val dialog = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.layout_dialogue, null)
            val title = view.findViewById<EditText>(R.id.title)
            val content = view.findViewById<EditText>(R.id.content)



            dialog.setView(view)
            dialog.setPositiveButton("Add") { _: DialogInterface, _: Int ->
                    val Note = Note()
                    Note.title = title.text.toString()
                    Note.content = content.text.toString()

                doAsync{

                    val list = noteManager.addNote(Note)
                    uiThread{
                        refresh(list)
                    }
                }

            }
            dialog.setNegativeButton("Cancel") { _: DialogInterface, _: Int ->
            }
            dialog.show()
        }



    }

    override fun onResume() {
        doAsync {
            val list = noteManager.getNotes()
            uiThread{
                refresh(list)
            }
        }



        super.onResume()

    }
    fun refresh(Notes:MutableList<Note>){
        rv_dashboard.adapter = DashboardAdapter(this,Notes)
    }
    class DashboardAdapter(val activity: Dashboard , val list: MutableList<Note>) :
        RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item, parent, false))
        }

        override fun getItemCount(): Int {
            return list.size
        }

        @TargetApi(Build.VERSION_CODES.O)
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
            holder.content.text = list[p1].content
            holder.title.text = list[p1].title

            holder.deleteBtn.setOnClickListener{
                doAsync{
                    val list = activity.noteManager.deleteNotes(list[p1])
                    uiThread {
                        activity.refresh(list)
                    }
                }
            }

            holder.editBtn.setOnClickListener{
                val dialog = AlertDialog.Builder(activity)
                val view = activity.layoutInflater.inflate(R.layout.layout_dialogue, null)
                dialog.setView(view)
                val title = view.findViewById<EditText>(R.id.title)
                val content = view.findViewById<EditText>(R.id.content)

                dialog.setPositiveButton("modifier") { _: DialogInterface, _: Int ->
                    var Note = Note()
                    Note.num = list[p1].num
                    Note.title = title.text.toString()
                    Note.content = content.text.toString()

                    doAsync{
                        val list = activity.noteManager.editNote(Note)
                        uiThread {
                            activity.refresh(list)
                        }
                    }
                }
                dialog.setNegativeButton("annuler") { _: DialogInterface, _: Int ->
                }
                dialog.show()
            }
        }

        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val title:TextView = v.findViewById(R.id.tv_title)
            val content:TextView = v.findViewById(R.id.tv_content)
            val deleteBtn : FloatingActionButton = v.findViewById(R.id.rv_delete)
            val editBtn : FloatingActionButton = v.findViewById(R.id.rv_edit)


        }

    }
}