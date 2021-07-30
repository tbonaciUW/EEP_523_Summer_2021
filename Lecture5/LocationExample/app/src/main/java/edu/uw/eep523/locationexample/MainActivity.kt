package edu.uw.eep523.locationexample

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_INDEFINITE
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*



/**
 * Demonstrates use of the Location API to retrieve the last known location for a device.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Visible while the address is being fetched.
     */
    private lateinit var progressBar: ProgressBar
    private val TAG = "MainActivity"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private var addressRequested = false
    /**
     * Represents a geographical location.
     */
    private var lastLocation: Location? = null
    /**
     * The formatted location address.
     */
    private var addressOutput = ""
    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var latitudeText: TextView
    private lateinit var longitudeText: TextView

    private lateinit var geocoder : Geocoder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress_bar)
        latitudeText = findViewById(R.id.latitude_text)
        longitudeText = findViewById(R.id.longitude_text)
        progressBar = findViewById(R.id.progress_bar)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geocoder = Geocoder(this, Locale.getDefault())
        updateUIWidgets()
    }
    private fun updateUIWidgets() {
        if (addressRequested) {
            progressBar.visibility = ProgressBar.VISIBLE

        } else {
            progressBar.visibility = ProgressBar.GONE

        }
    }


    override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()

        }
    }

    /////////////////////////////////////////////////////////////////////////
    //       GETTING THE LOCATION IN LONGITUDE-LATITUDE VALUES
    //////////////////////////////////////////////////////////////////////
    //Function called when the user clicks on the location button
    fun locationButton(v:View){
        getLastLocation()
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     *
     * Note: this method should be called after location permission has been granted.
     */
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnCompleteListener { taskLocation ->
                if (taskLocation.isSuccessful && taskLocation.result != null) {
                    lastLocation = taskLocation.result
                    latitudeText.text = resources.getString(R.string.latitude_label) + ": "+lastLocation?.latitude.toString()
                    longitudeText.text = resources.getString(R.string.longitude_label) + ": " +lastLocation?.longitude
                } else {
                    showSnackbar(R.string.no_location_detected)
                }
            }
    }


    /////////////////////////////////////////////////////////////////////////
    //       GETTING THE LOCATION ADDRESS
    //////////////////////////////////////////////////////////////////////
    //This funciton is called when the user clics on the adress button
    fun handleButtonAddress(v:View){
        addressRequested = true
        updateUIWidgets()
        getAddress()
    }

    /**
     * Gets the address for the last known location.
     */
    private fun getAddress() {
        var addresses: List<Address> = emptyList()

        if (addressRequested)
        // Address found using the Geocoder.

            if (lastLocation == null) {
                return
            }
            // If the user pressed the fetch address button before we had the location,
            // this will be set to true indicating that we should kick off the intent
            // service after fetching the location.

                try {
                    // Using getFromLocation() returns an array of Addresses for the area immediately
                    // surrounding the given latitude and longitude. The results are a best guess and are
                    // not guaranteed to be accurate.
                    addresses = geocoder.getFromLocation(
                        lastLocation!!.latitude,
                        lastLocation!!.longitude,
                        // In this sample, we get just a single address.
                        1)
                } catch (ioException: IOException) {
                    // Catch network or other I/O problems.
                } catch (illegalArgumentException: IllegalArgumentException) {
                    // Catch invalid latitude or longitude values.
                }

                // Handle case where no address was found.
                if (addresses.isEmpty()) {

                } else {
                    val address = addresses[0]
                    // Fetch the address lines using {@code getAddressLine},
                    // join them, and send them to the thread. The {@link android.location.address}
                    // class provides other options for fetching address details that you may prefer
                    // to use. Here are some examples:
                    // getLocality() ("Mountain View", for example)
                    // getAdminArea() ("CA", for example)
                    // getPostalCode() ("94043", for example)
                    // getCountryCode() ("US", for example)
                    // getCountryName() ("United States", for example)
                    val addressFragments = with(address) {
                        (0..maxAddressLineIndex).map { getAddressLine(it) }
                    }

                    addressOutput =  addressFragments.joinToString(separator = "\n")
                    text_adress.text = addressOutput
                    addressRequested = false
                    updateUIWidgets()
                }
       }


    /////////////////////////////////////////////////////////////////////////
    //    FUNCTIONS FOR CHECKING AND ASKING FOR PERMISSIONS
    //////////////////////////////////////////////////////////////////////
    /**
     * Return the current state of the permissions needed.
     */
    private fun checkPermissions() =
        ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    private fun requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION)) {
            // Provide an additional rationale to the user. This would happen if the user denied the
            // request previously, but didn't check the "Don't ask again" checkbox.
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar(R.string.permission_rationale, android.R.string.ok, View.OnClickListener {
                // Request permission
                startLocationPermissionRequest()
            })

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                grantResults.isEmpty() -> Log.i(TAG, "User interaction was cancelled.")

                // Permission granted.
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) ->{
                    getAddress()
                    getLastLocation()
                }

                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                else -> {
                    showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent().apply {
                                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                data = Uri.fromParts("package", packageName, null)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }
                            startActivity(intent)
                        })
                }
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////
    //       SHOW A SNACK BAR
    //////////////////////////////////////////////////////////////////////

    /**
     * Shows a [Snackbar].
     *
     * @param snackStrId The id for the string resource for the Snackbar text.
     * @param actionStrId The text of the action item.
     * @param listener The listener associated with the Snackbar action.
     */
    private fun showSnackbar(
        snackStrId: Int,
        actionStrId: Int = 0,
        listener: View.OnClickListener? = null
    ) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), getString(snackStrId),
            LENGTH_INDEFINITE)
        if (actionStrId != 0 && listener != null) {
            snackbar.setAction(getString(actionStrId), listener)
        }
        snackbar.show()
    }
}
