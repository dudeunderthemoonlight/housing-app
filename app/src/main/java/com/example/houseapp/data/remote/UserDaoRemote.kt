package com.example.houseapp.data.remote

import com.example.houseapp.data.models.User
import com.example.houseapp.utils.DatabaseConnection.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserDaoRemote {
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.userId],
        firstName = row[Users.firstName],
        lastName = row[Users.lastName],
        address = row[Users.address],
        phone = row[Users.phoneNumber]
    )

    suspend fun getAllUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    suspend fun getUserByID(id: String): User = dbQuery {
        Users
            .select(Users.userId eq id)
            .map(::resultRowToUser)
            .first()
    }


    suspend fun addNewUser(user: User): User? = dbQuery {
        val insertStatement = Users.insert {
            it[userId] = user.id
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[address] = user.address
            it[phoneNumber] = user.phone
        }

        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
    }

    suspend fun update(user: User) = dbQuery {
        Users.update({ Users.userId eq user.id }) {
            it[firstName] = user.firstName
            it[lastName] = user.lastName
            it[address] = user.address
            it[phoneNumber] = user.phone
        }
    }

    suspend fun getUserIfAdmin(id: String): User? = dbQuery {
        Users
            .select { Users.userId eq id and(Users.isAdmin eq true) }
            .map(::resultRowToUser)
            .firstOrNull()
    }
}
