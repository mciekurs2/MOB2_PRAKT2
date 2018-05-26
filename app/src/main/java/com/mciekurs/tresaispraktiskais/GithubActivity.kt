package com.mciekurs.tresaispraktiskais

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.JsonReader
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_github.*
import okhttp3.*
import java.io.IOException
import java.io.StringReader

class GithubActivity : AppCompatActivity() {

    private var list = listOf<UserRepos>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github)

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        recyclerView_github.layoutManager = manager

        button_confirm.setOnClickListener {
            jsonToList(editText_repoUser.text.toString())
            //nestrādā nezināmu iemeslu dēļ
            editText_repoUser.clearFocus()
        }


    }

    private fun jsonToList(username: String){
        val url = "https://api.github.com/users/$username/repos"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call?, e: IOException?) {
                return
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                try {
                    list = gson.fromJson(body, Array<UserRepos>::class.java).asList()
                    runOnUiThread { recyclerView_github.adapter = ViewAdapter(list) }
                } catch (ex: IllegalStateException){
                    //nestrāda
                    return
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.menu_maps, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

