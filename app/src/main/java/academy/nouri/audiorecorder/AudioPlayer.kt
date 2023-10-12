package academy.nouri.audiorecorder

import android.content.Context
import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.core.net.toUri
import kotlinx.coroutines.*
import java.io.File

class AudioPlayer(private val context: Context) {

    private var player: MediaPlayer? = null
    private var job: Job? = null

    fun playFile(file : File,seekBar: SeekBar) {
        //if it is playing at first stop it
        player?.let {
            if (it.isPlaying) {
                stop()
            }
        }
        //play the mp3 recorded file in directory
        MediaPlayer.create(context,file.toUri()).apply {
            player = this
            start()
        }

        attachSeekToPlayer(seekBar)
    }

    private fun attachSeekToPlayer(seekBar: SeekBar) {
        //Show the position of playing by sekk bar
        player?.let {
            seekBar.max = it.duration
            job = CoroutineScope(Dispatchers.Main).launch {
                while (it.isPlaying) {
                    delay(100)
                    seekBar.progress = it.currentPosition
                }
            }
        }
    }


    fun stop() {
        player?.stop()
        player?.release()
        player = null
        job?.cancel()
    }


}