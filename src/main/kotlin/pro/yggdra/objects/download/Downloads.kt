package pro.yggdra.objects.download

import com.google.gson.annotations.SerializedName

class Downloads (
    val application: Download,
    @SerializedName("mojang-mappings")
    val mojangMappings: Download,
)