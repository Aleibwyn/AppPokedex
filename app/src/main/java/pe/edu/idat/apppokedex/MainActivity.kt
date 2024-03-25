package pe.edu.idat.apppokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pe.edu.idat.apppokedex.databinding.ActivityMainBinding
import pe.edu.idat.apppokedex.retrofit.PokemonApiService
import pe.edu.idat.apppokedex.retrofit.response.PokemonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var apiRetrofit: Retrofit
    private lateinit var pokemonAdapter: PokemonAdapter

    private var offset = 0
    private val limit = 20

    private var puedeCargar = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        apiRetrofit = Retrofit
            .Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokemonAdapter = PokemonAdapter()

        binding.rvPokemon.layoutManager = GridLayoutManager(applicationContext, 3)
        binding.rvPokemon.adapter = pokemonAdapter
        binding.rvPokemon.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(dy > 0) {
                    val itemsVisible = binding.rvPokemon.layoutManager!!.childCount
                    val itemTotales = binding.rvPokemon.layoutManager!!.itemCount
                    val primerItemVisible = (binding.rvPokemon.layoutManager!! as GridLayoutManager)
                        .findFirstVisibleItemPosition()

                    if (puedeCargar) {
                        if(itemsVisible + primerItemVisible >= itemTotales) {
                            Log.i("Item Visible", primerItemVisible.toString())
                            puedeCargar = false
                            offset += 20
                            obtenerPokemonsRetrofit()
                        }
                    }
                }
            }
        })
        obtenerPokemonsRetrofit()
    }

    private fun obtenerPokemonsRetrofit() {
        //Creando el servicio api GET
        var services = apiRetrofit.create(PokemonApiService::class.java)
        var pokemonResponse = services.obtenerPokemones(offset, limit)
        pokemonResponse.enqueue(object : Callback<PokemonResponse>{
            override fun onResponse(
                call: Call<PokemonResponse>,
                response: Response<PokemonResponse>
            ) {
                puedeCargar = true
                pokemonAdapter.agregarPokemones(response.body()!!.results)
            }

            override fun onFailure(call: Call<PokemonResponse>, t: Throwable) {

            }
        })
    }
}