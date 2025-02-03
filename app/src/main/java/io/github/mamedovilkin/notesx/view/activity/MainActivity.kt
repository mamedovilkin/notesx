package io.github.mamedovilkin.notesx.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.mamedovilkin.notesx.R
import io.github.mamedovilkin.notesx.databinding.ActivityMainBinding
import io.github.mamedovilkin.notesx.view.fragment.NewNoteFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        if (intent.dataString != null && intent.dataString == "new_note") {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container_view, NewNoteFragment())
                .commit()
        }
    }
}