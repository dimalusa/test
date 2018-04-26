package main.scala

import database.{BankSiDBLight, PartnerDB, FormationDB}

object Main extends App {

  PartnerDB.compute()
  FormationDB.compute()
  BankSiDBLight.compute()
}

