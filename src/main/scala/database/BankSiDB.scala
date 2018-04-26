package main.scala.database

import java.io.{BufferedWriter, FileWriter}

import main.scala.utils.Converter.{createEntityFromSQLServer, getFile}


object BankSiDB {

  private val bankSiSql = s"""
                             |create table BankSI.UnsuccessfullRegistration
                             |(
                             |	LastName varchar(50),
                             |	FirstName varchar(50),
                             |	BirthDate datetime,
                             |	Mobile varchar(20),
                             |	Email varchar(200),
                             |	InsertDate datetime,
                             |	NbTentatives int,
                             |	LogsEnvoi varchar(max)
                             |)
                             |go
                             |
 |create table BankSI.CardOppositionReason
                             |(
                             |	CardOppositionReasonCode int not null
                             |		constraint PK_CardOppositionReason
                             |			primary key,
                             |	CardOppositionReasonLabel varchar(300)
                             |)
                             |go
                             |
 |create table BankSI.AddressCheck
                             |(
                             |	RefAddressCheck int identity
                             |		constraint PK_AddressCheck
                             |			primary key,
                             |	RegistrationCode varchar(20),
                             |	PhoneNumber varchar(20),
                             |	NbChallengeGeneration int,
                             |	Challenge varchar(20),
                             |	ChallengeH varbinary(50),
                             |	AddressChecked bit,
                             |	CheckDate smalldatetime,
                             |	CreationDate smalldatetime,
                             |	NbUnsuccessfullTry int,
                             |	LastTryDate datetime,
                             |	NbReceptions int,
                             |	RC int
                             |)
                             |go
                             |
 |create table BankSI.CustomerChildren
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerChildren
                             |			primary key,
                             |	Email varchar(326),
                             |	PhoneNumber varchar(50),
                             |	TransferAllowedAmount money,
                             |	TransferAllowedStart smalldatetime,
                             |	TransferAllowedEnd smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerBirthDepartment
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerBirthDepartment
                             |			primary key,
                             |	BirthDepartment varchar(3)
                             |)
                             |go
                             |
 |
 |create table BankSI.AddressCheck_Attempt
                             |(
                             |	RefAddressCheck int not null
                             |		constraint PK_AddressCheck_Attempt
                             |			primary key,
                             |	RegistrationCode varchar(20),
                             |	PhoneNumber varchar(20),
                             |	ChallengeH varbinary(50),
                             |	AddressChecked bit,
                             |	CheckDate smalldatetime,
                             |	CreationDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.LockCustomerReason
                             |(
                             |	RefLockReason int not null
                             |		constraint PK_LockCustomerReason
                             |			primary key,
                             |	LockReason varchar(50),
                             |	isFilterHideReason bit
                             |)
                             |go
                             |
 |create table BankSI.CustomerCardOpposition
                             |(
                             |	RefCardOpposition int not null
                             |		constraint PK_CustomerCardOpposition
                             |			primary key,
                             |	RefCustomer int not null,
                             |	HashCard varchar(50),
                             |	CardOppositionReasonCode int,
                             |	StealDate smalldatetime,
                             |	barcode varchar(12),
                             |	OppositionDate smalldatetime,
                             |	OppositionRequest varchar(2000),
                             |	OppositionAnswer varchar(2000),
                             |	Opposed bit,
                             |	NotificationSent bit,
                             |	NotificationSentDate bit
                             |)
                             |go
                             |
 |create table BankSI.PinBySMS
                             |(
                             |	RefPinBySMS int identity
                             |		constraint PK_PinBySMS
                             |			primary key,
                             |	TrackingNumber varchar(20),
                             |	PhoneNumber varchar(20),
                             |	PhoneNumber2 varchar(20),
                             |	RegistrationCode varchar(20),
                             |	ChallengeH varbinary(50),
                             |	NbChallengeGeneration int,
                             |	Sent bit,
                             |	CreationDate datetime,
                             |	CheckDate datetime,
                             |	NbUnsuccessfullTry int,
                             |	SentDate datetime,
                             |	NbReceptions int,
                             |	ReceptionDate datetime,
                             |	LastTryDate datetime,
                             |	ProviderRC int,
                             |	RC int,
                             |	ElapsedTimeSMS time
                             |)
                             |go
                             |
 |create table BankSI.CustomerTax
                             |(
                             |	RefCustomerTax int identity
                             |		constraint PK_CustomerTax
                             |			primary key,
                             |	RefCustomer int,
                             |	ISO2 varchar(2),
                             |	TaxId varchar(20),
                             |	ReasonNoTax varchar(max)
                             |)
                             |go
                             |
 |
 |create table BankSI.CardActivation
                             |(
                             |	RefCardActivation int identity
                             |		constraint PK_CardActivation
                             |			primary key,
                             |	RefCustomer int,
                             |	HCard varchar(50),
                             |	RequestDate datetime,
                             |	RequestXML varchar(max),
                             |	AnswerDate datetime,
                             |	AnswerXML varchar(max),
                             |	Activated bit,
                             |	Monext_REF_ACTIVATING varchar(20),
                             |	Monext_STAMP varchar(20),
                             |	RC int,
                             |	RefBoUser int,
                             |	Channel varchar(10)
                             |)
                             |go
                             |
 |
 |create table BankSI.Registration
                             |(
                             |	RefTransaction int not null
                             |		constraint PK_Registration
                             |			primary key,
                             |	RegistrationCode varchar(50),
                             |	RequestXML varchar(max),
                             |	AnswerXML varchar(max),
                             |	RequestDate datetime,
                             |	AnswerDate datetime,
                             |	CashierName varchar(50),
                             |	CashierID varchar(50),
                             |	AgencyName varchar(50),
                             |	AgencyID varchar(50),
                             |	refHardware int,
                             |	RC int
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerATDDocumentStatus
                             |(
                             |	RefDocATDStatus int not null,
                             |	StatusTAG varchar(20),
                             |	StatusLabel varchar(200)
                             |)
                             |go
                             |
 |create table BankSI.PinBySMS_Attempt
                             |(
                             |	RefPinBySMS int,
                             |	TrackingNumber varchar(20),
                             |	PhoneNumber varchar(20),
                             |	PhoneNumber2 varchar(20),
                             |	RegistrationCode varchar(20),
                             |	ChallengeH varbinary(50),
                             |	NbChallengeGeneration int,
                             |	Sent bit,
                             |	CreationDate smalldatetime,
                             |	CheckDate smalldatetime,
                             |	SentDate smalldatetime,
                             |	ReceptionDate datetime,
                             |	LastTryDate datetime,
                             |	ProviderRC int
                             |)
                             |go
                             |
 |create table BankSI.RegistrationHisto
                             |(
                             |	RefTransaction int not null,
                             |	RegistrationCode varchar(50),
                             |	RequestXML varchar(max),
                             |	AnswerXML varchar(max),
                             |	RequestDate datetime,
                             |	AnswerDate datetime,
                             |	HistoDate datetime,
                             |	CashierName varchar(50),
                             |	CashierID varchar(50),
                             |	AgencyName varchar(50),
                             |	AgencyID varchar(50),
                             |	refHardware int,
                             |	RC int
                             |)
                             |go
                             |
 |create table BankSI.AddressConfirmationReasons
                             |(
                             |	RefReasonAddressConfirmationStarted int,
                             |	ReasonDescription varchar(250)
                             |)
                             |go
                             |
 |create table BankSI.SMSAddressCheckAttempts
                             |(
                             |	RefAttempt int identity
                             |		constraint PK_SMSAttemptsAddressCheck
                             |			primary key,
                             |	PhoneNumber varchar(20),
                             |	Challenge varchar(20),
                             |	AttemptDate datetime,
                             |	RefAddressCheck int,
                             |	RC int
                             |)
                             |go
                             |
 |create table BankSI.CardStatus
                             |(
                             |	PackNumber varchar(20) not null
                             |		constraint PK_CardStatus
                             |			primary key,
                             |	Colis varchar(50),
                             |	DtInsert smalldatetime default getdate() not null,
                             |	DtFabrication date,
                             |	AgencyIDCommande varchar(20),
                             |	AgencyIDReception varchar(20),
                             |	DtReception datetime,
                             |	DtVente datetime,
                             |	AgencyIDVente varchar(20),
                             |	DtActivation datetime,
                             |	AgencyIDActivation varchar(20),
                             |	HCard varchar(50),
                             |	Etat varchar(50),
                             |	DateEtat smalldatetime,
                             |	ModeVente varchar(20),
                             |	CardTrackingNumber varchar(15),
                             |	CodeBin varchar(10),
                             |	CodeVisuel varchar(25),
                             |	DtFinValidite varchar(10)
                             |)
                             |go
                             |
 |create table BankSI.AgentScore
                             |(
                             |	Refcustomer int not null,
                             |	IDPDV varchar(20),
                             |	Barcode varchar(20),
                             |	AgentScore int,
                             |	RawCurativeScore int,
                             |	RawPreventiveScore int,
                             |	CurativeScore int,
                             |	PreventiveScore int,
                             |	AgentScoreDetail varchar(max),
                             |	LastChangeDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.ISILIS
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_ISILIS
                             |			primary key,
                             |	RegistrationDate smalldatetime,
                             |	LastRequestDate smalldatetime,
                             |	SABRequest varchar(max),
                             |	SABAnswer varchar(max),
                             |	PaidDate smalldatetime,
                             |	Paid bit
                             |)
                             |go
                             |
                             """.stripMargin
  val missingSql = s"""
                      |
 |
 |create table BankSI.CustomerRenewSubscriptionAttempts
                      |(
                      |	RefAttempt int identity
                      |		constraint PK_CustomerRenewSubscriptionAttempts
                      |			primary key,
                      |	RefCustomer int not null,
                      |	AttemptDate datetime,
                      |	AttemptAnswerDate datetime,
                      |	RC int,
                      |	RefUID int
                      |)
                      |go
 |
 |create table BankSI.CustomerPersonalizedCardInterest
                      |(
                      |	RefCustomer int not null,
                      |	StartDate smalldatetime,
                      |	Zendesk bit,
                      |	ZendeskDate smalldatetime,
                      |	WebAccess bit
                      |)
                      |go
 |
 |create table BankSI.CustomerPersonalizedCardAttempts
                      |(
                      |	RefAttempt int identity
                      |		constraint PK_CustomerPersonalizedCardAttempts
                      |			primary key,
                      |	RefCustomer int not null,
                      |	AttemptDate datetime,
                      |	AttemptAnswerDate datetime,
                      |	RC int
                      |)
                      |go
                      |
 |create table BankSI.CustomerPersonalizedCardOrder
                      |(
                      |	RefCardOrder int identity
                      |		constraint PK_CustomerPersonalizedCardOrder
                      |			primary key,
                      |	RefCustomer int not null,
                      |	AskDate smalldatetime,
                      |	PreviewFileName varchar(200),
                      |	Paid bit,
                      |	PaymentDate smalldatetime,
                      |	isGenerated bit,
                      |	GenerationDate smalldatetime,
                      |	isManufactured bit,
                      |	ManufacturationDate smalldatetime,
                      |	isSent bit,
                      |	SendDate smalldatetime,
                      |	Address1 varchar(50),
                      |	Address2 varchar(50),
                      |	Address3 varchar(50),
                      |	Address4 varchar(50),
                      |	At varchar(100),
                      |	City varchar(50),
                      |	Zipcode varchar(50),
                      |	CountryISO3 varchar(3),
                      |	Oldbarcode varchar(20),
                      |	Newbarcode varchar(20),
                      |	OldCardOpposed bit,
                      |	OldCardOppositionDate smalldatetime,
                      |	NewCardActivated bit,
                      |	NewCardActivationDate smalldatetime,
                      |	ModelTag varchar(6),
                      |	isCancelled bit,
                      |	ToGenerate bit,
                      |	DisplayName varchar(50)
                      |)
                      |go
                      |
 |create table BankSI.CustomerPersonalizedCardRefund
                      |(
                      |	RefCardRefund int identity
                      |		constraint PK_CustomerPersonalizedCardRefund
                      |			primary key,
                      |	RefCustomer int not null,
                      |	ReasonTAG varchar(20),
                      |	AskDate smalldatetime,
                      |	RefCre int,
                      |	RC int,
                      |	RefBOUser int
                      |)
                      |go
 |create table BankSI.T_ImportBIC
                             |(
                             |	[MODIFICATION FLAG] varchar(50),
                             |	[RECORD KEY] varchar(50),
                             |	BIC varchar(50),
                             |	[INSTITUTION NAME] varchar(300),
                             |	CITY varchar(50),
                             |	[ISO COUNTRY CODE] varchar(50),
                             |	SCHEME varchar(50),
                             |	[ADHERENCE BIC] varchar(50),
                             |	[ADHERENCE START DATE] varchar(50),
                             |	[ADHERENCE STOP DATE] varchar(50),
                             |	[PAYMENT CHANNEL ID] varchar(50),
                             |	[PREFERRED CHANNEL FLAG] varchar(50),
                             |	REACHABILITY varchar(50),
                             |	[INTERMEDIARY INSTITUTION BIC] varchar(50),
                             |	[START DATE] varchar(50),
                             |	[STOP DATE] varchar(50),
                             |	AOS varchar(50),
                             |	[FIELD A] varchar(50),
                             |	[FIELD B] varchar(50)
                             |)
                             |go
 |
 |create table BankSI.[3DChannel]
 |(
 |	RefChannel int identity
 |		constraint PK_3DChannel
 |			primary key,
 |	ChannelLabel varchar(20)
 |)
 |go
 |create table BankSI.[3DSecure]
 |(
 |	Ref3DS int identity
 |		constraint PK_3DSecure
 |			primary key,
 |	RefCustomer int,
 |	RefChannel nchar(10),
 |	CreationDate datetime,
 |	SentDate datetime,
 |	MonextDate varchar(10),
 |	MonextTime varchar(10),
 |	RC int,
 |	texte varchar(250)
 |)
 |go
 |create table BankSI.[3DSLockReason]
 |(
 |	ReasonTAG varchar(10) not null
 |		constraint PK_3DSLockReason
 |			primary key,
 |	ReasonLabel varchar(50)
 |)
 |go
 |create table BankSI.[3DSLockCustomer]
 |(
 |	Ref3DSLock int identity
 |		constraint PK_3DSLockCustomer
 |			primary key,
 |	RefCustomer int,
 |	StartDate smalldatetime,
 |	EndDate smalldatetime,
 |	ReasonTAG varchar(10)
 |		constraint FK_3DSLockCustomer_3DSLockReason
 |			references BankSI.[3DSLockReason]
 |)
 |go
                             """
  val d3 = s"""              |
 |create table BankSI.CustomerATDDocumentType
                             |(
                             |	DocType varchar(5) not null
                             |		constraint PK_CustomerATDDocumentType
                             |			primary key,
                             |	RefSpecificity int,
                             |	DocTypeLabel varchar(100)
                             |)
                             |go
                             |
 |create table BankSI.WelcomeLetterPND
                             |(
                             |	RefWelcomeLetterPND int identity,
                             |	PND int,
                             |	IDPNDFILE int,
                             |	InsertionDate datetime,
                             |	PNDFromFile varchar(50),
                             |	Treated bit default 0 not null
                             |)
                             |go
                             |
 |create table BankSI.EsabPassword
                             |(
                             |	RefEsab int identity,
                             |	RegistrationCode varchar(20),
                             |	PhoneNumber varchar(20),
                             |	CreationDate smalldatetime,
                             |	PhoneNumberCheckDate smalldatetime,
                             |	EsabPassword varchar(20),
                             |	Sent bit,
                             |	SentDate smalldatetime,
                             |	NbUnsuccessfullTry int,
                             |	LastSentErrorDate smalldatetime,
                             |	NbUnsuccessfullDelivery int,
                             |	DeliveryErrorDate smalldatetime,
                             |	MessageId nvarchar(50),
                             |	Received bit,
                             |	ReceiveDate smalldatetime
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerATDDocument
                             |(
                             |	RefDocATD int identity
                             |		constraint PK_CustomerATDDocument
                             |			primary key,
                             |	DocName varchar(250),
                             |	DocType varchar(5),
                             |	DocCustomerName varchar(100),
                             |	Address varchar(500),
                             |	sADD_Recipient varchar(100),
                             |	sADD_Street varchar(100),
                             |	sADD_Zip varchar(10),
                             |	sADD_City varchar(100),
                             |	Amount money,
                             |	TransferAmount money,
                             |	ReferenceDocument varchar(100),
                             |	LastNameDocument varchar(50),
                             |	FirstNameDocument varchar(50),
                             |	RefCustomer int,
                             |	IBAN varchar(32),
                             |	MailBDF varchar(250),
                             |	CreationDate datetime,
                             |	FindCustomerDate datetime,
                             |	ReservedBy int,
                             |	Treated bit,
                             |	TreatmentDate datetime,
                             |	RCTreatment int,
                             |	NbPages int,
                             |	CheckedDate smalldatetime,
                             |	CertifiedBy int,
                             |	RejectedBy int,
                             |	RejectedReason varchar(500),
                             |	ResponseMethod varchar(10),
                             |	RefDocATDStatus int,
                             |	RefCustomerSpecificity int,
                             |	iStatutEnvoiTresorerie int default 0,
                             |	CustomerBalance money default 0,
                             |	sMessageID varchar(50),
                             |	GoogleUploadDate smalldatetime,
                             |	RejectedFinalBy int,
                             |	RejectedFinalDate smalldatetime
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerATDDocumentLogs
                             |(
                             |	RefDocATDLog int identity
                             |		constraint PK_CustomerATDDocumentLogs
                             |			primary key,
                             |	RefDocATD int not null,
                             |	LogDate datetime default getutcdate(),
                             |	DocATDLog varchar(500)
                             |)
                             |go
                             |
 |create table BankSI.CustomerAddInfo
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerAddInfo
                             |			primary key,
                             |	codPro varchar(3),
                             |	modHab varchar(3),
                             |	sitFam varchar(3),
                             |	ProofOfDomicile bit,
                             |	Address1 varchar(50),
                             |	Address2 varchar(50),
                             |	Address3 varchar(50),
                             |	Address4 varchar(50),
                             |	At varchar(100),
                             |	City varchar(50),
                             |	Zipcode varchar(50),
                             |	CountryISO3 varchar(3),
                             |	PDFSubscriptionSent bit,
                             |	PDFSubscriptionSendDate smalldatetime,
                             |	ArchiveGenerated bit,
                             |	RefArchive int,
                             |	ArchiveDate smalldatetime,
                             |	RefAddressCheck int,
                             |	AddressCheckStartdate datetime,
                             |	AddressCheckChallenge varchar(10),
                             |	AddressCheckMessageID varchar(50),
                             |	RefStatusAddressCheck int,
                             |	AddressCheckDate smalldatetime,
                             |	AddressCheckKind int,
                             |	AddressCheckLetterStatusDate smalldatetime,
                             |	AddressCheckLetterStatusCode int,
                             |	AddressCheckLetterStatusDescription varchar(200),
                             |	ZendeskCreated bit default 0,
                             |	ZendeskCreationDate smalldatetime,
                             |	isSuspicious bit,
                             |	SuspiciousReason varchar(200),
                             |	LastIdentifierUpdate datetime,
                             |	EmailTemp varchar(326),
                             |	Latitude varchar(20),
                             |	Longitude varchar(20),
                             |	LockReason varchar(200),
                             |	IDPDVSubscription varchar(15),
                             |	SatisfactionDate smalldatetime,
                             |	FirstAddressCheckDate smalldatetime,
                             |	FirstAddressCheckMethod smallint
                             |)
                             |go
                             |
                             |
 |create table BankSI.CustomerAddressConfirmation
                             |(
                             |	RefCustomerAddressConfirmation int identity,
                             |	RefCustomer int not null,
                             |	RefReasonAddressConfirmationStarted int,
                             |	StartDate datetime,
                             |	NbSMSSent int,
                             |	NbEmailSent int,
                             |	FirstSMSDate datetime,
                             |	FirstEmailDate datetime,
                             |	LastSMSDate datetime,
                             |	LastEmailDate datetime,
                             |	ConfirmationStatus nchar(10),
                             |	ConfirmationDate datetime,
                             |	ConfirmationMethod varchar(10),
                             |	NbNo int,
                             |	LastNoDate datetime,
                             |	ExpireOfferDate date,
                             |	ExpireCardDate date
                             |)
                             |go
                             |
 |create table BankSI.CustomerOldBarcode
                             |(
                             |	RefReplacement int identity
                             |		constraint PK_CustomerOldBarcode
                             |			primary key,
                             |	RefCustomer int not null,
                             |	BarCode varchar(20),
                             |	NewBarcode varchar(20),
                             |	ReplacementDate datetime
                             |)
                             |go
                             |
 |
 |create table BankSI.WelcomeLetterPNDLogImport
                             |(
                             |	id int identity
                             |		constraint PK_IdLog
                             |			primary key,
                             |	DateImport datetime default getutcdate() not null,
                             |	FileName varchar(300),
                             |	InsertedRows nchar(10),
                             |	Logs varchar(300)
                             |)
                             |go
                             |
 |create table BankSI.T_EckRNIPPrspn
                             |(
                             |	id int identity,
                             |	IdRnippFile int not null,
                             |	dateRspn date default getutcdate() not null,
                             |	refCustomer int not null,
                             |)
                             |go
                             |
 |create table BankSI.OverdrawnsPastAccounts
                             |(
                             |	RefCustomer int not null,
                             |	BeginDate datetime not null,
                             |	EndDate datetime not null,
                             |	AmountAtStart float not null,
                             |	AmountAtEnd float not null
                             |)
                             |go
                             |
 |create table BankSI.CustomerRenewExpireCard
                             |(
                             |	RefCustomerRenewExpireCard int identity
                             |		constraint PK_CustomerRenewExpireCard
                             |			primary key,
                             |	CreationDate smalldatetime default getutcdate(),
                             |	RefCustomer int,
                             |	ExpireDate datetime,
                             |	FirstNotification smalldatetime,
                             |	KYCCompleted bit,
                             |	KYCCompletedDate smalldatetime,
                             |	RenewCardDone bit,
                             |	RefCardOrder int,
                             |	RenewCardDate smalldatetime,
                             |	RenewCardMethodTAG varchar(3),
                             |	Cancelled bit,
                             |	CancelReason varchar(50),
                             |	NotificationJMoins60 bit,
                             |	NotificationJMoins40 bit,
                             |	NotificationJMoins25 bit,
                             |	NotificationJMoins10 bit,
                             |	NotificationJMoins3 bit,
                             |	NotificationJ bit,
                             |	NotificationJPlus7 bit
                             |)
                             |go
                             |
 |
 |create table BankSI.DocumentType
                             |(
                             |	DocumentType varchar(2),
                             |	BankSI_DocumentType varchar(10),
                             |	Category varchar(3)
                             |)
                             |go
                             |
 |create table BankSI.SMSAttempts
                             |(
                             |	RefAttempt int identity
                             |		constraint PK_SMSAttempts
                             |			primary key,
                             |	PhoneNumber varchar(20),
                             |	Challenge varchar(20),
                             |	AttemptDate datetime,
                             |	RefPinBySMS int,
                             |	RC int
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
                             |
 |create table BankSI.ScoreChurn
                             |(
                             |	Refcustomer int not null
                             |		constraint PK_CustomerScoreChurnY1
                             |			primary key,
                             |	CHURN_SCORE varchar(20),
                             |	SCORE_DETAIL varchar(max),
                             |	LastChangeDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CardExchange
                             |(
                             |	RefCardExchange int identity
                             |		constraint PK_CardReplacement
                             |			primary key,
                             |	AskDate datetime,
                             |	TAG varchar(10),
                             |	RefCustomer int,
                             |	NbTry int default 0,
                             |	Cancelled bit default 0,
                             |	CancelledDate datetime,
                             |	Oldbarcode varchar(20),
                             |	Newbarcode varchar(20),
                             |	Refhardware int,
                             |	Closed bit default 0,
                             |	ClosedDate datetime,
                             |	RequestXML varchar(max),
                             |	AnswerXML varchar(max),
                             |	RequestDate smalldatetime,
                             |	AnswerDate smalldatetime,
                             |	PhoneNumber varchar(50),
                             |	NbSend int,
                             |	iRefCREComPOS int
                             |)
                             |go
                             |
 |create table BankSI.CardStatusLog
                             |(
                             |	IdLog int identity
                             |		constraint PK_CardStatusLog
                             |			primary key,
                             |	PackNumber varchar(20),
                             |	DtInsert datetime default getdate() not null,
                             |	AgencyID varchar(20),
                             |	Comments varchar(max)
                             |)
                             |go
                             |
 |create table BankSI.CustomerLinksHistory
                             |(
                             |	RefPrimaryCustomer int not null,
                             |	RefSecondaryCustomer int not null,
                             |	LinkType int,
                             |	RemoveDate datetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerChangeLimit
                             |(
                             |	RefCustomer int,
                             |	PaymentLimit varchar(10),
                             |	WithdrawMoneyLimit varchar(10),
                             |	ChangeDate datetime
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerRenewSubscription
                             |(
                             |	RefCustomer int not null,
                             |	NbYears int not null,
                             |	ExpectedRenewDate smalldatetime,
                             |	RenewDate smalldatetime,
                             |	MonthDelay int,
                             |	WarningMail smalldatetime,
                             |	WarningSMS smalldatetime,
                             |	WarningMailBalanceIssue smalldatetime,
                             |	Paid bit,
                             |	WantToRenewDate smalldatetime,
                             |	WantToCloseDate smalldatetime,
                             |	Pending bit,
                             |	CancelledSub bit,
                             |	constraint PK_CustomerRenewSubscription
                             |		primary key (RefCustomer, NbYears)
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerLinksType
                             |(
                             |	LinkType int not null
                             |		constraint PK_CustomerLinksType
                             |			primary key,
                             |	LinkTypeLabel varchar(100),
                             |	WebAccess bit
                             |)
                             |go
                             |
 |create table BankSI.Groups
                             |(
                             |	RefGroup int identity
                             |		constraint PK_Group
                             |			primary key,
                             |	TAGGroupKind varchar(3),
                             |	GroupName varchar(200)
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerShortCode
                             |(
                             |	phonenumber varchar(20) not null
                             |		constraint PK_CustomerShortCode
                             |			primary key,
                             |	Allowed bit,
                             |	CreationDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerGroup
                             |(
                             |	RefGroup int,
                             |	RefCustomer int
                             |)
                             |go
                             |
 |create table BankSI.CardOpposition
                             |(
                             |	RefCardOpposition int identity
                             |		constraint PK_CardOpposition
                             |			primary key,
                             |	RefCustomer int,
                             |	HCard varchar(50),
                             |	CardOppositionReasonCode int,
                             |	StealDate smalldatetime,
                             |	RequestDate datetime,
                             |	RequestXML varchar(max),
                             |	AnswerDate datetime,
                             |	AnswerXML varchar(max),
                             |	Opposed bit,
                             |	Monext_REF_BLOCKING varchar(20),
                             |	Monext_STAMP varchar(20),
                             |	RC int,
                             |	RefBoUser int,
                             |	Channel varchar(10),
                             |	EtatSAB varchar(10)
                             |)
                             |go
                             |
 |create table BankSI.CardOperations
                             |(
                             |	HCard varchar(50) not null
                             |		constraint PK_CardOperations
                             |			primary key,
                             |	FirstOperationDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.GroupKind
                             |(
                             |	TAGGroupKind varchar(3) not null
                             |		constraint PK_GroupKind
                             |			primary key,
                             |	GroupKindName varchar(200)
                             |)
                             |go
                             |
 |create table BankSI.CardExchangeTry
                             |(
                             |	RefTry int identity
                             |		constraint PK_CardExchangeTry
                             |			primary key,
                             |	RefCardExchange int,
                             |	RequestXML varchar(max),
                             |	AnswerXML varchar(max),
                             |	RequestDate smalldatetime,
                             |	AnswerDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerPackSMSAttempts
                             |(
                             |	RefAttempt int identity
                             |		constraint PK_CustomerPackSMSAttempts
                             |			primary key,
                             |	RefCustomer int not null,
                             |	AttemptDate datetime,
                             |	AttemptAnswerDate datetime,
                             |	RC int
                             |)
                             |go
                             |
 |create table BankSI.T_EckCustomerDead
                             |(
                             |	refCustomer int not null,
                             |	DeathDate date,
                             |	DeathActNumber varchar(50),
                             |	InsertionDate datetime default getutcdate() not null,
                             |	DeathPlace varchar(6),
                             |	SuccessionDate date,
                             |	BeneficiaryFirstName varchar(50),
                             |	BeneficiaryLastName varchar(50),
                             |	BeneficiaryEmail varchar(326),
                             |	BeneficiaryPhoneNumber varchar(50),
                             |	NotaryFirstName varchar(50),
                             |	NotaryLastName varchar(50),
                             |	NotaryEmail varchar(326),
                             |	NotaryPhoneNumber varchar(50),
                             |	BeneficiaryAddress varchar(200),
                             |	BeneficiaryZipCode varchar(50),
                             |	BeneficiaryCity varchar(50),
                             |	NotaryAddress varchar(200),
                             |	NotaryZipCode varchar(50),
                             |	NotaryCity varchar(50)
                             |)
                             |go
                             |
 |create table BankSI.CustomerCobalt
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerCobalt
                             |			primary key,
                             |	StartDate smalldatetime default getutcdate()
                             |)
                             |go
                             |
 |create table BankSI.OverdrawnsActionsLogs
                             |(
                             |	RefCustomer int not null,
                             |	DuringDays int not null,
                             |	Amount float not null,
                             |	Action varchar(20) not null,
                             |	DateAction datetime not null,
                             |	Status varchar(1000) not null,
                             |	Reported bit not null,
                             |	ReportedDate datetime
                             |)
                             |go
                             |
 |create table BankSI.LockCustomer
                             |(
                             |	RefLockCustomer int identity
                             |		constraint PK_LockCustomer
                             |			primary key,
                             |	RefCustomer int not null,
                             |	RefBOUserLock int,
                             |	RefBOUserUnlock int,
                             |	RefLockReason int,
                             |	LockDate smalldatetime,
                             |	UnlockDate smalldatetime,
                             |	OldPaymentLimit varchar(15),
                             |	OldWithDrawMoneyLimit varchar(15),
                             |	Active bit
                             |)
                             |go
                             |
 |
 |create table BankSI.CardModel
                             |(
                             |	RefCardModel int identity
                             |		constraint PK_CardModel
                             |			primary key,
                             |	ModelDescription varchar(250),
                             |	ModelLink varchar(250),
                             |	ModelTextLink varchar(250),
                             |	NumOrder int,
                             |	AllowedAccountKind varchar(100),
                             |	IsAvailable bit,
                             |	ModelTAG varchar(10),
                             |	EmbossingColor varchar(7)
                             |)
                             |go
                             |
 |create table BankSI.CustomerDocuments
                             |(
                             |	RefDocument int identity
                             |		constraint PK_CustomerDocuments
                             |			primary key,
                             |	RefCustomer int not null,
                             |	CategoryTAG varchar(3),
                             |	DocumentTypeTAG varchar(3),
                             |	DeliveryDate smalldatetime,
                             |	ExpireDate smalldatetime,
                             |	DocumentNumber varchar(50),
                             |	AdditionalInfo varchar(400),
                             |	isActive bit,
                             |	IssuedCountry varchar(2)
                             |)
                             |go
                             |
 |
 |create table BankSI.GroupCustomer
                             |(
                             |	RefGroup int not null,
                             |	RefCustomer int not null,
                             |	isStar bit,
                             |	constraint PK_GroupCustomer
                             |		primary key (RefGroup, RefCustomer)
                             |)
                             |go
                             |
 |create table BankSI.CustomerExternalBlackList
                             |(
                             |	RefExtBlackList int identity
                             |		constraint PK_CustomerExternalBlackList
                             |			primary key,
                             |	FirstName varchar(50),
                             |	LastName varchar(50),
                             |	BirthDate date,
                             |	BirthPlace varchar(100)
                             |)
                             |go
                             |
 |create table BankSI.DebitsAbuseActionLogs
                             |(
                             |	RefLog int identity,
                             |	RefCustomer int not null,
                             |	Profile varchar(50) not null,
                             |	Action varchar(50) not null,
                             |	Date datetime not null,
                             |	Request varchar(max) not null,
                             |)
                             |go
                             |
 |create table BankSI.SpecialDay
                             |(
                             |	RefSpecialDay int identity
                             |		constraint PK_SpecialDay
                             |			primary key,
                             |	DayTAG varchar(20),
                             |	SpecialDay date
                             |)
                             |go
                             |
 |create table BankSI.CloseReason
                             |(
                             |	RefCloseReason int identity
                             |		constraint PK_CloseReason
                             |			primary key,
                             |	TAGReason varchar(10),
                             |	ReasonDescription varchar(100),
                             |	BlockNextSubscriptions bit,
                             |	NoLetter bit
                             |)
                             |go
                             |
 |
 |create table BankSI.OverdrawnsExceptionList
                             |(
                             |	AccountNumber varchar(12) not null,
                             |	Reason varchar(100) not null
                             |)
                             |go
                             |
 |create table BankSI.CustomerOldEmail
                             |(
                             |	RefEmailChange int identity
                             |		constraint PK_CustomerOldEmail
                             |			primary key,
                             |	Email varchar(355),
                             |	NewEmail varchar(355),
                             |	RefCustomer int not null,
                             |	ChangeDate datetime,
                             |	RefBOUser int
                             |)
                             |go
                             |
 |create table BankSI.T_EckCptInactifs
                             |(
                             |	refCustomer int not null,
                             |	DateMaximale datetime not null,
                             |	DateDerniereRelance datetime,
                             |	DateTransfertCDC smalldatetime
                             |)
                             |go
                             |
                             |
                             |
 |create table BankSI.CustomerOldPhoneNumber
                             |(
                             |	RefPhoneChange int identity
                             |		constraint PK_CustomerOldPhoneNumber
                             |			primary key,
                             |	PhoneNumber varchar(30),
                             |	NewPhoneNumber varchar(30),
                             |	RefCustomer int not null,
                             |	ChangeDate datetime,
                             |	RefBOUser int,
                             |	IPAddress varchar(20)
                             |)
                             |go
                             |
 |create table BankSI.T_EckLogSyspertec
                             |(
                             |	idFile int identity,
                             |	FileName varchar(300) not null,
                             |	GenerationDate datetime default getdate() not null,
                             |	ResponseDate datetime,
                             |	refClientMin int,
                             |	refClientMax int,
                             |	Message varchar(300)
                             |)
                             |go
                             |
 |create table BankSI.AbusiveDebitDischarges
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_busiveDebitDischarges
                             |			primary key,
                             |	FirstA2pDate datetime,
                             |	CountA2p smallint not null,
                             |	AlreadyDeposit bit not null,
                             |	Warning bit,
                             |	WarningDate datetime,
                             |	Closed bit,
                             |	ClosedDate datetime
                             |)
                             |go
                             |
 |create table BankSI.CleanOCRDirectory
                             |(
                             |	RegistrationCode varchar(20),
                             |	OCR1 bit,
                             |	OCR2 bit,
                             |	CleanDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.OverdrawnsCurrentAccounts
                             |(
                             |	RefCustomer int not null,
                             |	BeginDate datetime not null,
                             |	DuringDays int not null,
                             |	AmountAtBeginning float not null,
                             |	CurrentAmount float not null,
                             |	Changing varchar(1)
                             |)
                             |go
                             |
 |create table BankSI.Audit
                             |(
                             |	RefAudit int identity
                             |		constraint PK_Audit
                             |			primary key,
                             |	refUID int,
                             |	refCustomer int,
                             |	refHardware int,
                             |	AuditDate datetime,
                             |	AuditTAG varchar(2),
                             |	Audit varchar(max)
                             |)
                             |go
                             |
 |create table BankSI.DebitsAbuseCre
                             |(
                             |	RefCre int identity,
                             |	RefCustomer int not null,
                             |	Amount money not null,
                             |	BillingDate datetime not null,
                             |	Done int not null,
                             |	Label varchar(100) not null,
                             |	IdCre int
                             |)
                             |go
    """.stripMargin

