package com.example.mathapp.presentation.home

import android.app.Application
import android.os.Build
import android.widget.Toast
import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val application: Application,
    private val database: FirebaseDatabase
) : ViewModel() {

    private val _appDetails = MutableStateFlow(AppDetails())
    val appDetails = _appDetails.asStateFlow()
    val currentVersionCode: Long by lazy { getAppVersionCode() }

    val updateAvailable: StateFlow<Boolean> = _appDetails.map {
        currentVersionCode < it.versionCode
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        observeAppDetails()
    }

    private fun getAppVersionCode(): Long {
        val packageInfo = application.packageManager.getPackageInfo(application.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }

    private fun observeAppDetails() {
        viewModelScope.launch {
            val appRef = database.reference.child("AppDetails")
            appRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(AppDetails::class.java)?.let {
                        _appDetails.value = it
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast
                        .makeText(application, error.message, Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
    }
}

data class AppDetails(
    val versionCode: Long = 1L,
    val versionName: String = "",
    val urlToDownload: String = "",
    val sourceCode: String = "",
    val madeBy: String = ""
)