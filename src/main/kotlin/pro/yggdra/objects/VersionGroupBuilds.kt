package pro.yggdra.objects

class VersionGroupBuilds(
    projectType: ProjectType,
    projectName: String,
    versionGroups: String,
    versions: Array<String>,
    val builds: Array<Build>
): VersionGroup(projectType, projectName, versionGroups, versions)