  private val bankSiSql2 = s"""
                             |
 |create table BankSI.PinBySMSReSent
                             |(
                             |	RefResent int identity
                             |		constraint PK_PinBySMSReSent
                             |			primary key,
                             |	RefPinBySMS int not null,
                             |	RefUID int not null,
                             |	RegistrationCode varchar(20),
                             |	SentDate datetime,
                             |	RefReason int not null,
                             |	Remark varchar(200),
                             |	RC int
                             |)
                             |go
                             |
 |create table BankSI.PinBySMSReSentReason
                             |(
                             |	RefReason int,
                             |	Reason varchar(200)
                             |)
                             |go
                             |
 |
                             |
 |create table BankSI.Fees
                             |(
                             |	RefFee int not null
                             |		constraint PK_CustomerFees
                             |			primary key,
                             |	FeeKind varchar(5),
                             |	TAGFee varchar(5),
                             |	FeeValue money,
                             |	FeeDescription varchar(200),
                             |	OperationTAG varchar(50),
                             |	Libelle1 varchar(200),
                             |	FeeMaxValue money
                             |)
                             |go
                             |
 |create table BankSI.WelcomeLetter
                             |(
                             |	RefWelcome int identity
                             |		constraint PK_WelcomeLetter
                             |			primary key,
                             |	SentDate smalldatetime,
                             |	TAGProvider varchar(2),
                             |	TAGMODEL varchar(10),
                             |	TAGTarif varchar(20),
                             |	LetterID varchar(50),
                             |	RefCustomer int,
                             |	Address1 varchar(50),
                             |	Address2 varchar(50),
                             |	Address3 varchar(50),
                             |	Address4 varchar(50),
                             |	Zipcode varchar(50),
                             |	City varchar(50),
                             |	RC int
                             |)
                             |go
                             |
 |create table BankSI.CustomerATD
                             |(
                             |	RefATD int identity
                             |		constraint PK_CustomerATD
                             |			primary key,
                             |	RefCustomer int not null,
                             |	RefCustomerSpecificity int,
                             |	FirstNotificationDate datetime,
                             |	ExpectedPaymentDate datetime,
                             |	Paid int,
                             |	PaymentDate datetime,
                             |	RequestPaymentDate datetime,
                             |	RefCRE int
                             |)
                             |go
                             |
 |create table BankSI.CustomerDebitCardPreferences
                             |(
                             |	RefCardPreference int identity
                             |		constraint PK_CustomerDebitCardPreferences
                             |			primary key,
                             |	RefCustomer int not null,
                             |	ForbidInternetPayment bit,
                             |	ForbidForeignPayment bit,
                             |	ForbidAllPayment bit
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerRetail
                             |(
                             |	RefCustomer int not null,
                             |	VerifStaffRequiredDate smalldatetime,
                             |	VerifStaffLastCheck smalldatetime
                             |)
                             |go
                             |
 |
 |create table BankSI.CardvsOpenERP
                             |(
                             |	PackNumber varchar(50) not null,
                             |	AgencyIDReception varchar(20),
                             |	AgencyIDVente varchar(20),
                             |	MODE varchar(9) not null,
                             |	DateInsert datetime default getdate() not null,
                             |	Cas varchar(100),
                             |	Code varchar(50),
                             |	Libelle varchar(50),
                             |	Error_MSG varchar(max)
                             |)
                             |go
                             |
 |create table BankSI.LetterProvider
                             |(
                             |	TagProvider varchar(2),
                             |	ProviderName varchar(50)
                             |)
                             |go
                             |
 |create table BankSI.CustomerRegistrationIssues
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerRegistrationIssues
                             |			primary key,
                             |	AddressIssue bit,
                             |	SuscriptionKO bit,
                             |	POSVisitRequired bit,
                             |	SubscriptionDate smalldatetime,
                             |	StartDate smalldatetime,
                             |	J0 datetime,
                             |	J15 datetime,
                             |	J30 datetime,
                             |	J60 datetime,
                             |	J75 datetime,
                             |	J90 datetime,
                             |	SolvedDate smalldatetime,
                             |	CommunicationDoneList varchar(200),
                             |	BlockingDate smalldatetime,
                             |	Closedate smalldatetime,
                             |	Fees money,
                             |	DocumentName varchar(50),
                             |	isActive bit
                             |)
                             |go
                             |
 |create table BankSI.CustomerPro
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerPro
                             |			primary key,
                             |	SIREN varchar(9),
                             |	IDAccountant varchar(40),
                             |	SIRENAccountant varchar(9),
                             |	CAAnnuelCourant money,
                             |	RaisonSocial1 varchar(32),
                             |	RaisonSocial2 varchar(32),
                             |	NbPartners tinyint,
                             |	NbEmployees smallint,
                             |	TAGBusinessActivity varchar(2),
                             |	BankName varchar(100),
                             |	CodeNAF varchar(5),
                             |	AccountantStatus smallint,
                             |	LastAccountantAnswerDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.AgencyAdditionalInfos
                             |(
                             |	RefAgency int not null
                             |		constraint PK_AgencyAdditionalInfos
                             |			primary key,
                             |	idePDVSAB varchar(50),
                             |	FirstActivation smalldatetime,
                             |	nbTentative int,
                             |	dateLastTentative datetime,
                             |	codeInsee varchar(10),
                             |	LastVisitDate datetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerWorldCheck
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerWorldCheck
                             |			primary key,
                             |	FirstNameLastNameWC varchar(100),
                             |	BIRTHYEAR varchar(4),
                             |	LastNameDOBWC varchar(100),
                             |	LastNameSoundexWC varchar(100)
                             |)
                             |go
                             |
 |
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
                             |
 |create table BankSI.CustomerFees
                             |(
                             |	RefCustomerFees int identity
                             |		constraint PK_CustomerFees_1
                             |			primary key,
                             |	RefCustomer int not null,
                             |	TAGFee varchar(5),
                             |	FeeAmount money,
                             |	RefLinkedCloseAccount int,
                             |	Done smallint,
                             |	ExecutionDate smalldatetime,
                             |	CreationDate smalldatetime,
                             |	SI_RequestXML varchar(max),
                             |	SI_AnswerXML varchar(max)
                             |)
                             |go
                             |
 |
 |create table BankSI.SMSBankingEventsSummary
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerSMS
                             |			primary key,
                             |	NbSentSMSTot int,
                             |	NbSentSMSThisMonth int,
                             |	NbSentSMSThisYear int,
                             |	NbBoughtPackThisYear int,
                             |	NbSMSinActivePack int
                             |)
                             |go
                             |
 |create table BankSI.Reprint_Ticket
                             |(
                             |	RefReprint int identity
                             |		constraint PK_Reprint_Ticket
                             |			primary key,
                             |	RefTransaction int not null,
                             |	RefCashier int,
                             |	RefHardware int,
                             |	RefAgency int,
                             |	CashierName varchar(50),
                             |	AgencyName varchar(100),
                             |	AgencyID varchar(50),
                             |	PrintDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CheckDLLLive
                             |(
                             |	DLLTAG varchar(50) not null
                             |		constraint PK_CheckDLLLive
                             |			primary key,
                             |	CheckDate datetime
                             |)
                             |go
                             |
 |create table BankSI.AddressCheckMethod
                             |(
                             |	FirstAddressCheckMethod smallint,
                             |	AddressCheckMethodLabel varchar(2000)
                             |)
                             |go
                             |
 |create table BankSI.CustomerCloseLogs
                             |(
                             |	RefCustomerCloseLog int identity
                             |		constraint PK_CustomerCloseLogs
                             |			primary key,
                             |	RefClose int not null,
                             |	LogDate datetime default getutcdate(),
                             |	CustomerCloseLog varchar(500)
                             |)
                             |go
                             |
 |create table BankSI.SMSEvent
                             |(
                             |	RefEvent int not null,
                             |	EventName varchar(50),
                             |	IsBillable bit,
                             |	IsPriorityProcessing bit,
                             |	SMSAccount varchar(20)
                             |)
                             |go
                             |
 |create table BankSI.CustomerZendDesk
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerZendDesk
                             |			primary key,
                             |	ZendeskID varchar(20),
                             |	BarcodeUpdateDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerLinks
                             |(
                             |	RefPrimaryCustomer int not null,
                             |	RefSecondaryCustomer int not null,
                             |	LinkType int,
                             |	constraint PK_CustomerLinks
                             |		primary key (RefPrimaryCustomer, RefSecondaryCustomer)
                             |)
                             |go
                             |
 |
 |create table BankSI.LogsWorldCheck
                             |(
                             |	RefTransactionWC int identity
                             |		constraint PK_RefTransactionWC
                             |			primary key,
                             |	RefTransaction int,
                             |	WorldCheckStatus int,
                             |	EventDate datetime default getutcdate(),
                             |	TextWC varchar(200)
                             |)
                             |go
                             |
 |create table BankSI.CustomerProPartner
                             |(
                             |	RefProPartner int not null,
                             |	RefCustomer int not null,
                             |	RefPoliteness int,
                             |	FirstName varchar(50),
                             |	LastName varchar(50),
                             |	Address1 varchar(50),
                             |	Address2 varchar(50),
                             |	Address3 varchar(50),
                             |	Address4 varchar(50),
                             |	At varchar(100),
                             |	City varchar(50),
                             |	Zipcode varchar(20),
                             |	CountryISO3 varchar(3),
                             |	CompanyPercent tinyint,
                             |	constraint PK_CustomerProPartner
                             |		primary key (RefProPartner, RefCustomer)
                             |)
                             |go
                             |
 |create table BankSI.SMSBankingEvents
                             |(
                             |	Ref int identity
                             |		constraint PK_SMSBankingEvents_1
                             |			primary key,
                             |	RefCustomer int not null,
                             |	RefEvent int,
                             |	EventDate datetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerBankStatusHistory
                             |(
                             |	RefCustomer int,
                             |	OldStatus smallint,
                             |	NewStatus smallint,
                             |	ChangeDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerClose
                             |(
                             |	RefClose int identity
                             |		constraint PK_CustomerClose
                             |			primary key,
                             |	RefCustomer int not null,
                             |	AskDate smalldatetime default getutcdate(),
                             |	NotifyBefore bit,
                             |	EndDate smalldatetime,
                             |	NoticeEndDate smalldatetime,
                             |	ReturnFunds bit,
                             |	AskIBANDate1 smalldatetime,
                             |	AskIBANDate2 smalldatetime,
                             |	CreditReference varchar(500),
                             |	TransferAmount money,
                             |	BeneficiaryName varchar(400),
                             |	IBAN varchar(32),
                             |	BIC varchar(20),
                             |	RefBOUser int,
                             |	Reasons varchar(max),
                             |	DoneStatus smallint,
                             |	DoneDate smalldatetime,
                             |	RefBOSupervisor int,
                             |	ApprobationDate smalldatetime,
                             |	CancellationDate smalldatetime,
                             |	Cancelled bit,
                             |	IsCACF bit,
                             |	NoLetter bit
                             |)
                             |go
                             |
 |create table BankSI.CustomerKYC
                             |(
                             |	RefCustomer int not null
                             |		constraint PK_CustomerKYC
                             |			primary key,
                             |	AccountUsageTAGList varchar(50),
                             |	AccountUsageTAGListUpdateDate smalldatetime,
                             |	ProfessionPlusTAG varchar(10),
                             |	ProfessionPlusTAGUpdateDate smalldatetime,
                             |	Profession varchar(200),
                             |	ProfessionUpdateDate smalldatetime,
                             |	Estate varchar(5),
                             |	EstateUpdateDate smalldatetime,
                             |	OldProfessionTAG varchar(5),
                             |	OldProfessionTAGUpdateDate smalldatetime,
                             |	OldProfession varchar(200),
                             |	OldProfessionPlusTAG varchar(5),
                             |	CNUsage varchar(5),
                             |	CNUsageUpdateDate smalldatetime,
                             |	CNUsagePro varchar(5),
                             |	CNUsageProUpdateDate smalldatetime,
                             |	IncomeTAG varchar(5),
                             |	IncomeTAGUpdateDate smalldatetime,
                             |	codProUpdateDate smalldatetime,
                             |	modHabUpdateDate smalldatetime,
                             |	sitFamUpdateDate smalldatetime,
                             |	TaxResidenceISO2 varchar(2),
                             |	TaxResidenceISO2Date smalldatetime,
                             |	CNUsageProAlert bit,
                             |	FrenchTaxOnly bit,
                             |	FrenchTaxOnlyDate smalldatetime,
                             |	USResident bit,
                             |	USResidentDate smalldatetime,
                             |	USBorn bit,
                             |	USBornDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerPersonalizedCardActivationAttempts
                             |(
                             |	RefAttemptActivation int identity
                             |		constraint PK_CustomerPersonalizedCardActivationAttempts
                             |			primary key,
                             |	RefCustomer int not null,
                             |	AttemptDateStart smalldatetime,
                             |	NewBarcode varchar(15),
                             |	MonextActivation bit,
                             |	MonextActivation_IN varchar(max),
                             |	MonextActivation_OUT varchar(max),
                             |	RC_MonextActivation int,
                             |	PinCodeSent bit,
                             |	RC_PinCodeSent int,
                             |	SAB_ReplaceCard bit,
                             |	RC_SAB_ReplaceCard int,
                             |	SAB_ReplaceCard_IN varchar(max),
                             |	SAB_ReplaceCard_OUT varchar(max),
                             |	AttemptDateEnd smalldatetime,
                             |	CheckNbActiveCards int,
                             |	SABActivationRC int,
                             |	SABActivationDate smalldatetime
                             |)
                             |go
                             |
 |create table BankSI.CustomerAddInfo2
                             |(
                             |	RefCustomer int not null,
                             |	MaidenName varchar(50),
                             |	isCloseLetterSent bit,
                             |	NbShowHowToSwitchAccount int,
                             |	isPersonalizedCardOrder bit,
                             |	NationalityISO2 varchar(2),
                             |	NewLastName varchar(50),
                             |	LastSIAddressChangeDate datetime,
                             |	iRefCreComSubscription int,
                             |	AdvancedKYCBlockingDate smalldatetime,
                             |	KYCRiskLevel varchar(1),
                             |	LastKYCCompletedDate smalldatetime,
                             |	PushKYCCompletionDate smalldatetime,
                             |	RefPushUser int,
                             |	KYCLimitCompletionDate smalldatetime,
                             |	MarriedLastName varchar(50),
                             |	AccountClosing bit
                             |)
                             |go
                             |
 |
 |create table BankSI.CustomerScore
                             |(
                             |	Refcustomer int not null,
                             |	CLIENT_SCORE int,
                             |	LastChangeDate smalldatetime,
                             |	ScoreDetails varchar(4000)
                             |)
                             |go
                             |
 |create table BankSI.Archive
                             |(
                             |	RefArchive int identity
                             |		constraint PK_Archive
                             |			primary key,
                             |	RefCustomer int not null,
                             |	Status tinyint not null,
                             |	InsertedDate datetime not null,
                             |	TreatedDate datetime,
                             |	ArchiveReturnCode varchar(1000),
                             |	InSafeBox bit not null,
                             |	ArchiveMetaData varchar(max),
                             |	FileName varchar(max)
                             |)
                             |go
                             |
 |create table BankSI.Politeness
                             |(
                             |	RefPoliteness int,
                             |	Politeness varchar(10),
                             |	PolitenessLabel varchar(20),
                             |	PolitenessTAG varchar(5)
                             |)
                             |go
 """.stripMargin

