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
    val experience: List<Experience> = listOf(),
    val favorites: List<String> = listOf(), // IDs of favorite mentors
    val reviewsGiven: List<Review> = listOf(),
    val reviewsReceived: List<Review> = listOf(),
    val availability: Map<String, Boolean> = mapOf(), // Days of the week or time slots available for mentoring
    val sessionsBooked: List<Session> = listOf(),
    val chats: List<Chat> = listOf(),
    val notificationsEnabled: Boolean = true,
    val lastOnlineTimestamp: Long = System.currentTimeMillis(),
    val roles: List<String> = listOf("user") // Additional roles can be "admin", "mentor", etc.
)

data class Experience(
    val title: String = "",
    val company: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val description: String = ""
)

data class Review(
    val reviewerId: String = "",
    val targetUserId: String = "", // Mentor or mentee ID
    val rating: Float = 0f,
    val comment: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class Session(
    val sessionId: String = "",
    val mentorId: String = "",
    val menteeId: String = "",
    val scheduledTime: Long = System.currentTimeMillis(),
    val topic: String = "",
    val status: String = "scheduled" // Could be "completed", "cancelled", etc.
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
    val reviewsReceived: List<Review> = listOf(),
)
data class Chat(
    val chatId: String = "",
    val userIds: List<String> = listOf(), // Participants in the chat
    val lastMessage: String = "",
    val lastMessageTimestamp: Long = System.currentTimeMillis()
)

object FirestoreReference{
    val db: FirebaseFirestore = Firebase.firestore
}
object MentorManager {
    private val db: FirebaseFirestore = FirestoreReference.db
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