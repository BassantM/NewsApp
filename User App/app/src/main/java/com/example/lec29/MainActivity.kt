package com.example.lec29

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.lec29.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadNews()
        val adRequest = AdRequest.Builder().build()
        binding.bannerAdView.loadAd(adRequest)
    }
    private fun loadNews(){
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val code = prefs.getString("code","us")!!

        val cat = intent.getStringExtra("cat")!!

        val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit
            .Builder()
            .client(client)
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val callable = retrofit.create(NewsCallable::class.java)
        callable.getNews(cat,code).enqueue(object: Callback<News>{
            override fun onResponse(p0: Call<News>, p1: Response<News>) {
                val articles = p1.body()!!.articles
                val adapter = NewsAdapter(this@MainActivity, articles)
                binding.recyclerNews.adapter = adapter
                binding.progress.isVisible=false
                Log.d("Trace","Link: ${articles[0].urlToImage}")
            }

            override fun onFailure(p0: Call<News>, p1: Throwable) {
                Log.d("Trace","Error: ${p1.message}")
            }

        })
    }
}