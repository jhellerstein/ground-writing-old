type StructureId = ThingId
type NodeId = ThingId
type EdgeId = ThingId
type GraphId = ThingId
type ExternalThingId = ThingId
type Key = String
type URI = String
type StructureTVID = (StructureId, VersionId)
type NodeTVID = (NodeId, VersionId)
type EdgeTVID = (EdgeId, VersionId)
type GraphTVID = (GraphId, VersionId)
type ExternalThingTVID = (ExternalThingId, VersionId)

trait Tag {
  def getKey: Key

  def getType : Option[Type]

  def getValue: Option[Any]
}

trait StructureVersion extends Version {
  def getTVID: StructureTVID

  def getTagKeys: Map[Key, Type]

  def getStructure: Structure
}

trait Structure extends Thing[StructureVersion] {
  override def getId: StructureId
}

trait RichVersion extends Version {
  def getTags: Option[Map[Key, Tag]]

  def getStructure: Option[StructureVersion]
}

trait NodeVersion extends RichVersion {
  def getTVID: NodeTVID

  def getNode: Node
}

trait Node extends Thing[NodeVersion] {
  override def getId(): NodeId
}

trait EdgeVersion extends RichVersion {
  def getTVID: EdgeTVID

  def getEndpointOne: NodeVersion

  def getEndpointTwo: NodeVersion

  def getEdge: Edge
}

trait Edge extends Thing[EdgeVersion] {
  override def getId: EdgeId
}

trait GraphVersion extends RichVersion {
  def getTVID: GraphTVID

  def getEdgeVersions: Set[EdgeVersion]

  def getGraph: Graph
}

trait Graph extends Thing {
  override def getId: GraphId
}

trait ExternalVersion extends RichVersion {
  def getTVID: ExternalThingTVID

  def getReference: URI

  def getParameters: Map[String, String]

  def getExternalThing: ExternalThing
}

trait ExternalThing extends Thing {
  override def getId: ExternalThingId

  def isUnchanging: Boolean
}
