package com.example.mycep.network
import com.example.mycep.Cep
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface ViaCepService {
    @GET("{cep}/json/")
    fun buscarCep(@Path("cep") cep: String): Call<Cep>
}