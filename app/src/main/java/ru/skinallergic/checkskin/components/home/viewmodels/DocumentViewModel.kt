package ru.skinallergic.checkskin.components.home.viewmodels

import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.home.repositories.DocumentRepository
import javax.inject.Inject

class DocumentViewModel @Inject constructor(val documentRepository: DocumentRepository): BaseHomeViewModel() {
}