package net.joyfulworld.example.googlestt

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        val TAG = "MainActivity"
    }
    val REQUESTCODE_PERMISSION = 10001
    lateinit var recognizer: SpeechRecognizer
    var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= 23) {
            // permission
            val permissions = arrayOf(Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO)
            ActivityCompat.requestPermissions(this, permissions, REQUESTCODE_PERMISSION)
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")

        btn_main_start_recognition.setOnClickListener {
            recognizer = SpeechRecognizer.createSpeechRecognizer(this)
            recognizer.setRecognitionListener(recognizerListener)
            recognizer.startListening(intent)
        }
    }

    val recognizerListener = object: RecognitionListener {
        override fun onReadyForSpeech(p0: Bundle?) {
            Log.d(TAG, "onReadyForSpeech()")
            Toast.makeText(applicationContext,"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show()
        }

        override fun onRmsChanged(p0: Float) {
            Log.d(TAG, "onRmsChanged()")
        }

        override fun onBufferReceived(p0: ByteArray?) {
            Log.d(TAG, "onBufferReceived()")
        }

        override fun onPartialResults(p0: Bundle?) {
            Log.d(TAG, "onPartialResults()")
        }

        override fun onEvent(p0: Int, p1: Bundle?) {
            Log.d(TAG, "onEvent()")
        }

        override fun onBeginningOfSpeech() {
            Log.d(TAG, "onBeginningOfSpeech()")
        }

        override fun onEndOfSpeech() {
            Log.d(TAG, "onEndOfSpeech()")
        }

        override fun onError(error: Int) {
            Log.d(TAG, "onError()")
            when (error) {
                SpeechRecognizer.ERROR_AUDIO -> message = "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> message = "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> message = "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> message = "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> message = "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> message = "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> message = "RECOGNIZER가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> message = "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> message = "말하는 시간초과"
                else -> message = "알 수 없는 오류임"
            }

            Toast.makeText(
                applicationContext,
                "에러가 발생하였습니다. : $message",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onResults(results: Bundle?) {
            Log.d(TAG, "onResults()")
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            for (i in 0 until matches?.size!!) {
                tv_main_result.setText(matches[i])
            }
        }

    }
}
