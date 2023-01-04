package tj.navin.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import tj.navin.test.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val results = intent.getStringExtra("json_results")

        binding.jsonResultsTextview.text = results
    }
}