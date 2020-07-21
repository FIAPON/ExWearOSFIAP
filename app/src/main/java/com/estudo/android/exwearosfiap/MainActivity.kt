package com.estudo.android.exwearosfiap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.widget.WearableLinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    private var connectivityManager: ConnectivityManager? = null

    val request: NetworkRequest = NetworkRequest.Builder().run {
        addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
        addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Enables Always-on
        setAmbientEnabled()

        wearableRecyclerView.apply {
            layoutManager = WearableLinearLayoutManager(this@MainActivity)

            val menuItems: ArrayList<MenuItem> = ArrayList()
            menuItems.add(MenuItem("Timer"))
            menuItems.add(MenuItem("Confirmação de Sucesso"))
            menuItems.add(MenuItem("Confirmação de Falha"))
            menuItems.add(MenuItem("Confirmação On Phone"))
            menuItems.add(MenuItem("Minha Posição"))
            menuItems.add(MenuItem("Entrada de Áudio"))
            menuItems.add(MenuItem("Chamada HTTP"))

            setAdapter(
                MainMenuAdapter(this@MainActivity, menuItems, object : MainMenuAdapter.AdapterCallback {
                    override fun onItemClicked(menuPosition: Int?) {
                        when (menuPosition) {
                            0 -> timer()
                            1 -> successConfirmation()
                            2 -> failureConfirmation()
                            3 -> openOnPhoneConfirmation()
                            4 -> myPosition()
                            6 -> httpRequest()
                        }
                    }
                }))
        }
    }

    private fun httpRequest() {
        Thread.sleep(10000)
        connectivityManager?.requestNetwork(request, networkCallback)
    }

    private fun getQueue(): RequestQueue{
        return Volley.newRequestQueue(this)
    }

    val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            val url = "https://app.fakejson.com/q/Lpg0g0V6?token=9s0m5BF14JecTED1KL7rrQ"

            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->
                    successConfirmation(extra = response.getString("user_name"))
                },
                Response.ErrorListener { error ->
                    failureConfirmation(extra = "Problema na requisição")
                }
            )

            getQueue().add(jsonObjectRequest)
        }

        override fun onUnavailable() {
            super.onUnavailable()
        }
    }

    fun myPosition(){
        if (hasGps()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    10
                )
            } else {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {location ->
                        val address = Geocoder(this).getFromLocation(location.latitude, location.longitude, 1).get(0)
                        Toast.makeText(this, address.getAddressLine(0), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun hasGps(): Boolean = packageManager.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)

    fun timer(){
        val intent = Intent(this, ConfirmScreenActivity::class.java);
        startActivity(intent)
    }

    fun showConfirmation(extra: String = "Mensagem Extra", type: Int){
        val intent = Intent(this, ConfirmationActivity::class.java).apply {
            putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, type)
            putExtra(ConfirmationActivity.EXTRA_MESSAGE, extra)
        }
        startActivity(intent)
    }

    fun successConfirmation(extra: String = "Mensagem Extra"){
        showConfirmation(extra, ConfirmationActivity.SUCCESS_ANIMATION)
    }

    fun failureConfirmation(extra: String = "Mensagem Extra"){
        showConfirmation(extra, ConfirmationActivity.FAILURE_ANIMATION)
    }

    fun openOnPhoneConfirmation(extra: String = "Mensagem Extra"){
        showConfirmation(extra, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION)
    }
}
