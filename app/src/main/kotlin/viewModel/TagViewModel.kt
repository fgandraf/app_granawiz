package viewModel

import androidx.compose.runtime.derivedStateOf
import service.TagService

class TagViewModel {

    val service = TagService()

    var tags = derivedStateOf { service.tags }

    init {
        service.loadTags()
    }
}