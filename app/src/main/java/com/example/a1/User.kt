package com.example.a1
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.json.JSONException

data class User(
    val userId: String = "",
    var email: String = "",
    val password: String = "", // Note: Actual password should not be stored; this is for illustration only.
    var name: String = "",
    var contactNumber: String = "",
    val isMentor: Boolean = false,
    var profileImageUrl: String = "",
    val coverImageUrl: String = "",
    val bio: String = "",
    val expertise: List<String> = listOf(),
    var location: String = "",
    val education: String = "",
    val favorites: List<String> = listOf(), // IDs of favorite mentors
    val reviewsGiven: ArrayList<Review> = ArrayList(),
    val sessionsBooked: ArrayList<Session> = ArrayList(),
    val lastOnlineTimestamp: Long = System.currentTimeMillis(),
    var Notifications: ArrayList<String> = ArrayList()
)


data class Review(
    val userId: String = "",
    val mentorId: String = "",
    val targetUserName: String = "", // Mentor or mentee ID
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Session(
    val mentorId: String = "",
    val menteeId: String = "",
    val scheduledTime: String = "",
    val status: String = "scheduled" // "scheduled", "inProgress", "completed", "cancelled"
)

data class Mentor(
    val mentorId: String = "",
    var name: String = "",
    var mentorDesc: String = "",
    var mentorStatus: String = "",
    var category: String = "Entrepreneurship",
    var contactNumber: String = "",
    var profileImageUrl: String = "",
    var role: String = "",
    var cost: String = "",
    var Rating: String = "",
    val sessionsBooked: ArrayList<Session> = ArrayList(),
    val reviewsReceived: ArrayList<Review> = ArrayList(),
)


data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val message: String = "",
    val timestamp: String = ""
)
data class Chat(
    val chatId: String = "GlobalChat",
    var messages: ArrayList<Message> = ArrayList()
)


object ChatManager {
    private val db: FirebaseFirestore = FirestoreReference.db
    var chat: Chat ?= null
    var focusedMessage: Message ?= null
    /**
     * Adds a new chat to the Firestore database.
     *
     * @param chat The chat to add.
     */
    fun addChat(chat: Chat) {
        db.collection("chats").document(chat.chatId).set(chat)
    }

    /**
     * Retrieves all chats from the Firestore database.
     *
     * @return A list of chats.
     */

    fun getallchats(callback: (ArrayList<Chat>) -> Unit){
        db.collection("chats").get().addOnSuccessListener { result ->
            val chatList = ArrayList<Chat>()

            for (document in result) {
                val chat = document.toObject<Chat>()
                chatList.add(chat)
            }
            callback(chatList)

        }
    }




    fun updateChat() {
        db.collection("chats").document("GlobalChat").set(chat!!)
        sendChatMessages(DataManager.context!!,"GlobalChat",chat!!.messages)
    }

    fun getAllMessages(context: Context, callback: (ArrayList<Message>?) -> Unit) {
        val url = "http://192.168.18.10/getAllMessages.php"

        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    val gson = Gson()
                    val messagesType = object : TypeToken<ArrayList<Message>>() {}.type
                    val messages = gson.fromJson<ArrayList<Message>>(response, messagesType)
                    callback(messages)
                } catch (e: JSONException) {
                    Log.e("JSON Exception", "Error parsing JSON: ${e.message}")
                    callback(null)
                }
            },
            { error ->
                Log.e("Volley Error", "Error occurred: ${error.message}")
                callback(null)
            })

        requestQueue.add(stringRequest)
    }

    fun sendChatMessages(context: Context, chatId: String, messages: ArrayList<Message>) {

        val url = "http://192.168.18.10/updateMessages.php"
        val gson = Gson()

        // Convert messages ArrayList to JSON array
        val messagesJson = gson.toJson(messages.toTypedArray())

        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                // Handle response
                Log.d("Volley", response)
            },
            Response.ErrorListener { error ->
                // Handle error
                Log.e("Volley", "Error occurred: ${error.message}")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                // Add chatId and messages as JSON array
                params["chatId"] = chatId
                params["messages"] = messagesJson
                return params
            }
        }

        requestQueue.add(stringRequest)
    }


}



object FirestoreReference{
    val db: FirebaseFirestore = Firebase.firestore
}
object MentorManager {
    private val db: FirebaseFirestore = FirestoreReference.db
    var searchedMentors: ArrayList<Mentor> = ArrayList()
    var focusedMentor: Mentor? = null
    var lastUpdatedAllMentors: ArrayList<Mentor> = ArrayList()
    /**
     * Adds a new mentor to the Firestore database.
     *
     * @param mentor The mentor to add.
     */
    fun addMentor(mentor: Mentor) {
        db.collection("mentors").document(mentor.mentorId).set(mentor)
        addMentorSQL(DataManager.context!!,mentor)
    }


