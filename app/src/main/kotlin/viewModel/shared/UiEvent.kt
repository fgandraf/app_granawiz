package viewModel.shared

sealed class UiEvent {
    data class Error(val message: String) : UiEvent()
}
