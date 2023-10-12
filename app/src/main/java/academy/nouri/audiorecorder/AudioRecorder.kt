package academy.nouri.audiorecorder

import android.content.Context
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.FileOutputStream

class AudioRecorder(private val context: Context) {

    private var recorder : MediaRecorder? = null

    //Create Recorder
    private fun createRecorder() : MediaRecorder{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            MediaRecorder(context)
        else
            MediaRecorder()
    }

    fun start(outputFile : File){
        //Start recording by the created recorder
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this
        }
    }

    fun stop(){
        //Stop recording
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}