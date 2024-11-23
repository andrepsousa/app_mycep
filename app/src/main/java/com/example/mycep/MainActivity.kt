package com.example.mycep

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycep.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var cepEditText: EditText
    private lateinit var resultadoTextView: TextView
    private lateinit var buscarButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var limparButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cepEditText = findViewById(R.id.etCep)
        buscarButton = findViewById(R.id.btnBuscar)
        resultadoTextView = findViewById(R.id.tvResultado)
        progressBar = findViewById(R.id.progressBar)
        limparButton = findViewById(R.id.btnLimpar)

        buscarButton.setOnClickListener {
            val cep = cepEditText.text.toString().trim()

            val cepRegex = Regex("^[0-9]{5}-?[0-9]{3}$")
            if (!cep.matches(cepRegex)) {
                Toast.makeText(this, "CEP inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = ProgressBar.VISIBLE

            buscarCep(cep)
        }
        limparButton.setOnClickListener {
            cepEditText.text.clear()
            resultadoTextView.text = ""
            progressBar.visibility = ProgressBar.GONE
            limparButton.visibility = Button.GONE
        }
    }

    private fun buscarCep(cep: String) {
        val service = RetrofitClient.instance
        val call = service.buscarCep(cep)

        call.enqueue(object : Callback<Cep> {
            override fun onResponse(call: Call<Cep>, response: Response<Cep>) {
                progressBar.visibility = ProgressBar.GONE
                if (response.isSuccessful) {
                    val cepData = response.body()
                    resultadoTextView.text = """
                        CEP: ${cepData?.cep}
                        Logradouro: ${cepData?.logradouro}
                        Bairro: ${cepData?.bairro}
                        Cidade: ${cepData?.localidade}
                        UF: ${cepData?.uf}
                    """.trimIndent()

                    limparButton.visibility = Button.VISIBLE
                } else {
                    resultadoTextView.text = "Erro: Não foi possível encontrar o CEP."
                    limparButton.visibility = Button.GONE
                }
            }

            override fun onFailure(call: Call<Cep>, t: Throwable) {
                progressBar.visibility = ProgressBar.GONE
                resultadoTextView.text = "Erro: ${t.message}"
                limparButton.visibility = Button.GONE
            }
        })
    }
}
