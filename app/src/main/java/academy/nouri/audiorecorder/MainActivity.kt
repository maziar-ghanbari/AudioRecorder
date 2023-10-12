package academy.nouri.audiorecorder

import academy.nouri.audiorecorder.databinding.ActivityMainBinding
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.File

class MainActivity : AppCompatActivity() {

    //biding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private val audioRecorder: AudioRecorder by lazy { AudioRecorder(applicationContext) }
    private val audioPlayer: AudioPlayer by lazy { AudioPlayer(applicationContext) }
    private var audioFile : File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.apply {

            //Request permission if VERSION_CODE >= Marshmello
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.RECORD_AUDIO), 0
                )
            }

            //Toggle button for record audio
            record.setOnCheckedChangeListener{ buttonView, isChecked ->
                if (isChecked){
                    //Start recording and save to this file
                    File(cacheDir,"audio.mp3").also { file ->
                        audioRecorder.start(file)
                        audioFile = file
                        play.isEnabled = false
                    }
                }else{
                    //Stop recording
                    audioRecorder.stop()
                    play.isEnabled = true
                }
            }

            play.setOnClickListener {
                //Play the audio recorded
                audioFile?.let { file ->
                    audioPlayer.playFile(file,seekBar)
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //Check about request permission for audio recorder
        if (requestCode == 0) {
            binding?.record?.isEnabled =
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //Stop recording after end life of app
        audioRecorder.stop()
        audioPlayer.stop()
    }
}