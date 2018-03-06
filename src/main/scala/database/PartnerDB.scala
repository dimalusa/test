package main.scala.database

import java.io.{BufferedWriter, File, FileWriter}

import main.scala.utils.Converter
import main.scala.utils.Converter.createEntity


object PartnerDB {

  private val agenciesSql = s"""
create table Partner.Agencies
(
	refAgency int identity
		constraint PK_Agencies
			primary key,
	refPartner int not null,
	AgencyID varchar(20),
	PartnerAgencyID varchar(20),
	ExternalID varchar(20),
	CustomsCode varchar(10),
	SIRET varchar(20),
	AgencyName varchar(100),
	AgencyName2 varchar(100),
	ManagerFirstName varchar(50),
	ManagerLastName varchar(50),
	Address varchar(60),
	City varchar(50),
	Zip varchar(50),
	Area varchar(50),
	ISO2Country varchar(2),
	PhoneNumber varchar(20),
	Mobile varchar(20),
	Fax varchar(20),
	Email varchar(50),
	Login varchar(50),
	Password varchar(50),
	Free1 varchar(50),
	Free2 varchar(50),
	GMTOffset numeric(3,1),
	refRightList int,
	isBackOffice bit,
	isDeleted bit,
	PasswordCheck varbinary(50),
	NbTry int,
	NbUnsuccessfullTry int,
	LastUnsuccessfullTry smalldatetime,
	OpenHour varchar(5),
	CloseHour varchar(5),
	LockedDate smalldatetime,
	LastSuccessfullTry smalldatetime,
	NbUnlockedPer24h int,
	CreationDate smalldatetime default getutcdate(),
	PassageJourConf varchar(50),
	iRefLeadingAgency int
)
go


create table Partner.Agencies_Allowed_IP
(
	refIPAddress int identity
		constraint PK_Agencies_Allowed_IP
			primary key,
	refAgency int not null,
	IPAddress varchar(50),
	IPaddressMin varchar(50),
	IPaddressMax varchar(50),
	IPAddressStart varchar(50),
	CountryCode varchar(2),
	ISP varchar(50),
	REGION varchar(50)
)
go

create table Partner.Agencies_IP
(
	refAgency int not null,
	IPAddress varchar(50) not null,
	LastDate smalldatetime,
	AuthenticationMode varchar(1),
	ServerName varchar(15),
	constraint PK_Agencies_IP
		primary key (refAgency, IPAddress)
)
go

create table Partner.Agencies_SIG
(
	refAgency int not null
		constraint PK_Agencies_SIG_IP
			primary key,
	Latitude varchar(50),
	Longitude varchar(50)
)
go



create table Partner.AgencyService
(
	RefAgency int,
	RefService int
)
go

create table Partner.AgencySession
(
	Token varchar(50) not null
		constraint PK_AgencySession
			primary key,
	StartDate datetime,
	LastUsedDate datetime,
	RefAgency int,
	IPAddress varchar(50),
	AccessType varchar(1),
	WebServer varchar(50),
	CookieActivated bit
)
go


create table Partner.Agency_Hardware
(
	refAgency int not null
		constraint FK_Agency_Hardware_Agencies
			references Partner.Agencies,
	refHardware int not null,
	HardwareType varchar(3),
	refRightList int,
	constraint PK_Agency_Hardware
		primary key (refAgency, refHardware)
)
go

create table Partner.Agency_UID
(
	refAgency int not null
		constraint FK_Agency_UID_Agencies
			references Partner.Agencies,
	refUser int not null,
	RefRightList int,
	refUserParent int,
	refHardwareParent int,
	CreationDate smalldatetime,
	ExcludePOS bit,
	isSubstitute bit,
	SubstituteEndDate smalldatetime,
	constraint PK_Agency_UID
		primary key (refAgency, refUser)
)
go""".stripMargin

