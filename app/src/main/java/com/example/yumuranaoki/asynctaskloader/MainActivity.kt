package com.example.yumuranaoki.asynctaskloader

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.widget.TextView

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> = ResultLoader(this)

    override fun onLoadFinished(loader: Loader<String>, data: String?) {
        if (data != null) {
            val publicKeyText = findViewById<TextView>(R.id.publicKey)
            publicKeyText.text = data   
        }
        supportLoaderManager.destroyLoader(0)
    }

    override fun onLoaderReset(loader: Loader<String>) {
        
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportLoaderManager.initLoader(0, null, this)
    }
}
