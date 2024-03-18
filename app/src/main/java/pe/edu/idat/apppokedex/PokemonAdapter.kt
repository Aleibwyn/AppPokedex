package pe.edu.idat.apppokedex

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pe.edu.idat.apppokedex.databinding.ItemPokemonBinding
import pe.edu.idat.apppokedex.retrofit.response.Pokemon

class PokemonAdapter: RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {
    inner class ViewHolder (val binding: ItemPokemonBinding): RecyclerView.ViewHolder(binding.root)

    private var lista= ArrayList<Pokemon>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            with(lista[position]){
                tvNomPokemon.text = name
                //val arrayUrl = url.split("/")
                Glide.with(holder.itemView)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${position+1}.png")
                    .into(ivPokemon)
            }
        }
    }

    fun agregarPokemones(nuevosPokemones: List<Pokemon>) {
        lista.addAll(nuevosPokemones)
        notifyDataSetChanged()
    }
}