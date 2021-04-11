package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting
import java.util.*

object UserHolder {
    private val map = mutableMapOf<String, User>()
    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User {
        if (!map.containsKey(email.toLowerCase(Locale.getDefault()))) {
            return User.makeUser(fullName, email = email, password = password)
            .also { user -> map[user.login] = user }
        } else throw IllegalArgumentException("A user with this email already exists")
    }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        if ("\\+\\d{11}".toRegex().matches(rawPhone.replace("[^+\\d]".toRegex(), ""))) {
            if (!map.containsKey(rawPhone.replace("[^+\\d]".toRegex(), ""))) {
                return User.makeUser(fullName = fullName, phone = rawPhone).also { user -> map[user.login] = user }
            } else throw IllegalArgumentException("A user with this phone already exists")
        } else throw IllegalArgumentException("Enter a valid phone number starting with + and containing 11 digits")
    }

    fun loginUser(login: String, password: String): String? {
        return if(login.contains('@')) {
            map[login.trim()]?.let {
                if (it.checkPassword(password)) it.userInfo
                else null
            }
        } else {
            map[login.replace("[^+\\d]".toRegex(), "")]?.let {
                if (it.checkPassword(password)) it.userInfo
                else null
            }
        }
    }


    fun requestAccessCode(login: String) : Unit {
        val user = map[login.replace("[^+\\d]".toRegex(), "")]
        user?.let {
            return user.requestAccessCode()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}