package main.scala

import database.{BankSiDBLight, PartnerDB, StoreLocatorDB}

object Main extends App {

  PartnerDB.compute()
  StoreLocatorDB.compute()
  BankSiDBLight.compute()
}

