package pe.edu.idat.apppokedex.retrofit.response

data class PokemonResponse(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<Pokemon>
)
