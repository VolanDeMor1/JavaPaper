package pro.yggdra.objects

import com.google.gson.annotations.SerializedName

enum class ProjectType {
    @SerializedName("paper") PAPER,
    @SerializedName("travertine") TRAVERTINE,
    @SerializedName("waterfall") WATERFALL,
    @SerializedName("velocity") VELOCITY,
    @SerializedName("folia") FOLIA;

    fun id(): String = name.lowercase()
}