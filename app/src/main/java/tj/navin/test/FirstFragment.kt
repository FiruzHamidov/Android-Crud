package tj.navin.test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import tj.navin.test.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            getMethod()
        }

        binding.buttonGetOne.setOnClickListener {
            getOneMethod()
        }

        binding.buttonFirst2.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment2)
        }

        binding.buttonFirst3.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_PutFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMethod() {

        binding.resultText.text = "Загрузка..."

        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gorest.co.in")
            .build()


        val service = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getPosts()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)

                    binding.resultText.text = prettyJson
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())

                    binding.resultText.text = response.code().toString()

                }
            }
        }
    }

    private fun getOneMethod() {

        binding.resultText.text = "Загрузка..."

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gorest.co.in")
            .build()

        val service = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getOneUser()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        JsonParser.parseString(
                            response.body()
                                ?.string()
                        )
                    )
                    Log.d("Pretty Printed JSON :", prettyJson)

                    binding.resultText.text = prettyJson
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())
                    binding.resultText.text = response.code().toString()

                }
            }
        }
    }
}