package com.example.myapplication.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import com.example.myapplication.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var viewmodel: MainViewModel

    private lateinit var GET: SharedPreferences
    private lateinit var SET: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GET = getSharedPreferences(packageName, MODE_PRIVATE)
        SET = GET.edit()

        viewmodel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        var cName = GET.getString("cityName", "bangkok")?.toLowerCase()
        edittext_city_name.setText(cName)
        viewmodel.refreshData(cName!!)

        getLiveData()

        swipe_refresh_layout.setOnRefreshListener {
            linearlayout_data.visibility = View.GONE
            textview_error.visibility = View.GONE
            progressbar_loading.visibility = View.GONE

            var cityName = GET.getString("cityName", cName)?.toLowerCase()
            edittext_city_name.setText(cityName)
            viewmodel.refreshData(cityName!!)
            swipe_refresh_layout.isRefreshing = false
        }

        button_search_city.setOnClickListener {
            val cityName = edittext_city_name.text.toString()
            SET.putString("cityName", cityName)
            SET.apply()
            viewmodel.refreshData(cityName)
            getLiveData()
            Log.i(TAG, "onCreate: " + cityName)
        }

        button_go_temp_page.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            finish()
            startActivity(intent)
        }

    }

    private fun getLiveData() {

        viewmodel.weather_data.observe(this, Observer { data ->
            data?.let {
                linearlayout_data.visibility = View.VISIBLE

                textview_city_code.text = data.sys.country.toString()
                textview_city_name.text = data.name.toString()

                textview_weather.text = data.weather.get(0).main
                textview_weather_description.text = data.weather.get(0).description

            }
        })

        viewmodel.weather_error.observe(this, Observer { error ->
            error?.let {
                if (error) {
                    textview_error.visibility = View.VISIBLE
                    progressbar_loading.visibility = View.GONE
                    linearlayout_data.visibility = View.GONE
                } else {
                    textview_error.visibility = View.GONE
                }
            }
        })

        viewmodel.weather_loading.observe(this, Observer { loading ->
            loading?.let {
                if (loading) {
                    progressbar_loading.visibility = View.VISIBLE
                    textview_error.visibility = View.GONE
                    linearlayout_data.visibility = View.GONE
                } else {
                    progressbar_loading.visibility = View.GONE
                }
            }
        })

    }
}