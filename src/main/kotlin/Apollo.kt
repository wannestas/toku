import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import me.wtas.fileLocator.graphql.anilist.SearchQuery
import me.wtas.fileLocator.graphql.anilist.type.MediaType
import sources.anime.anilist.toInternal



suspend fun main() {
    val response = apolloClient.query(SearchQuery(type = Optional.present(MediaType.ANIME), page = Optional.present(20))
    ).execute()


    println(response.data?.Page?.media?.forEach { println(it?.toInternal()) })
}