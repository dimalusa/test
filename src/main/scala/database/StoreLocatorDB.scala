package main.scala.database

import java.io.{BufferedWriter, FileWriter}

import main.scala.utils.Converter.{createEntityFromPostgres, getFile}

object StoreLocatorDB {

  private val storeLocatorSql: String =
    s"""
       |CREATE Table PointOfSale (
       |  id            INT PRIMARY KEY,
       |  name          TEXT      NOT NULL,
       |  businessName TEXT,
       |  coordinates   GEOGRAPHY,
       |  siret         TEXT,
       |  description   TEXT,
       |  createdAt    TIMESTAMP NOT NULL
       |);
       |
 |CREATE Table PointOfSaleTerminal (
       |  id                INT PRIMARY KEY,
       |  pointOfSaleId  INT       NOT NULL REFERENCES pointOfSale (id),
       |  installationDate TIMESTAMP NOT NULL,
       |  serialNumber     TEXT      NOT NULL,
       |  lastConnexion    DATETIME
       |);
       |
 |CREATE Table Service (
       |  id    INT PRIMARY KEY,
       |  type  TEXT NOT NULL,
       |  label TEXT NOT NULL
       |);
       |
 |CREATE Table PointOfSaleServices (
       |  id               INT       NOT NULL,
       |  pointOfSaleId INT REFERENCES pointOfSale (id),
       |  serviceId       INT REFERENCES service (id),
       |  createdAt       TIMESTAMP NOT NULL,
       |  PRIMARY KEY (pointOfSaleId, serviceId)
       |);
       |
 |CREATE Table Image (
       |  id               INT PRIMARY KEY,
       |  pointOfSaleId INT NOT NULL REFERENCES pointOfSale (id),
       |  url              TEXT,
       |  source           BINARY
       |);
       |
 |CREATE Table Staff (
       |  id                  INT PRIMARY KEY,
       |  firstName          TEXT      NOT NULL,
       |  lastName           TEXT      NOT NULL,
       |  homePhoneNumber   TEXT,
       |  mobilePhoneNumber TEXT,
       |  about               TEXT,
       |  createdAt          TIMESTAMP NOT NULL
       |);
       |
 |CREATE Table PointOfSaleStaff (
       |  id               INT       NOT NULL,
       |  pointOfSaleId INT REFERENCES pointOfSale (id),
       |  staffId         INT REFERENCES staff (id),
       |  createdAt       TIMESTAMP NOT NULL,
       |  PRIMARY KEY (pointOfSaleId, staffId)
       |);
     """.stripMargin

  private val relations =
    s"""
       |relationship ManyToOne {
       | 	Image{pointOfSale} to PointOfSale{images}
       | 	PointOfSaleTerminal{pointOfSale} to PointOfSale{posTerminals}
       | 	PointOfSaleServices{associatedPointOfSale} to PointOfSale{services}
       | 	PointOfSaleServices{service} to Service{ids}
       | 	PointOfSaleStaff{pointOfSale} to PointOfSale{staffs}
       | 	PointOfSaleStaff{staff} to Staff{ids}
       |}
    """.stripMargin

  def compute(): Unit = {
    val file = getFile("storelocatorJDL")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(createEntityFromPostgres(storeLocatorSql, ""))
    // bw.write("paginate StoreLocatorPOS with pagination")
     bw.write(relations)
    bw.close()
  }
}

