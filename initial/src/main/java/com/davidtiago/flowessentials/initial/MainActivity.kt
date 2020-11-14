package com.davidtiago.flowessentials.initial

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.davidtiago.flowessentials.initial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progress.hide()
        binding.button.setOnClickListener {
            binding.textView.text = ""
            binding.progress.show()
            val number = binding.editTextNumber.text.toString().toLong()
            val count = isPrimeNo(number)
            if (count == 0.toLong()) {
                binding.textView.text = "$number \n is a prime number 👍"
            } else {
                binding.textView.text =
                    "$number \n is NOT a prime number 👎 \n can be divided by $count other numbers"
            }
            binding.progress.hide()
        }
    }

}

private fun isPrimeNo(number: Long): Long {
    val range = 2.toLong()..number / 2.toLong()
    var divisorCount: Long = 0
    for (i in range) {
        if (number.rem(i) == 0.toLong()) {
            Log.d("isPrimeNo", "Can be divided by $i")
            divisorCount += 1
        } else {
            Log.d("isPrimeNo", "Can't be divided by $i")
        }
    }
    return divisorCount
}
