package ru.skinallergic.checkskin.components.home.viewmodels

import androidx.lifecycle.ViewModel
import ru.skinallergic.checkskin.components.home.repositories.ReviewsRepository
import javax.inject.Inject

class ReviesViewModel @Inject constructor(val reviewsRepository: ReviewsRepository): BaseHomeViewModel(){
}