
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.cbellmont.neoland.datamodel.bootcamp.Bootcamp
import com.google.gson.annotations.SerializedName

data class User(
    @Embedded
    var name: Name,
    var gender: String,
    var email: String,
    @Embedded
    var picture: Picture,
) {
    var userId = 0
    var fkBootcampId : Int? = null

    fun getCompleteName() : String {
        return String.format("%s %s", name.name, name.surname)
    }

    fun getPhotoUrl() : String {
        return picture.large
    }

    fun getEmailEnString(): String {
        return email
    }
}

data class Name (
    @SerializedName("first") var name: String,
    @SerializedName("last") var surname: String
)

data class Picture (
    var large : String,
    var medium : String,
    var thumbnail : String
)