    fun addMentorSQL(context: Context, mentor: Mentor) {
        val url = "http://192.168.18.10/addMentor.php"
        val gson = Gson()

        // Convert sessions and reviews ArrayLists to arrays
        val sessionsArray = mentor.sessionsBooked.toTypedArray()
        val reviewsArray = mentor.reviewsReceived.toTypedArray()

        // Convert arrays to JSON arrays
        val sessionsJson = gson.toJson(sessionsArray)
        val reviewsJson = gson.toJson(reviewsArray)

        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                // Handle response
                Log.d("Volley", response)
            },
            Response.ErrorListener { error ->
                // Handle error
                Log.e("Volley", "Error occurred: ${error.message}")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                // Add mentor details
                params["mentorId"] = mentor.mentorId
                params["name"] = mentor.name
                params["mentorDesc"] = mentor.mentorDesc
                params["mentorStatus"] = mentor.mentorStatus
                params["category"] = mentor.category
                params["contactNumber"] = mentor.contactNumber
                params["profileImageUrl"] = mentor.profileImageUrl
                params["role"] = mentor.role
                params["cost"] = mentor.cost
                params["Rating"] = mentor.Rating
                // Add sessions and reviews as JSON arrays
                params["sessions"] = sessionsJson
                params["reviews"] = reviewsJson
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    /**
     * Retrieves all mentors from the Firestore database.
     *
     * @return A list of mentors.
     */
    fun getAllMentors(callback: (ArrayList<Mentor>) -> Unit) {
        db.collection("mentors").get().addOnSuccessListener { result ->
            val mentorList = ArrayList<Mentor>()

            for (document in result) {
                val mentor = document.toObject<Mentor>()
                mentorList.add(mentor)
            }
            lastUpdatedAllMentors=mentorList
            callback(mentorList)

        }

    }

    fun updateMentor(mentor: Mentor) {
        db.collection("mentors").document(mentor.mentorId).set(mentor)
    }

}

object  DataManager {
    private val db: FirebaseFirestore = FirestoreReference.db
    var currentUser: User? = null
    var currentFragment: Fragment = HomeFragment()
    var context : Context ?= null
    var chatlisteneron : Boolean =false
    var chatNotifListener : Boolean = false
    /**
     * Adds a new user to the Firestore database.
     *
     * @param user The user to add.
     */
    fun addUser(user: User) {
        getAllUsers { users ->
            var userExists = false
            for (existingUser in users) {
                if (existingUser.name == user.name) {
                    userExists = true
                    break
                }
            }
            if (!userExists) {
                db.collection("users").document(user.userId).set(user)
            }
        }
    }
    fun sendUserSQL(context: Context, user: User) {
        val url = "http://192.168.18.10/addUser.php"
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = @SuppressLint("StaticFieldLeak")
        object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                // Handle response
                Log.d("Volley", response)
            },
            Response.ErrorListener { error ->
                // Handle error
                Log.e("Volley", "Error occurred: ${error.message}")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = user.userId
                params["email"] = user.email
                params["password"] = user.password // Ensure this is hashed appropriately before sending
                params["name"] = user.name
                params["contactNumber"] = user.contactNumber
                params["isMentor"] = user.isMentor.toString()
                params["profileImageUrl"] = user.profileImageUrl
                params["coverImageUrl"] = user.coverImageUrl
                params["bio"] = user.bio
                params["location"] = user.location
                params["education"] = user.education
                params["lastOnlineTimestamp"] = user.lastOnlineTimestamp.toString()
                params["expertise"] = Gson().toJson(user.expertise.toTypedArray())
                params["favorites"] = Gson().toJson(user.favorites.toTypedArray())
                params["reviews"] = Gson().toJson(user.reviewsGiven.toTypedArray())
                params["sessions"] = Gson().toJson(user.sessionsBooked.toTypedArray())
                params["notifications"] = Gson().toJson(user.Notifications.toTypedArray())
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    /**
     * Retrieves all users from the Firestore database.
     *
     * @return A list of users.
     */
    fun getAllUsers(callback: (ArrayList<User>) -> Unit) {
        db.collection("users").get().addOnSuccessListener { result ->
            val userList = ArrayList<User>()
            for (document in result) {
                val user = document.toObject<User>()
                userList.add(user)
            }
            callback(userList)
        }
    }

    fun updateUser(context: Context,user: User) {
        db.collection("users").document(user.userId).set(user)
        sendUpdatedUser(context,user)
    }


    fun sendUpdatedUser(context: Context, user: User) {
        val url = "http://192.168.18.10/updateUser.php"
        val gson = Gson()

        // Convert ArrayLists to arrays
        val expertiseArray = user.expertise.toTypedArray()
        val favoritesArray = user.favorites.toTypedArray()
        val reviewsArray = user.reviewsGiven.toTypedArray()
        val sessionsArray = user.sessionsBooked.toTypedArray()
        val notificationsArray = user.Notifications.toTypedArray()

        // Create a mutable map to hold the parameters
        val params = HashMap<String, String>()
        params["userId"] = user.userId
        params["email"] = user.email
        params["password"] = user.password
        params["name"] = user.name
        params["contactNumber"] = user.contactNumber
        params["isMentor"] = user.isMentor.toString()
        params["profileImageUrl"] = user.profileImageUrl
        params["coverImageUrl"] = user.coverImageUrl
        params["bio"] = user.bio
        params["location"] = user.location
        params["education"] = user.education
        params["lastOnlineTimestamp"] = user.lastOnlineTimestamp.toString()
        params["expertise"] = gson.toJson(expertiseArray)
        params["favorites"] = gson.toJson(favoritesArray)
        params["reviews"] = gson.toJson(reviewsArray)
        params["sessions"] = gson.toJson(sessionsArray)
        params["notifications"] = gson.toJson(notificationsArray)

        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener<String> { response ->
                // Handle response
                Log.d("Volley", response)
            },
            Response.ErrorListener { error ->
                // Handle error
                Log.e("Volley", "Error occurred: ${error.message}")
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return params
            }
        }

        requestQueue.add(stringRequest)
    }




}