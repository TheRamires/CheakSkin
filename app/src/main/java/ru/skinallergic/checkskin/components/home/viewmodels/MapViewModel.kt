package ru.skinallergic.checkskin.components.home.viewmodels

import com.google.android.gms.maps.GoogleMap
import ru.skinallergic.checkskin.components.home.data.MyGoogleMap
import javax.inject.Inject

class MapViewModel @Inject constructor(val myGoogleMap :MyGoogleMap): BaseHomeViewModel() {

    fun mapInit(mMap: GoogleMap?,i0:Int, i1:Int ,i2:Int, i3:Int) {
        myGoogleMap.googlemapsInit(mMap,i0,i1,i2,i3)
    }
    fun mapInit(mMap: GoogleMap?, layoutHeight: Int) {
        myGoogleMap.googlemapsInit(mMap,0,0,0,layoutHeight)
    }
}