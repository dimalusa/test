package main.scala.utils

import java.io.File

object Converter {

  def getFile(fileName: String): File = {
    new File(s"./src/main/resources/generated/$fileName.jdl")
  }

  private def regWithInsensitive(reg: String) = {
    s"(?i)$reg"
  }

  def createEntity(sql: String, prefix: String): String = {
    sql.replaceAll(regWithInsensitive("""nvarchar\(\d*\)"""), "String")
      .replaceAll(regWithInsensitive("""varchar\(\d*\)"""), "String")
      .replaceAll(regWithInsensitive("""varchar\(max\)"""), "String")
      .replaceAll(regWithInsensitive("""nchar\(\d*\)"""), "String")
      .replaceAll(regWithInsensitive(""",(\n)"""), "$1")
      .replaceAll(regWithInsensitive(""" identity"""), "")
      .replaceAll(regWithInsensitive("""not null"""), "required")
      .replaceAll(regWithInsensitive("""(\s*)references(.*)"""), "")
      .replaceAll(regWithInsensitive("""(\s*)constraint(.*)"""), "")
      .replaceAll(regWithInsensitive("""(\s*)primary key(.*)"""), "")
      .replaceAll(regWithInsensitive("""not null"""), "required")
      .replaceAll(regWithInsensitive("""bit"""), "Boolean")
      .replaceAll(regWithInsensitive(""" smalldatetime"""), " Instant")
      .replaceAll(regWithInsensitive(""" datetime"""), " Instant")
      .replaceAll(regWithInsensitive(""" date"""), " Instant")
      .replaceAll(regWithInsensitive(""" tinyint"""), " Instant")
      .replaceAll(regWithInsensitive(""" time"""), " Instant")
      .replaceAll(regWithInsensitive(""" default .*"""), "")
      .replaceAll(regWithInsensitive(""" int"""), " Integer")
      .replaceAll(regWithInsensitive(""" smallint"""), " Integer")
      .replaceAll(regWithInsensitive(""" float"""), " Float")
      .replaceAll(regWithInsensitive("""numeric.*"""), "Integer")
      .replaceAll(regWithInsensitive("""money"""), "BigDecimal")
      .replaceAll(regWithInsensitive("""varbinary\(\d*\)"""), "String")
      .replaceAll(regWithInsensitive("""create\s*table(.*)\s[(]"""), "entity$1 {")
      .replaceAll(regWithInsensitive("""[)]\sgo"""), "}")
      .replaceAll(s"""$prefix[.]""", "")
  }

}
