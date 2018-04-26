package main.scala.database

import java.io.{BufferedWriter, FileWriter}

import main.scala.utils.Converter.{createEntityFromSQLServer, getFile}


object BankSiDBLight {

  private val bankSiSql = s"""

 |create table BankSI.Customer
                             |(
                             |	RefCustomer int identity
                             |		constraint PK_Customer
                             |			primary key,
                             |	FirstName varchar(50),
                             |	LastName varchar(50),
                             |	RefPoliteness int,
                             |	Sex varchar(1),
                             |	email varchar(326),
                             |	PhoneNumber varchar(50),
                             |	refSubscriptionABM int,
                             |	RegistrationCode varchar(20),
                             |	BankReference varchar(10),
                             |	BankWebIdentifier varchar(12),
                             |	BankAccountNumber varchar(20),
                             |	BankRefSubscription varchar(20),
                             |	FirstDeposit money,
                             |	BarCode varchar(20),
                             |	BirthDate smalldatetime,
                             |	BirthCountryISO2 varchar(2),
                             |	BirthPlace varchar(50),
                             |	CreationDate smalldatetime default getutcdate(),
                             |	isPro bit default 0,
                             |	isDeleted bit default 0,
                             |	isLocked bit default 0,
                             |	IBAN varchar(34),
                             |	FirstAddressChecked bit,
                             |	TokenRegistrationCode varchar(100),
                             |	BankStatus smallint,
                             |	IsSubscriptionFree bit,
                             |	RefAccountKind smallint,
                             |	RefCustomerParent int
                             |)
                             |go
 |create table BankSI.Politeness
 |(
 |	RefPoliteness int,
 |	Politeness varchar(10),
 |	PolitenessLabel varchar(20),
 |	PolitenessTAG varchar(5)
 |)
 |go
 |
 |create table BankSI.AccountKind
 |(
 |	RefAccountKind int not null
 |		constraint PK_AccountKind
 |			primary key,
 |	AccountKindTAG varchar(3),
 |	AccountKindLabel varchar(200)
 |)
 |go
 """.stripMargin

  private val relations =
    s"""
    """.stripMargin


  def compute(): Unit = {
    val file = getFile("bankSiLightJDL")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(createEntityFromSQLServer(bankSiSql, "BankSI"))
   // bw.write(createEntityFromSQLServer(missingSql, "BankSI"))
    bw.write(relations)
    bw.close()
}
}



