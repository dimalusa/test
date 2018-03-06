package main.scala

import database.{DistribDB, PartnerDB}

object Main extends App {

  PartnerDB.compute()
  DistribDB.compute()
}

