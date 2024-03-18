package com.example.a1
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

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
    }

}


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

    fun updateUser(user: User) {
        db.collection("users").document(user.userId).set(user)
    }
}