package com.example.textfieldissue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.textfieldissue.databinding.ActivityViewBinding

class ViewActivity : AppCompatActivity() {

	private lateinit var binding: ActivityViewBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityViewBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}
}