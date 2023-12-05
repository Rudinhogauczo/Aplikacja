package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.content.ContentValues
import androidx.biometric.BiometricPrompt
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class NoweKonto : AppCompatActivity() {

    private lateinit var executor: Executor
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nowekonto)

        executor = ContextCompat.getMainExecutor(this)

        val dbHelper = Baza(this)

        val spinnerOptions = findViewById<Spinner>(R.id.WyborTypu)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adapter.add("Chcę zredukować wagę")
        adapter.add("Chcę zachować wagę")
        adapter.add("Chcę zwiększyć wagę")

        spinnerOptions.adapter = adapter

        val buttonrezygnacja = findViewById<ImageButton>(R.id.ButtonRezygnacja)

        buttonrezygnacja.setOnClickListener {
            val intent = Intent(this, EkranGlowny::class.java)
            startActivity(intent)
        }

        val buttonAkceptacja = findViewById<ImageButton>(R.id.ButtonAkceptacja)
        buttonAkceptacja.setOnClickListener {
            val imieEditText = findViewById<EditText>(R.id.PodajImie)
            val wzrostEditText = findViewById<EditText>(R.id.PodajWzrost)
            val wagaEditText = findViewById<EditText>(R.id.PodajWagę)
            val typWagiSpinner = findViewById<Spinner>(R.id.WyborTypu)

            val imie = imieEditText.text.toString()
            val wzrost = wzrostEditText.text.toString().toInt()
            val waga = wagaEditText.text.toString().toInt()
            val typWagi = typWagiSpinner.selectedItem.toString()

            val contentValues = ContentValues()
            contentValues.put("imie", imie)
            contentValues.put("wzrost", wzrost)
            contentValues.put("waga", waga)
            contentValues.put("typ_wagi", typWagi)

            val db = dbHelper.writableDatabase
            db.insert("Konto", null, contentValues)

            val intent = Intent(this, EkranGlowny::class.java)
            startActivity(intent)
            finish()
            if (isFingerprintAvailable()) {
                showBiometricPrompt()
            } else {
                // Obsługa, gdy linie papilarne nie są dostępne
                // Kontynuuj zapis do bazy danych lub poinformuj użytkownika
            }
        }

    }


    private fun isFingerprintAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)

        val result = when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
            else -> false
        }
        if (!result) {
            // Wyświetlenie informacji Toast, jeśli czytnik nie jest dostępny
            Toast.makeText(this, "Czytnik linii papilarnych niedostępny.", Toast.LENGTH_SHORT).show()
        }

        return result
    }
        private fun showBiometricPrompt() {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Tytuł")
                .setSubtitle("Podtytuł")
                .setDescription("Opis")
                .setNegativeButtonText("Anuluj")
                .build()

            val biometricPrompt =
                BiometricPrompt(
                    this,
                    executor,

                    @RequiresApi(Build.VERSION_CODES.P)
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            // Obsługa błędów uwierzytelniania
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            // Uwierzytelnienie zakończone sukcesem - zapisz dane do bazy danych
                            saveUserDataToDatabase()
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            // Obsługa nieudanego uwierzytelnienia
                        }
                    })

            biometricPrompt.authenticate(promptInfo)
        }

        fun saveUserDataToDatabase() {
            // Kod zapisujący dane użytkownika do bazy danych
        }
    }

