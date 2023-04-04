package tj.navin.test

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import tj.navin.test.databinding.FragmentGetOneBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GetOneFragment : Fragment() {

    private var _binding: FragmentGetOneBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentGetOneBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getButton.setOnClickListener {
            if (binding.idEditText.text.isEmpty()) {
                Toast.makeText(activity, "Поле Id обязательное", Toast.LENGTH_SHORT).show()
            } else {
                getOneUser()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getOneUser() {

        binding.resultText.text = "Загрузка..."

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gorest.co.in")
            .build()

        val service = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.getOneUser(binding.idEditText.text.toString())

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

                    val obj = JSONObject(prettyJson)

                    binding.idText.text = "Id: " + obj.getString("id")
                    binding.nameText.text = "Имя: " +obj.getString("name")
                    binding.emailText.text = "Эл почта: " + obj.getString("email")
                    binding.genderText.text = "Пол: " + obj.getString("gender")
                    binding.statusText.text = "Статус: " + obj.getString("status")
                    binding.resultText.text = ""
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())
                    binding.resultText.text = response.code().toString()

                }
            }
        }
    }
}