package sources.anime

interface AnimeSource {
    fun search(query: String): List<Anime>;
}