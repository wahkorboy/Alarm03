package com.wahkor.alarm03

import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import com.wahkor.alarm03.model.MusicItem

class MusicActivity : AppCompatActivity() {
    private lateinit var playBtn:Button
    private lateinit var stopBtn:Button
    private lateinit var tv_pass:TextView
    private lateinit var titleTextview:TextView
    private lateinit var tv_due:TextView
    private lateinit var mediaPlayer:MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private lateinit var listview: ListView
    private lateinit var seekbar: SeekBar
    private lateinit var adapter: musicAdapter
    private val REQUEST_CODE_ASK_PERMISSIONS = 117569
    private val playlist = ArrayList<MusicItem>()
    var selectid: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        titleTextview=findViewById(R.id.musicPlayerDisplayTextView)
        listview = findViewById(R.id.musicListview)
        tv_pass=findViewById(R.id.musicTvPassTextView)
        tv_due=findViewById(R.id.musicTvDueTextView)
        playBtn=findViewById(R.id.musicPlayButton)
        stopBtn=findViewById(R.id.musicStopButton)
        seekbar=findViewById(R.id.musicSeekBar)
        checkPermissions()
        mediaPlayer= MediaPlayer()

        //SeekBar
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })


    }

    fun playBTNCLICK(view: View) {
        if(playBtn.text.toString()=="PAUSE"){
            mediaPlayer.pause()
            playBtn.text="CONTINUE"
        }else{
            mediaPlayer.start()
            playBtn.text="PAUSE"
        }
    }
    fun stopBTNCLICK(view: View) {}
    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_ASK_PERMISSIONS
            )
        } else {
            loadMusic()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMusic()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }

    private fun loadMusic() {
        playlist.clear() //clear list
        val allMusic = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allMusic, null, selection, null, null)
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val title =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val duration =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                playlist.add(MusicItem(title, uri, duration))
            }
        }
        cursor?.close()
        adapter = musicAdapter(this, playlist)
        listview.adapter = adapter
        adapter.notifyDataSetInvalidated()
    }

    private fun getTimeInMinute(time: Int): String {
        var minute: Int = time / 60
        val hours: Int = minute / 60
        val seconds: Int = time - minute * 60
        minute -= hours * 60
        return "${if (hours > 0) "$hours:" else ""}$minute:${if (seconds < 10) "0$seconds" else "$seconds"}"
    }

    inner class musicAdapter(var context: Context, var arraylst: ArrayList<MusicItem>) :
        BaseAdapter() {
        override fun getCount(): Int {
            return arraylst.size
        }

        override fun getItem(position: Int): Any {
            return arraylst[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = View.inflate(context, R.layout.playlist_layout, null)
            val song = arraylst[position]
            val title = view.findViewById<TextView>(R.id.playlistTitleTextView)
            title.text = song.title
            view.setOnClickListener {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(song.uri)
                mediaPlayer.prepare()
                mediaPlayer.start()
                selectid=position
                titleTextview.text=song.title
                var string=song.title!!+"              "
                seekbar.max=mediaPlayer.seconds
                playBtn.visibility=View.VISIBLE
                playBtn.text="PAUSE"
                runnable= Runnable {
                    if (position==selectid){
                        seekbar.progress=mediaPlayer.currentSeconds
                        tv_pass.text=getTimeInMinute(mediaPlayer.currentSeconds)
                        tv_due.text=getTimeInMinute(mediaPlayer.seconds-mediaPlayer.currentSeconds)
                        string=string.substring(5,string.length)+string.substring(0,5)
                        title.text = if(string.length>40) string.substring(0,40) else string
                        title.setTextColor(resources.getColor(R.color.text_run_color))
                        handler.postDelayed(runnable,1000)
                    }else{
                        title.text=song.title
                        title.setTextColor(resources.getColor(R.color.black))
                        //handler.removeCallbacks(runnable)
                    }
                }
                handler.postDelayed(runnable,1000)

            }

            return view
        }
    }
}
// 1
private val MediaPlayer.seconds: Int
    get() {
        return this.duration / 1000
    }

// 2
private val MediaPlayer.currentSeconds: Int
    get() {
        return this.currentPosition / 1000
    }