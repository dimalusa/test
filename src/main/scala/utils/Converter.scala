package main.scala.utils

object Converter {

  def createEntity(sql: String, prefix: String): String = {
    sql.replaceAll("""varchar\(\d*\)""", "String")
      .replaceAll("""varchar\(max\)""", "String")
      .replaceAll(""",(\n)""", "$1")
      .replaceAll(""" identity""", "")
      .replaceAll("""not null""", "required")
      .replaceAll("""(\s*)references(.*)""", "")
      .replaceAll("""(\s*)constraint(.*)""", "")
      .replaceAll("""(\s*)primary key(.*)""", "")
      .replaceAll("""not null""", "required")
      .replaceAll("""bit""", "Boolean")
      .replaceAll("""smalldatetime""", "Instant")
      .replaceAll("""datetime""", "Instant")
      .replaceAll("""time""", "Instant")
      .replaceAll(""" default .*""", "")
      .replaceAll(""" int""", " Integer")
      .replaceAll("""numeric.*""", "Integer")
      .replaceAll("""varbinary\(\d*\)""", "String")
      .replaceAll("""create\s*table(.*)\s[(]""", "entity$1 {")
      .replaceAll("""[)]\sgo""", "}")
      .replaceAll(s"""$prefix[.]""", "")
  }

}
