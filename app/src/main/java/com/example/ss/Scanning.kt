package com.example.ss
import SavedTextAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Scanning : Fragment(), View.OnClickListener {
    private lateinit var scanBtn: Button
    private var messageText: TextView? = null
    private var messageFormat: TextView? = null
    private lateinit var btnsearch: Button
    private lateinit var savebtn: Button
    private lateinit var sharedPreferences: SharedPreferences
    val database = Firebase.database
    val myRef = database.getReference("History")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scanning, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("scanned_data", Context.MODE_PRIVATE)

        // referencing and initializing
        // the button and text views
        scanBtn = view.findViewById(R.id.scanBtn)
        messageText = view.findViewById(R.id.textContent)
        messageFormat = view.findViewById(R.id.textFormat)
        btnsearch = view.findViewById(R.id.btnsearch)
        savebtn = view.findViewById(R.id.button2)




        // adding listener to the button
        scanBtn.setOnClickListener(this)
        btnsearch.setOnClickListener {
            val scannedText = messageText?.text?.toString()
            if (scannedText != null && isUrl(scannedText)) {
                // If the scanned text is a URL, open it in a browser
                openUrlInBrowser(scannedText)
            } else {
                // If it's not a URL, display a toast
                Toast.makeText(requireContext(), "Not a URL", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for the save button
        savebtn.setOnClickListener {
            val scannedText = messageText?.text?.toString()
            if (scannedText != null && isUrl(scannedText)) {
                saveScannedText(scannedText)
                Toast.makeText(requireContext(), "Text saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "No text to save", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun isUrl(text: String): Boolean {
        // A simple URL validation check
        return text.startsWith("http://") || text.startsWith("https://")
    }

    private fun openUrlInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun saveScannedText(text: String) {
        // Here, you can save the scanned text along with the current date and time
        val dateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val formattedText = "$text - $dateTime"

        if (formattedText.isNotEmpty()) {
            // Push the text to Firebase with a unique key
            val key = myRef.push().key
            if (key != null) {
                myRef.child(key).setValue(formattedText)
                    .addOnSuccessListener {
                        // Optionally, you can show a success message
                        // Toast.makeText(this, "Data saved to Firebase", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        // Handle any errors
                        // Optionally, you can show an error message
                        // Toast.makeText(this, "Error saving data: $it", Toast.LENGTH_SHORT).show()
                    }
            }
    }
    }

    override fun onClick(v: View) {
        val currentOrientation = activity?.requestedOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        // Lock orientation to portrait
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Create IntentIntegrator instance and initiate scan
        val intentIntegrator = IntentIntegrator.forSupportFragment(this)
        intentIntegrator.setPrompt("Scan a barcode or QR Code")
        intentIntegrator.setOrientationLocked(true) // Lock orientation
        intentIntegrator.initiateScan()

        // Restore the previous orientation after scan
        activity?.requestedOrientation = currentOrientation
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                messageText?.text = intentResult.contents
                messageFormat?.text = intentResult.formatName
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
