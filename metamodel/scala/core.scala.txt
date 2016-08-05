type VersionId = String
type ThingId = String

trait Version {
  def getId: VersionId
 }

trait VersionSuccessor[T <: Version] {
  def getChild: T

  def getParent: T

  def getRelationship: (T, T)
}

trait VersionHistoryDAG[T <: Version] {
  def getRoot: VersionId

  def getEdges: List[VersionSuccessor[T]]

  def isRooted: Boolean

  def getLeaves: Set[Versions]
}

trait Thing[T <: Version] {
  def getId: ThingId

  def getVersionHistor): VersionHistoryDAG[T]

  def isRooted: Boolean

  def getLatestVersions: Set[Versions]
}
