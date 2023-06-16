package com.bangkit.wastify.ui.screens.explore

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bangkit.wastify.BuildConfig
import com.bangkit.wastify.R
import com.bangkit.wastify.databinding.CustomInfoWindowBinding
import com.bangkit.wastify.databinding.FragmentExploreBinding
import com.bangkit.wastify.utils.Helper.formatOpeningHours
import com.bangkit.wastify.utils.Helper.toast
import com.bangkit.wastify.utils.Helper.vectorToBitmap
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.Place.Field
import com.google.android.libraries.places.api.net.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.pow

@AndroidEntryPoint
class ExploreFragment : Fragment(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Init places client
        Places.initialize(requireContext(), BuildConfig.GOOGLE_MAP_API_KEY)
        placesClient = Places.createClient(requireContext())

        // Init fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInfo.setOnClickListener {
            context?.let { ctx ->
                MaterialAlertDialogBuilder(ctx)
                    .setTitle(getString(R.string.show_my_location))
                    .setMessage(getString(R.string.msg_activate_gps))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        val mapFragment = (childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment)
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isIndoorLevelPickerEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMapToolbarEnabled = true

        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(-2.5489, 118.0149)))

        getMyLocation()
        getWasteDisposalLocation()

        map.setInfoWindowAdapter(this)
        map.setOnMarkerClickListener { marker ->
            val zoom = map.cameraPosition.zoom
            val cu = CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    marker.position.latitude + 150 / 2.0.pow(zoom.toDouble()),
                    marker.position.longitude
                ),
                zoom
            )
            map.animateCamera(cu, 600, null)
            marker.showInfoWindow()
            return@setOnMarkerClickListener true
        }
    }

    override fun getInfoContents(marker: Marker): View? = null

    override fun getInfoWindow(marker: Marker): View {
        val customInfoWindow = CustomInfoWindowBinding.inflate(LayoutInflater.from(requireContext()))
        val place: Place = marker.tag as Place
        customInfoWindow.apply {
            tvName.text = place.name
            tvAddress.text = place.address
            tvHours.text = formatOpeningHours(place.openingHours)
        }
        return customInfoWindow.root
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            } else {
                toast(getString(R.string.msg_error_location_access))
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true

            val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (!gpsEnabled) {
                toast(getString(R.string.msg_turn_on_gps))
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latLng = LatLng(location.latitude, location.longitude)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    }
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getWasteDisposalLocation() {
        // Define your query strings
        val queries = listOf(
            "TPS",
            "TPSS",
            "TPPS",
            "TPAN",
            "LPS",
            "Pembuangan Sampah",
            "Tempat Pembuangan",
            "Bank Sampah",
            "Sampah",
        )

        for (query in queries) {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setCountries("ID")
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    for (prediction in response.autocompletePredictions) {
                        val placeId = prediction.placeId

                        // Retrieve place details using place ID
                        val placeFields = listOf(
                            Field.LAT_LNG,
                            Field.NAME,
                            Field.ADDRESS,
                            Field.OPENING_HOURS
                        )
                        val fetchRequest = FetchPlaceRequest.newInstance(placeId, placeFields)
                        placesClient.fetchPlace(fetchRequest)
                            .addOnSuccessListener { fetchResponse ->
                                val place = fetchResponse.place
                                val placeLatLng = place.latLng

                                // Create a marker options object
                                val markerOptions = placeLatLng?.let {
                                    MarkerOptions()
                                        .position(it)
                                        .icon(vectorToBitmap(R.drawable.location_marker, resources))
                                }

                                // Add the marker to the map
                                if (markerOptions != null) {
                                    map.addMarker(markerOptions)?.tag = place
                                }
                            }
                            .addOnFailureListener { exception ->
                                toast(exception.message.toString())
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    toast(exception.message.toString())
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}