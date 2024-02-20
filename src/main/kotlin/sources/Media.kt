package sources

import java.net.URI

interface Media {
    val id: String
    val title: List<String>
    val defaultTitle: String
    val coverImage: URI?
    val bannerImage: URI?
    val description: String?
    val informationTags: List<InformationTag>
}