  private val relations =
    s"""
       |relationship OneToOne {
       | 	CustomerChildren{RefCustomer} to Customer
       | 	CustomerBirthDepartment{RefCustomer} to Customer
       | 	CustomerCardOpposition{RefCustomer} to Customer
       | 	CustomerTax{RefCustomer} to Customer
       | 	CardActivation{RefCustomer} to Customer
       | 	ISILIS{RefCustomer} to Customer
       | 	CustomerATDDocument{RefCustomer} to Customer
       | 	CustomerAddInfo{RefCustomer} to Customer
       | 	CustomerAddressConfirmation{RefCustomer} to Customer
       | 	CustomerOldBarcode{RefCustomer} to Customer
       | 	OverdrawnsPastAccounts{RefCustomer} to Customer
       | 	CustomerRenewExpireCard{RefCustomer} to Customer
       | 	CardExchange{RefCustomer} to Customer
       | 	CustomerChangeLimit{RefCustomer} to Customer
       | 	CustomerRenewSubscription{RefCustomer} to Customer
       | 	CustomerGroup{RefCustomer} to Customer
       | 	CardOpposition{RefCustomer} to Customer
       | 	CustomerPackSMSAttempts{RefCustomer} to Customer
       | 	CustomerCobalt{RefCustomer} to Customer
       | 	OverdrawnsActionsLogs{RefCustomer} to Customer
       | 	LockCustomer{RefCustomer} to Customer
       | 	DeBooleansAbuseActionLogs{RefCustomer} to Customer
       | 	CustomerOldEmail{RefCustomer} to Customer
       | 	CustomerOldPhoneNumber{RefCustomer} to Customer
       | 	AbusiveDeBooleanDischarges{RefCustomer} to Customer
       | 	OverdrawnsCurrentAccounts{RefCustomer} to Customer
       | 	DeBooleansAbuseCre{RefCustomer} to Customer
       | 	WelcomeLetter{RefCustomer} to Customer
       | 	CustomerATD{RefCustomer} to Customer
       | 	CustomerDeBooleanCardP{RefCustomer} to Customer
       | 	CustomerRetail{RefCustomer} to Customer
       | 	CustomerRegistrationIssues{RefCustomer} to Customer
       | 	CustomerPro{RefCustomer} to Customer
       | 	CustomerWorldCheck{RefCustomer} to Customer
       | 	Customer{RefCustomerParent} to Customer
       | 	CustomerFees{RefCustomer} to Customer
       | 	CustomerZendDesk{RefCustomer} to Customer
       | 	CustomerProPartner{RefCustomer} to Customer
       | 	CustomerBankStatusHistory{RefCustomer} to Customer
       | 	CustomerAddInfo2{RefCustomer} to Customer
       | 	Archive{RefCustomer} to Customer
       | 	CustomerScore{Refcustomer} to Customer
       | 	CustomerClose{RefCustomer} to Customer
       | 	SMSBankingEvents{RefCustomer} to Customer
       | 	SMSBankingEventsSummary{RefCustomer} to Customer
       | 	Audit{refCustomer} to Customer
       | 	CustomerDocuments{refCustomer} to Customer
       | 	GroupCustomer{refCustomer} to Customer
       | 	T_EckRNIPPrspn{refCustomer} to Customer
       | 	T_EckCptInactifs{refCustomer} to Customer
       | 	AgentScore{Refcustomer} to Customer
       | 	ScoreChurn{Refcustomer} to Customer
       |}
    """.stripMargin


  def compute(): Unit = {
    val file = getFile("bankSiJDL")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(createEntityFromSQLServer(bankSiSql, "BankSI"))
    bw.write(createEntityFromSQLServer(bankSiSql2, "BankSI"))
    bw.write(createEntityFromSQLServer(d3, "BankSI"))
   // bw.write(createEntityFromSQLServer(missingSql, "BankSI"))
    bw.write(relations)
    bw.close()
}
}



