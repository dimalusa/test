package main.scala.database

import java.io.{BufferedWriter, FileWriter}

import main.scala.utils.Converter.{createEntity, getFile}

object StoreLocatorDB {

  private val storeLocatorSql: String =
    s"""
       |CREATE TABLE web.StoreLocatorPOS
       |(
       |  ref            INT           IDENTITY
       |    CONSTRAINT PK_StoreLocatorPOS
       |    PRIMARY KEY,
       |  AgencyName     VARCHAR(100) NOT NULL,
       |  Label1         VARCHAR(100),
       |  Address        VARCHAR(100),
       |  Zip            VARCHAR(50),
       |  City           VARCHAR(50),
       |  Area           VARCHAR(50),
       |  Latitude       VARCHAR(50),
       |  Longitude      VARCHAR(50),
       |  FreeLabel1     VARCHAR(200),
       |  CreationDate   SMALLDATETIME DEFAULT getutcdate(),
       |  Certified      BIT           DEFAULT 0,
       |  SearchLocation BIT,
       |  IsVisible      BIT           DEFAULT 0,
       |  idePDVSAB      VARCHAR(20),
       |  NbCitizens     INT,
       |  CitizenRange   INT,
       |  IsMPAD         BIT,
       |  PhoneNumber    VARCHAR(200),
       |  OpeningHours   VARCHAR(200)
       |)
       |GO
       |
     """.stripMargin

  def compute(): Unit = {
    val file = getFile("storelocatorJDL")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(createEntity(storeLocatorSql, "web"))
    bw.write("paginate StoreLocatorPOS with pagination")
    bw.close()
  }
}

