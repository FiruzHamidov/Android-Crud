package tj.navin.test

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import tj.navin.test.databinding.FragmentPut2Binding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PutFragment : Fragment() {

    private var _binding: FragmentPut2Binding? = null
    private var result: Boolean = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPut2Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.postData.setOnClickListener {
            if (result) {
                editUser()
            } else {
                Toast.makeText(activity, "Сначала нужно найти пользователя", Toast.LENGTH_SHORT)
                    .show()
            }
        }

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

    private fun editUser() {

        binding.resultText.text = "Отправка ..."

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gorest.co.in")
            .build()

        val service = retrofit.create(ApiService::class.java)

        val jsonObject = JSONObject()
        jsonObject.put("name", binding.nameEditText.text)
        jsonObject.put("email", binding.emailEditText.text)
        jsonObject.put("gender", binding.genderEditText.text)
        jsonObject.put("status", binding.statusEditText.text)

        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {

            val response = service.updateUser( binding.idEditText.text.toString(), requestBody)

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

                    binding.nameEditText.text = obj.getString("name").toEditable()
                    binding.emailEditText.text = obj.getString("email").toEditable()
                    binding.genderEditText.text = obj.getString("gender").toEditable()
                    binding.statusEditText.text = obj.getString("status").toEditable()

                    binding.resultText.text = prettyJson

                    result = true
                } else {

                    Log.e("RETROFIT_ERROR", response.code().toString())
                    binding.resultText.text = response.code().toString()

                }
            }
        }
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}