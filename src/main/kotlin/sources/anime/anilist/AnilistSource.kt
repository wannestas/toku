package sources.anime.anilist

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import me.wtas.toku.graphql.anilist.SearchQuery
import me.wtas.toku.graphql.anilist.type.MediaType
import sources.anime.AnimeSource

class AnilistSource() : AnimeSource {
    private val apolloClient = ApolloClient.Builder()
        .serverUrl("https://graphql.anilist.co")
        .build()
    override suspend fun search(title: String) = apolloClient.query(
        SearchQuery(type = Optional.present(MediaType.ANIME), search = Optional.present(title))
    ).execute().data?.Page?.media?.mapNotNull { it?.toInternal() } ?: emptyList()
}
