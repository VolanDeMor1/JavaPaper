package pro.yggdra.objects

class Change (
    val change: String,
    val summary: String,
    val message: String
) {
    override fun toString(): String {
        return "Change(\n" +
                "\tchange='$change', \n" +
                "\tsummary='$summary', \n" +
                "\tmessage='$message'\n" +
                ")"
    }
}