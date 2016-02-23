Before reading this post, check out earlier post on the motivations behind
building general, expressive metadata services.

## Core

The core of the Ground (meta)data model revolves around the following statement: 
All logical objects in the system are versioned. You can never overwrite any
piece of data, only create a new version of it. Thus, the most basic object in
the Core of the metamodel is a `Version`. A `Version` is very simply a globally
unique identifier representing an immutable version of an object. Pairs of
`Version`s are linked together by `VersionSuccessors`, which indicate that the 
second `Version` is the child or edit of the first version. A `Version` can
have many `VersionSuccessor`s, which represents a fork in the history of this
object. Similarly, a `Version` may be the `VersionSuccessor` of multiple
versions, which represents a merge in history.

`Version`s and `VersionSuccessor`s are composed into `VersionHistoryDAG`s,
which has a root `R` and a set of `VersionSuccessor`s that form a DAG rooted at
`R`. All of the `VersionSuccessor`s must connect the same kind of `Version` --
more on this later.

The last element of the Core is the building block for the rest of the
metamodel: `Thing`s. A `Thing` represents a logical object in our system that
captures its own version history. It consists of a unique identifier and a
`VersionHistoryDAG`.

The Core is not exposed to above-Ground (user-level) services; it is the root
of an inheritance hierarchy that is exposed by the outer layers described
below. From this point on, when we speak of `Thing`s and `Verison`s, we will
actually be referring to objects in the subclasses defined below that are
visible above Ground.

## Mantle

The Mantle is composed of a few basic objects, all of which are subclasses of
`Thing`: `Structure`s, `Node`s, `Edge`s, and `Graph`s. Each one of these
classes is accompanied by a corresponding subclass of `Version`
(`StructureVersion`, `NodeVersion`, etc.). The `FooVersion` contains 
information specific to each kind of object that it is representing. The Mantle
also defines `Tag`s which are key-and-optional-value pairs associated with a
particular `Version` of a particular `Thing`.

`Structure`s can be thought of as templates. They specify a set of keys and
corresponding types; when another `Version` is associated with a particular
`Version` of a `Structure`, that `Version` must have key-value pairs for the
keys specified by the `StructureVersion`.

Before we go further, it is worth noting that our remaining `Version`
subclasses actually inherit from another subclass of `Version`: `RichVersion`.
A `RichVersion` is simply a `Version` that can conform to a `StructureVersion`
and can have *ad-hoc* `Tag`s. This is to avoid circular dependencies in which
`StructureVersion`s can conform to other `StructureVersion`s or can have
`Tag`s.

`Node`s, `Edge`s, and `Graph`s and their corresponding `RichVersion`s are 
fairly self-explanatory. `NodeVersion`s are standalone objects. `EdgeVersion`s
connect pairs of `NodeVersion`s, and `GraphVersion`s are collections of
`EdgeVersion`s. These objects can also optionally have external references.

The Mantle uses a graph abstraction for data modeling because graphs are very
general, and can represent metadata for a variety of data models. Unstructured
data models like JSON and XML are very typically represented as flexible trees
or graphs. Structured data models like relational and entity-relationship can
be represented as graphs as well, often with constraints on the shapes that the
graphs can take on. Ground's Mantle allows diverse models of metadata to
coexist in a single metadata store, and be integrated over time.

## Crust

The goal of the Crust is to capture usage information from the `Node`s and
`Edge`s in the Mantle. To facilitate data lineage, the Ground meatmodel
depends on `Principal`s and `Workflow`s.

`Principal`s are key to capturing the actors that work with data, particlarly
in support of data governance. `Principal`s are the representations of notions
such as users, groups, and roles. They are integral to data lineage because
they allow us to capture responsibility for creating, modifying, and accessing
data.

The other semantic concept believe is common is workflow sepcification. This
sort of metadata is very important in usage tracking and reproducing workflows.
`Workflow`s are extensions of the `Graph` structure in the Mantle. These
`Workflow`s can explain what data was transformed, how it was tranformed, and
what the results of that transformation are. `Workflow`s can capture both a
sequence of *ad hoc* exploratory actions of a pre-defined sequence of actions.

In Ground, data lineage is captured as a relationship between two `Version`s,
the second of which is logically derived from the first. This relationship is
due to some action, either computational (`Workflow`s) or manual (`Principal`s)
or both. Basic `EdgeVersion`s are defined in the Mantle, but they can only
connect `NodeVersion`s and cannot represent `Workflow`s or `Principal`s as the
objects instituting the changes. We use `LineageEdge`s for this purpose.
