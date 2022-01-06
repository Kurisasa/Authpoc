package com.superbalist.authpoc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.shobhitpuri.custombuttons.GoogleSignInButton
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleResult
import com.willowtreeapps.signinwithapplebutton.view.SignInWithAppleButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signInWithAppleButtonBlack: SignInWithAppleButton =
            findViewById(R.id.sign_in_with_apple_button_black)
        val signInWithAppleButtonWhite: SignInWithAppleButton =
            findViewById(R.id.sign_in_with_apple_button_white)
        val signInWithAppleButtonWhiteOutline: SignInWithAppleButton =
            findViewById(R.id.sign_in_with_apple_button_white_outline)

        val googleSignIn: GoogleSignInButton = findViewById(R.id.textView5)

        // Replace clientId and redirectUri with your own values.
        val configuration = SignInWithAppleConfiguration(
            clientId = "com.superbalist.authpoc",
            redirectUri = "https://example-app.com/redirect",
            scope = "kurisani.chauke@superbalist.com”"
        )

        val callback: (SignInWithAppleResult) -> Unit = { result ->
            when (result) {
                is SignInWithAppleResult.Success -> {
                    Log.d(
                        TAG_KOTLIN,
                        "onCreate: " + result.authorizationCode
                    )
                    Toast.makeText(
                        this,
                        "authorizationCode : ${result.authorizationCode}   \n\n    \n" ,
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(TAG_KOTLIN, "Optional user details (JSON): ${result.authorizationCode}")
                }
                is SignInWithAppleResult.Failure -> {
                    Log.d(TAG_KOTLIN, "Received error from Apple Sign In ${result.error.message}")
                }
                is SignInWithAppleResult.Cancel -> {
                    Log.d(TAG_KOTLIN, "User canceled Apple Sign In")
                }
            }
        }

        googleSignIn.setOnClickListener {
            GoogleSignIn()
        }

        signInWithAppleButtonBlack.setUpSignInWithAppleOnClick(
            supportFragmentManager,
            configuration,
            callback
        )
        signInWithAppleButtonWhite.setUpSignInWithAppleOnClick(
            supportFragmentManager,
            configuration,
            callback
        )
        signInWithAppleButtonWhiteOutline.setUpSignInWithAppleOnClick(
            supportFragmentManager,
            configuration,
            callback
        )
    }

    //region User Google Sign-in and sign-out Code

    fun getGoogleSinginClient() : GoogleSignInClient {
        /**
         * Configure sign-in to request the user's ID, email address, and basic
         * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         */
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()


        /**
         * Build a GoogleSignInClient with the options specified by gso.
         */
        return com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(this, gso);
    }

    private fun GoogleSignIn(){

        if (!isUserSignedIn()){
            val signInIntent = getGoogleSinginClient().signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } else {
            toast("User already signed-in")
        }
    }

    private fun isUserSignedIn(): Boolean {
        val account = com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(this)
        return account != null
    }

    private fun signout() {
        if (isUserSignedIn()){
            getGoogleSinginClient().signOut().addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, " Signed out ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, " Error ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            handleSignData(data)
        }
    }

    private fun handleSignData(data: Intent?) {
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        com.google.android.gms.auth.api.signin.GoogleSignIn.getSignedInAccountFromIntent(data)
            .addOnCompleteListener {
                "isSuccessful ${it.isSuccessful}".print()
                if (it.isSuccessful){
                    // user successfully logged-in
                    "account ${it.result?.account}".print()
                    "displayName ${it.result?.displayName}".print()
                    "Email ${it.result?.email}".print()
                } else {
                    // authentication failed
                    "exception ${it.exception}".print()
                }
            }
    }

    //endregion
    companion object{
        const val RC_SIGN_IN = 0
        const val TAG_KOTLIN = "TAG_KOTLIN"
    }
}

fun Any.print(){
    Log.v(MainActivity.TAG_KOTLIN, " $this")
}

fun Context.toast(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}