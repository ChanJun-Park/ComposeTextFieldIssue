package com.example.textfieldissue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.textfieldissue.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		initButtons()
	}

	private fun initButtons() {
		binding.buttonView.setOnClickListener {
			startActivity(Intent(this, ViewActivity::class.java))
		}

		binding.buttonCompose.setOnClickListener {
			startActivity(Intent(this, ComposeTestActivity::class.java))
		}
	}
}