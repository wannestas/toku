package sources.anime

interface AnimeSource {
    suspend fun search(title: String): List<Anime>
}