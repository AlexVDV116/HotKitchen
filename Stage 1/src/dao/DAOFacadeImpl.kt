package hotkitchen.dao

import hotkitchen.dao.DatabaseFactory.dbQuery
import hotkitchen.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl : DAOFacade {
    // Helper function that builds a User from the resultRow
    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        email = row[Users.email],
        userType = row[Users.userType],
        password = row[Users.password]
    )

    // Table.selectAll returns an instance of Query, so to get the list of User instances we need
    // to manually extract data for each row and convert it to our data class. We accomplish that
    // using the helper function resultRowToArticle that builds an Article from the ResultRow.
    override suspend fun allUsers(): List<User> = dbQuery {
        Users.selectAll().map(::resultRowToUser)
    }

    // The select function takes an extension lambda as an argument. The implicit receiver inside
    // this lambda is of type SqlExpressionBuilder. You don't use this type explicitly, but it
    // defines a bunch of useful operations on columns, which you use to build your queries. You
    // can use comparisons (eq, less, greater), arithmetic operations (plus, times), check whether
    // value belongs or doesn't belong to a provided list of values (inList, notInList), check
    // whether the value is null or non-null, and many more.
    //
    // select returns a list of Query values. As before, we convert them to articles. In our case
    // it should be one user, so we return it as a result.
    override suspend fun user(id: Int): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    // Inside this lambda, we specify which value is supposed to be set for which column. The 'it'
    // argument has a type InsertStatement on which we can call the set operator taking column
    // and value as arguments.
    override suspend fun addUser(email: String, userType: String, password: String): User? = dbQuery {
        val notExistsUser = Users.select { Users.email eq email }.empty()
        if (notExistsUser) {
            val insertStatement = Users.insert {
                it[Users.email] = email
                it[Users.userType] = userType
                it[Users.password] = password
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
        } else {
            null
        }
    }

    // To update the existing article, the Table.update is used:
    override suspend fun editUser(id: Int, email: String, userType: String, password: String): Boolean = dbQuery {
        Users.update({ Users.id eq id }) {
            it[Users.email] = email
            it[Users.userType] = userType
            it[Users.password] = password
        } > 0
    }

    // Finally, use Table.deleteWhere to remove an article from the database:
    override suspend fun deleteUser(id: Int): Boolean = dbQuery {
        Users.deleteWhere { Users.id eq id } > 0
    }

    // Grab the user that matches the email from the db, if the password match return true
    override suspend fun signinUser(email: String, password: String): Boolean {
        val user = dbQuery {
            Users
                .select { Users.email eq email}
                .map(::resultRowToUser)
                .singleOrNull()
        }
        return user?.password == password
    }
}

// Let's create an instance of DAOFacade and add a some sample data into be inserted to the
// database before the application is started.
val daoFacadeImpl: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allUsers().isEmpty()) {
            addUser("john@domain.com", "user", "baddpassword")
            addUser("mike@domain.com", "user", "qwerty")
            addUser("lisa@domain.com", "user", "password123")
            addUser("alex@domain.com", "admin", "123456789")
        }
    }
}