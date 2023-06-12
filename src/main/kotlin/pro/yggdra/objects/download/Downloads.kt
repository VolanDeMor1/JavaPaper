package pro.yggdra.objects.download

import com.google.gson.annotations.SerializedName

class Downloads (
    val application: Download,
    @SerializedName("mojang-mappings")
    val mojangMappings: Download,
) {
    override fun toString(): String {
        return "Downloads(\n" +
                "\tapplication=$application, \n" +
                "\tmojangMappings=$mojangMappings\n" +
                ")"
    }
}