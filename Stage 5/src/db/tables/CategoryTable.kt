package hotkitchen.db.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object CategoryTable : IntIdTable() {

    val title = varchar("title", 100)
    val description = varchar("description", 100)
}