  private val partnersSql = s"""
                       |create table Partner.Partner_HW
                       |(
                       |	refPartner int not null,
                       |	refHardware int not null,
                       |	refRightList int,
                       |	HardwareType varchar(3),
                       |	constraint PK_Partner_HW
                       |		primary key (refPartner, refHardware)
                       |)
                       |go
                       |
 |create table Partner.Partner_UID
                       |(
                       |	refPartner int not null,
                       |	refUser int not null,
                       |	refRightList int not null,
                       |	NickName varchar(20),
                       |	constraint PK_Partner_UID
                       |		primary key (refPartner, refUser)
                       |)
                       |go
                       |
 |create table Partner.Partners
                       |(
                       |	refPartner int identity
                       |		constraint PK_Partner
                       |			primary key,
                       |	PartnerName varchar(50),
                       |	PartnerIdentifier varchar(3),
                       |	refRightList int
                       |)
                       |go
                       |
 |create table Partner.Entity
                       |(
                       |	refEntity int identity,
                       |	EntityLabel varchar(50)
                       |)
                       |go
                       |
                       |
 |create table Partner.Forbidden_UID
                       |(
                       |	refHardware int,
                       |	refUser int
                       |)
                       |go
                       |
 |create table Partner.Group
                       |(
                       |	refGroup int identity
                       |		constraint PK_Group
                       |			primary key,
                       |	refPartner int,
                       |	RefEntity int,
                       |	Label varchar(50),
                       |	Description varchar(200)
                       |)
                       |go
                       |
 |create table Partner.GroupElement
                       |(
                       |	refGroup int,
                       |	RefElement int
                       |)
                       |go
                       |
 |create table Partner.ListType
                       |(
                       |	RefTypeListe int identity
                       |		constraint PK_ListType
                       |			primary key,
                       |	TypeListName varchar(50),
                       |	TypeListDescription varchar(200)
                       |)
                       |go
                       |
 |create table Partner.ListType_Label
                       |(
                       |	RefTypeListe int not null,
                       |	RefLang varchar(50) not null,
                       |	ListTypeLabel varchar(200),
                       |	constraint PK_ListType_Label
                       |		primary key (RefTypeListe, RefLang)
                       |)
                       |go
                       |
                       |
 |create table Partner.Rights
                       |(
                       |	refService int not null,
                       |	refAction int not null,
                       |	TAGAction varchar(20),
                       |	ActionDescription varchar(100),
                       |	constraint PK_Rights
                       |		primary key (refService, refAction)
                       |)
                       |go
                       |
 |create table Partner.RightsList
                       |(
                       |	refRightList int identity
                       |		constraint PK_RightsList
                       |			primary key,
                       |	RefListType int,
                       |	RefPartner int,
                       |	Description varchar(max),
                       |	TAG varchar(10)
                       |)
                       |go
                       |
 |create table Partner.RightsListServices
                       |(
                       |	refRightList int not null,
                       |	refPartner int default 0 not null,
                       |	refService int not null,
                       |	refAction int not null,
                       |	constraint PK_RightsListByServices
                       |		primary key (refRightList, refPartner, refService, refAction)
                       |)
                       |go
                       |
 |create table Partner.Rights_Label
                       |(
                       |	refService int not null,
                       |	refAction int not null,
                       |	refLang int not null,
                       |	Label varchar(100),
                       |	constraint PK_Rights_Label
                       |		primary key (refService, refAction, refLang)
                       |)
                       |go
                       |
 |create table Partner.T_LeadingAgencies
                       |(
                       |	iRefLeadingAgency int identity
                       |		constraint PK_T_LeadingAgencies
                       |			primary key,
                       |	sLeadingAgencyName varchar(50) not null,
                       |	sLeadingAgencyBankReference varchar(10) not null,
                       |	sDescription varchar(150)
                       |)
                       |go
                       |
 |create table Partner.UID_Picture
                       |(
                       |	refUser int not null
                       |		constraint PK_UID_Picture
                       |			primary key,
                       |	Picture varchar(max),
                       |	LastRefreshDate datetime
                       |)
                       |go
                       |
 |create table Partner.UserSession
                       |(
                       |	Token varbinary(50) not null
                       |		constraint PK_UserSession
                       |			primary key,
                       |	EndDate datetime,
                       |	RefUser int,
                       |	RefAgency int,
                       |	RefHardware int
                       |)
                       |go
  """.stripMargin

  private val unknownEntity = s"""
entity User {
	RefUser Integer
}
                         |entity XAction {
                         |	refAction Integer
                         |}
entity XService {
	RefService Integer
}""".stripMargin

  private val relations =
    s"""
       |relationship OneToOne {
       | 	Group{refPartner} to Partners
       | 	RightsList{refPartner} to Partners
       | 	RightsListServices{refPartner} to Partners
       | 	Agencies{refPartner} to Partners
       | 	Partner_UID{refPartner} to Partners
       | 	Partner_HW{refPartner} to Partners
       |}
       |
      |relationship OneToOne {
       | 	AgencySession{RefAgency} to Agencies
       | 	Agencies_Allowed_IP{refAgency} to Agencies
       | 	AgencyService{refAgency} to Agencies
       | 	Agencies_SIG{refAgency} to Agencies
       | 	Agencies_IP{refAgency} to Agencies
       | 	Agency_Hardware{refAgency} to Agencies
       | 	Agency_UID{refAgency} to Agencies
       | 	UserSession{refAgency} to Agencies
       |}
       |
       |relationship OneToOne {
       | 	Partner_UID{refUser} to User
       | 	Agency_UID{refUser} to User
       | 	Agency_UID{refUserParent} to User
       | 	Forbidden_UID{refUser} to User
       | 	UID_Picture{refUser} to User
       | 	UserSession{refUser} to User
       |}
       |
      |relationship OneToOne {
       | 	RightsListServices{rightsList} to RightsList
       |}
       |
 |relationship OneToOne {
       | 	Rights_Label{RefService} to XService
       | 	Rights{RefService} to XService
       | 	RightsListServices{RefService} to XService
       | 	AgencyService{RefService} to XService
       |}
       |
 |relationship OneToOne {
       |    GroupElement{refGroup} to Group
       |}
       |
 |relationship OneToOne {
       |    Group{RefEntity} to Entity
       |}
       |
      |relationship OneToOne {
       |    ListType_Label{RefTypeListe} to ListType
       |}
       |
      |relationship OneToOne {
       | 	Rights_Label{refAction} to XAction
       | 	RightsListServices{refAction} to XAction
       | 	Rights{refAction} to XAction
       |}
    """.stripMargin


  def compute(): Unit = {
  // FileWriter
  val file = new File("partnerJDL.txt")
  val bw = new BufferedWriter(new FileWriter(file))
  bw.write(createEntity(agenciesSql, "Partner"))
  bw.write(createEntity(partnersSql, "Partner"))
  bw.write(unknownEntity)
  bw.write(relations)
  bw.close()
}
}



