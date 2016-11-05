**QUIS** is a declarative, domain level, universal query language mainly designed to work with scientific data.

The language introduces the concept of “perspective”, which allows scientists to author the specification of their data objects independent of the physical data structures stored in data sources. Processes authored in XQt are translated into the native language of the underlying data source with the help of dynamically chosen and loaded adapters. For a data scientist it means that XQt offers a uniform way to query and process data regardless of the underlying data source. In addition, the XQt API allows other data processing software applications to hand over their data querying and management to XQt.

Some of the features of well-known data querying languages and systems are considered in the design of XQt.

XQt syntax is inspired mainly from SQL as well as Cypher and XQuery. The language's declarative nature implies no ordering, no control statements, and only immutable variables.

Note: This is a ***proof of concept*** and ongoing implementation for my PhD work!
