package com.example.coronawatch


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.content.pm.PackageInfo
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import android.util.Base64
import java.util.Arrays
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import android.view.Menu
import android.view.View
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import java.security.MessageDigest
import android.widget.*
import com.example.article.R
import com.example.coronawatch.Login.APIServiceSign
import com.example.coronawatch.Login.AuthUser
import com.example.coronawatch.Login.user
import com.example.coronawatch.Signaler.APIService
import com.example.signaler.Attachment
import com.example.signaler.videoFeed
import com.google.android.gms.auth.api.Auth

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.navigation.NavigationView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class login : AppCompatActivity() {

    private var facebooklogin: Button? = null
    private var facebooklogout: Button? = null
    private var callbackManager: CallbackManager? = null

    private var googlelogin: Button? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private var PRIVATE_MODE = 0
    private var PREF_NAME ="coronawatch"
    var sharedPrefIdUser: SharedPreferences  ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginpage)

      callbackManager = CallbackManager.Factory.create()
        facebooklogin = findViewById(R.id.loginfacebook)
        //Google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_APPLICATION_CLIENT_ID")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        // Login with Facebook
        facebooklogin!!.setOnClickListener {
            // Login
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val accessToken = AccessToken.getCurrentAccessToken()
                        println("token facebook est : "+accessToken)
                        val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
                            println("===================JSON Object" + `object`)
                            //Intializing each parameters avaible in graph API
                            var logged = true
                            var prefsLoggin = PreferenceManager.getDefaultSharedPreferences(this@login)
                            prefsLoggin.edit().putBoolean("IsloginFb", logged).commit()


                            var id = ""
                            var name = ""
                            var email = ""
                            var gender = ""
                            var url = ""
                            try {
                                if (`object`.has("id")) {
                                    id = `object`.getString("id")
                                }
                                if (`object`.has("name")) {
                                    name = `object`.getString("name")
                                }
                                if (`object`.has("email")) {
                                    email = `object`.getString("email")
                                }
                                if (`object`.has("gender")) {
                                    gender = `object`.getString("gender")
                                }
                                if (`object`.has("picture")) {
                                    url = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
                                }
                                val intent = Intent(this@login, MapsActivity::class.java)
                                println("logged in facebook")

                                var bundle = Bundle()
                                bundle.putBoolean("logged",logged)
                                bundle.putString("name", name)
                                bundle.putString("email", email)
                                bundle.putString("gender", gender)
                                bundle.putString("url", url)
                                intent.putExtras(bundle)
                                startActivity(intent)

                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString("fields", "id,name,link,email,picture,gender, birthday")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        Log.d("MainActivity", "Facebook onCancel.")
                    }

                    override fun onError(error: FacebookException) {
                        Log.d("MainActivity", "Facebook onError.")
                    }
                })
        }
        // facebooklogout = findViewById(R.id.logout) as Button
        //Log Out Facebook
        //facebooklogout!!.setOnClickListener {
        //  LoginManager.getInstance().logOut();
        //  Intent login = new Intent(MainActivity.this, LoginActivity.class);
        // startActivity(login);
        //finish();
        //}

        //Shared pref to google sign in

        var prefsGoogleSignin = PreferenceManager.getDefaultSharedPreferences(this)


        // Log In with Google
        googlelogin = findViewById(R.id.logingoogle)
        googlelogin!!.setOnClickListener {
            var gso: GoogleSignInOptions  =  GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
             .requestEmail()
             .build()
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = mGoogleSignInClient.signInIntent
           // prefsGoogleSignin.edit().putStringSet("Islogin", signInIntent).commit()
            startActivityForResult(
                signInIntent, RC_SIGN_IN
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            val isSigned : Boolean = true

            // Signed in successfully
            // save LoginwithGoogle in internal storage
            sharedPrefIdUser = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

            val editor:SharedPreferences.Editor =  sharedPrefIdUser!!.edit()
            editor.putBoolean("login_google",isSigned)

            val googleId = account?.id ?: ""
            Log.i("Google ID",googleId)

            val googleFirstName = account?.givenName ?: ""
            Log.i("Google First Name", googleFirstName)

            val googleLastName = account?.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            val googleEmail = account?.email ?: ""
            Log.i("Google Email", googleEmail)

            val googleProfilePicURL = account?.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account?.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

            // store user infos in shared preferences
            editor.putString("user_name",googleFirstName)
            editor.putString("user_name_last",googleLastName)
            editor.putString("user_email",googleEmail)
            editor.putString("user_profilePic",googleProfilePicURL)

            // save shared preferences
            editor.apply()
            editor.commit()
            // Create a user
            var tokenv = googleIdToken.take(128)
            var userv = user(googleFirstName,googleEmail,tokenv,"final user",googleProfilePicURL)

           SignUp(userv)


            val myIntent = Intent(this, MapsActivity::class.java)


            myIntent.putExtra("google_id", googleId)
            myIntent.putExtra("isSigned", isSigned)
            myIntent.putExtra("google_first_name", googleFirstName)
            myIntent.putExtra("google_last_name", googleLastName)
            myIntent.putExtra("google_email", googleEmail)
            myIntent.putExtra("google_profile_pic_url", googleProfilePicURL)
            myIntent.putExtra("google_id_token", googleIdToken)
            startActivity(myIntent)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

    fun generateHashKey() {
        val info: PackageInfo
        try {
            info = packageManager.getPackageInfo("com.mobiledev.facebooklogin", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun SignUp(userv: user){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

        okHttpClient.addInterceptor(loggingInterceptor).build()
        okHttpClient.retryOnConnectionFailure(true)

        okHttpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Accept", "application/json")
                .addHeader("Connection", "close")
                .build()
            chain.proceed(request)
        }


        val BaseUrl = "http://corona-watch-api.herokuapp.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIServiceSign::class.java)
        // val getResponse = AppConfig.retrofit.create<APIService>(APIService::class.java!!)
        //   val call = getResponse.
        val call = service.SignUp(userv)


        call!!.enqueue(object : Callback<user?> {
            override fun onFailure(call: Call<user?>, t: Throwable) {
                Toast.makeText(this@login,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<user?>, response: Response<user?>) {
                if (response.isSuccessful()) {
                    println(response.body()!!.toString())
                    println("ouii enregistré dans la bdd")
                }else{
                    println("user existe deja")
                    var userauth = AuthUser(10,userv.username,userv.password)
                    SignIn(userauth)
                }
            }
        })
    }

    fun SignIn(userv: AuthUser){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

        okHttpClient.addInterceptor(loggingInterceptor).build()
        okHttpClient.retryOnConnectionFailure(true)

        okHttpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Accept", "application/json")
                .addHeader("Connection", "close")
                .build()
            chain.proceed(request)
        }


        val BaseUrl = "http://corona-watch-api.herokuapp.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIServiceSign::class.java)
        // val getResponse = AppConfig.retrofit.create<APIService>(APIService::class.java!!)
        //   val call = getResponse.
        val call = service.SignIn(userv)


        call!!.enqueue(object : Callback<AuthUser?> {
            override fun onFailure(call: Call<AuthUser?>, t: Throwable) {
                Log.e("hhh", "Unable to submit video feeds to API." + t.message)
                Toast.makeText(this@login,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AuthUser?>, response: Response<AuthUser?>) {
                if (response.isSuccessful()) {
                    println(response.body()!!.toString())

                    println("sign in reussi l'id de cet utilisateur est " +response!!.body()!!.user_id)


                    val id:Int = Integer.parseInt(response!!.body()!!.user_id.toString())
                    val editor:SharedPreferences.Editor =  sharedPrefIdUser!!.edit()
                    editor.putInt("id_user",id)
                    editor.apply()
                    editor.commit()

                }else{
                    Toast.makeText(this@login,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
                    println("prblm sign in")
                }
            }
        })
    }

}
