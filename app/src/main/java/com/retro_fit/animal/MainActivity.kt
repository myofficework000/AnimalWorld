package com.retro_fit.animal

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.retro_fit.animal.databinding.ActivityMainBinding
import com.retro_fit.animal.model.Animal
import com.retro_fit.animal.model.ApiClient
import com.retro_fit.animal.model.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var retrofit: Retrofit
    lateinit var apiService: ApiService

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAPICall()

        binding.nextButton.setOnClickListener {
            binding.loadingSpinner.visibility = View.VISIBLE
            binding.loadingSpinner2.visibility = View.VISIBLE
            binding.loadingSpinner3.visibility = View.VISIBLE
            initAPICall()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initAPICall() {
        retrofit = ApiClient.getRetrofit()
        apiService = retrofit.create(ApiService::class.java)
        val animalCall: Call<Animal> = apiService.getRandomAnimal()
        animalCall.enqueue(object : Callback<Animal> {
            override fun onResponse(call: Call<Animal>, response: Response<Animal>) {
                if (response.isSuccessful) {
                    val animal = response.body()!!
                    binding.apply {
                        animal.apply {
                            animalName.text = animal.name
                            @SuppressLint("NewApi")
                            fun htmlManipulation() {
                                val t1: String = getColoredSpanned(
                                    "Animal found at - ",
                                    getColor(R.color.text_color_secondary)
                                )
                                val r1: String =
                                    getColoredSpanned(geo_range.toString(), getColor(R.color.red))

                                val t2: String = getColoredSpanned(
                                    "Habitat - ",
                                    getColor(R.color.text_color_secondary)
                                )
                                val r2: String =
                                    getColoredSpanned(habitat.toString(), getColor(R.color.red))

                                val t3: String = getColoredSpanned(
                                    "Animal type - ",
                                    getColor(R.color.text_color_secondary)
                                )
                                val r3: String =
                                    getColoredSpanned(animal_type.toString(), getColor(R.color.red))

                                val t4: String = getColoredSpanned(
                                    "Lifespan - ",
                                    getColor(R.color.text_color_secondary)
                                )
                                val r4: String =
                                    getColoredSpanned(lifespan.toString(), getColor(R.color.red))

                                animalAnimalGeoRange.text = Html.fromHtml(t1 + r1)
                                animalHabitat.text = Html.fromHtml(t2 + r2)
                                animalAnimalType.text = Html.fromHtml(t3 + r3)
                                animalLifespan.text = Html.fromHtml(t4 + r4)
                            }
                            htmlManipulation()
                        }
                        if (animal.geo_range != null) {
                            animalAnimalGeoRange.visibility = View.VISIBLE
                        }
                        if (animal.habitat != null) {
                            animalHabitat.visibility = View.VISIBLE
                        }
                        if (animal.animal_type != null) {
                            animalAnimalType.visibility = View.VISIBLE
                        }
                        if (animal.lifespan != null) {
                            animalLifespan.visibility = View.VISIBLE
                        }

                        loadingSpinner.visibility = View.GONE
                        loadingSpinner2.visibility = View.GONE
                        loadingSpinner3.visibility = View.GONE
                        Glide.with(this@MainActivity).load(animal.image_link)
                            .into(imageOfAnimal)
                    }
                }
            }

            override fun onFailure(call: Call<Animal>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        }
        )
    }

    private fun getColoredSpanned(text: String, color: Int): String {
        return "<font color=$color>$text</font>"
    }
}