type WorkflowId = GraphId
type PrincipalId = NodeId
type LineageEdgeId = ThingId
type LineageEdgeTVID = (LineageEdgeId, VersionId)

trait Workflow extends Graph {
  override def getId: WorkflowId

  def execute: Unit
}

trait Principal extends Node {
  override def getId: PrincipalId
}

trait LineageEdgeVersion extends RichVersion {
  def getTVID: LineageEdgeTVID

  def getEndPointOne : RichVersion

  def getEndPointTwo : RichVersion

  def getWorkflow: Option[WorkflowId]

  def getPrincipal: Option[Principal]

  def getLineageEdge: LineageEdge
}

trait LineageEdge extends Thing[LineageEdgeVersion] {
  override def getId: LineageEdgeId
}
