package com.example.voice0622

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.voice0622.databinding.ActivityMainBinding
import android.Manifest
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.RecognitionListener
import android.speech.tts.TextToSpeech
import android.view.SearchEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.TaskExecutors
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.tasks.Continuation
//import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    //바인딩 추가 초기화
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    //음성 코드
    private val PERMISSIONS_RECORD_AUDIO = 1
    private val RC_SPEECH_RECOGNIZER = 2
    private lateinit var tv1: TextView
    private lateinit var tv2: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



//        setContentView(R.layout.activity_main)
        setContentView(binding.root)

        tv1 = findViewById(R.id.tv1)
        tv2 = findViewById(R.id.tv2)

//      메뉴 버튼 클릭시 새창으로 이동
        var intent = Intent(this, SubActivity::class.java)
        val btnmenu : Button = findViewById(R.id.btnmenu)
        val btn_voice: Button = findViewById(R.id.btn_voice)

        binding.btnmenu.setOnClickListener{
            startActivity(intent)
        }
// 버튼1 클릭시 음성녹음 후 텍스트 변환
//        button1 = findViewById(R.id.button1)
//        text1 = findViewById(R.id.text1)




        btn_voice.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSIONS_RECORD_AUDIO)
            } else {
                startSpeechToText()
            }
        }

    }
    //
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_RECORD_AUDIO) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSpeechToText()
            }
        }
    }


    private fun startSpeechToText() {
        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성을 입력하세요.")
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
            // 음성 입력 준비가 완료되었을 때 호출됩니다.
                tv2.text = "말씀하세요"
            }
            override fun onBeginningOfSpeech() {
                // 음성 입력이 시작될 때 호출됩니다.
            }

            override fun onRmsChanged(rmsdB: Float) {
                // 음성 입력의 소리 크기가 변경될 때 호출됩니다.
            }

            override fun onBufferReceived(buffer: ByteArray?) {
                // 음성 입력에 대한 buffer를 받을 때 호출됩니다.
            }

            override fun onEndOfSpeech() {
                // 음성 입력이 종료될 때 호출됩니다.
                tv2.text = "녹음완료"
            }

            override fun onError(error: Int) {
                // 음성 입력 중 오류가 발생했을 때 호출됩니다.
                tv2.text = "녹음중 오류발생 다시 시도 하세요"
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val inputText = matches[0]
                    tv1.text = inputText
                    translateText(inputText)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                // 부분 결과를 받을 때 호출됩니다.
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
                // 추가 이벤트를 받을 때 호출됩니다.
            }
        })
        speechRecognizer.startListening(intent)
    }

    private fun translateText(inputText: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.KOREAN)
                //대상 언어 선택
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        val downloadTask = translator.downloadModelIfNeeded(conditions)
        downloadTask.addOnSuccessListener {
            val translateTask = translator.translate(inputText)
            translateTask.addOnSuccessListener { translatedText ->
                // 번역된 텍스트를 표시합니다.
                tv2.text = translatedText
            }
        }
    }
}




