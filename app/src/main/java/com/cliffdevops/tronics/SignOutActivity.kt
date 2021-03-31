package com.cliffdevops.tronics

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.github.loadingview.LoadingDialog

private const val CAMERA_REQUEST_CODE = 101

class SignOutActivity : AppCompatActivity() {

    lateinit var codeScanner: CodeScanner
    private lateinit var codeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val backBtn: (ImageView) = findViewById(R.id.btnBack)
        codeText = findViewById(R.id.txtCode)

        setupPermission()
        codeScan()

        backBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(0, 0)
        })
    }

    private fun codeScan() {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.CONTINUOUS // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_SHORT).show()
                val codeString: String = it.text
                codeText.text = codeString

                dialogMessage(codeString)

            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(
                    this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
            codeText.text = "Scanning..."

        }

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun setupPermission() {
        val permission: Int = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    private fun dialogMessage( codeValue:String) {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_view, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.show()
        val dialog = LoadingDialog.get(this).show()

        val handler = Handler()
        handler.postDelayed({
            val intent  = Intent(this, ResultsActivity::class.java)
            intent.putExtra("codeValue",codeValue)
            startActivity(intent)
            overridePendingTransition(0, 0)

            mAlertDialog.dismiss()
            dialog?.hide()

        }, 3000)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera Permission is Required", Toast.LENGTH_SHORT).show()
        } else {
            //Success
        }
    }
}