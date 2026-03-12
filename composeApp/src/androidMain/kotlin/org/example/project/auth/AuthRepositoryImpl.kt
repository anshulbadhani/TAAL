package org.example.project.auth
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class AuthRepositoryImpl(private val context: Context) : AuthRepository {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("464898087369-kga1alvnsrma2t3n9cadhj38rvcqv928.apps.googleusercontent.com") // Keep your ID here
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    override suspend fun signInWithEmail(email: String, password: String) = runCatching {
        auth.signInWithEmailAndPassword(email, password)
        Unit
    }

    override suspend fun signUp(email: String, password: String) = runCatching {
        val authResult = auth.createUserWithEmailAndPassword(email, password)
        val user = authResult.user
        if (user != null) {
            val userData = mapOf(
                "uid" to user.uid,
                "email" to email,
                "createdAt" to System.currentTimeMillis()
            )
            db.collection("users").document(user.uid).set(userData)
        }
        Unit
    }

    override suspend fun signInWithGoogle(): Result<Unit> {
        return Result.failure(Exception("LAUNCH_GOOGLE_INTENT"))
    }

    override suspend fun firebaseAuthWithGoogle(idToken: String) = runCatching {
        val credential = GoogleAuthProvider.credential(idToken, accessToken = null)
        val authResult = auth.signInWithCredential(credential)
        val user = authResult.user

        if (user != null) {
            val userData = mapOf(
                "uid" to user.uid,
                "email" to (user.email ?: ""),
                "displayName" to (user.displayName ?: ""),
                "lastLogin" to System.currentTimeMillis()
            )
            db.collection("users").document(user.uid).set(userData)
        }
        Unit
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}