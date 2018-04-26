package main.scala.database

import java.io.{BufferedWriter, File, FileWriter}

import main.scala.utils.Converter.{createEntityFromSQLServer, getFile}

object DistribDB {

  private val unknownEntity: String = s"""
entity User {
	RefUser Integer
}
                         |entity XAction {
                         |	refAction Integer
                         |}
entity XService {
	RefService Integer
}""".stripMargin

  private val relations: String =
    s"""
       |relationship OneToOne {
       | 	T_ConfederationPersonnePhysique{IdPersonnePhysique} to T_PersonnePhysique
       | 	T_PointDeVente_PersonnePhysique{IdPersonnePhysique} to T_PersonnePhysique
       |}
       |
      |relationship OneToOne {
       | 	T_AlerteAction{IdAlerte} to T_Alerte
       |}
       |
       |relationship OneToOne {
       | 	T_AlerteAction{idAction} to T_Action
       |}
       |
 |relationship OneToOne {
       | 	T_Agrement{IdPdv} to T_PointDeVente
       | 	T_PointDeVente_PersonnePhysique{IdPointDeVente} to T_PointDeVente
       | 	T_DelegationPointDeVente{IdPointDeVente} to T_PointDeVente
       | 	T_HorairesPointDeVente{IdPointDeVente} to T_PointDeVente
       | 	T_InfosPointDeVente{IdPointDeVente} to T_PointDeVente
       |}
       |
 |relationship OneToOne {
       |    T_Agrement{IdStatut} to T_StatutAgrement
       |}
       |
    """.stripMargin

  private val kycSql: String =
    s"""
       |
 |
 |create table KYC.T_Action
       |(
       |	IdAction int identity
       |		constraint PK_T_Action
       |			primary key,
       |	Poids int,
       |	Description varchar(1000)
       |)
       |go
       |
 |create table KYC.T_Alerte
       |(
       |	IdAlerte int identity
       |		constraint PK_T_Alerte
       |			primary key,
       |	DateAlerte datetime,
       |	RefAgentAlerte int,
       |	DateModification datetime,
       |	RefAgentModification int,
       |	AncienneValeur varchar(1000),
       |	NouvelleValeur varchar(1000),
       |	TypeAlerte varchar(10),
       |	DateValidation datetime,
       |	RefAgentValidation int,
       |	DateAction datetime,
       |	Commentaire varchar(5000),
       |	RefAgentAction int,
       |	RefClient int,
       |	RefAgence int,
       |	FausseAlerte bit
       |)
       |go
       |
 |create table KYC.T_AlerteAction
       |(
       |	idAlerte int not null
       |		constraint FK_T_AlerteAction_T_Alerte
       |			references KYC.T_Alerte,
       |	idAction int not null
       |		constraint FK_T_AlerteAction_T_Action
       |			references KYC.T_Action,
       |	constraint PK_T_AlerteAction
       |		primary key (idAlerte, idAction)
       |)
       |go
       |
     """.stripMargin


  private val posSql: String =
    s"""
 |create table agrement.T_Agrement
       |(
       |	IdAgrement int identity,
       |	IdStatut int not null,
       |	IdPdv int not null,
       |	DateCreation datetime not null,
       |	DateModification datetime,
       |	IsImport bit,
       |	constraint PK_T_Agrement
       |		primary key (IdAgrement, IdPdv)
       |)
       |go
       |
 |create table agrement.T_Challenge
       |(
       |	RefChallenge int identity
       |		constraint PK_Challenge
       |			primary key,
       |	Challenge varchar(50),
       |	RefAgrement int,
       |	GenerationDate datetime,
       |	Used bit,
       |	UsedDate datetime,
       |	SendSMS int,
       |	Reason varchar(50)
       |)
       |go
       |
 |create table agrement.T_ConfederationPersonnePhysique
       |(
       |	IdPersonnePhysique int not null
       |		constraint PK_T_ConfederationPersonnePhysique
       |			primary key,
       |	StatutConfe varchar(100)
       |)
       |go
       |
 |create table agrement.T_DelegationPointDeVente
       |(
       |	IdPointDeVente int not null
       |		constraint PK_T_DelegationPersonnePhysique
       |			primary key,
       |	Siret varchar(14),
       |	Description varchar(max),
       |	PourcentageActivite int,
       |	ActivitePrincipale bit,
       |	GestionCommerce bit
       |)
       |go
       |
 |create table agrement.T_HorairesPointDeVente
       |(
       |	IdPointDeVente int not null,
       |	Jour varchar(20) not null,
       |	Ouvert bit not null,
       |	OuvertureDebut time,
       |	OuvertureFin time,
       |	FermetureDebut time,
       |	FermetureFin time,
       |	constraint PK_T_HorairesPointDeVente
       |		primary key (IdPointDeVente, Jour)
       |)
       |go
       |
 |create table agrement.T_InfosPointDeVente
       |(
       |	IdPointDeVente int not null
       |		constraint PK_T_InfosPointDeVente
       |			primary key,
       |	CaisseMarque varchar(50),
       |	CaisseNombre int,
       |	TpeMarque varchar(50),
       |	TpeModele varchar(50),
       |	TpeNombre int,
       |	TpeProprietaire varchar(50),
       |	NombreEmployes int,
       |	NombrePassagesJour int,
       |	TransfertArgent bit,
       |	CartesPrepayees bit,
       |	FDJ bit,
       |	PMU bit,
       |	Bar bit,
       |	Presse bit,
       |	Colis bit,
       |	TimbresFiscaux bit,
       |	PointBanque bit,
       |	Restauration bit,
       |	Epicerie bit,
       |	Pain bit,
       |	VenteAutre varchar(500)
       |)
       |go
       |
 |create table agrement.T_PersonnePhysique
       |(
       |	IdPersonnePhysique int identity
       |		constraint PK_T_PersonnePhysique
       |			primary key,
       |	Civilite varchar(10),
       |	Prenom varchar(100),
       |	Nom varchar(100),
       |	NomJeuneFille varchar(100),
       |	Email varchar(100),
       |	Fonction varchar(100),
       |	TelMobile varchar(15),
       |	DateNaissance varchar(10),
       |	LieuNaissance varchar(100),
       |	Type varchar(50)
       |)
       |go
       |
 |create table agrement.T_PointDeVente
       |(
       |	IdPointDeVente int identity
       |		constraint PK_T_PointDeVente
       |			primary key,
       |	Nom_RaisonSociale varchar(100),
       |	NomCommercial varchar(100),
       |	Activite varchar(100),
       |	CodeNAF varchar(6),
       |	CodeDouanes varchar(20),
       |	Siret varchar(14),
       |	RCS varchar(50),
       |	Immatriculation varchar(10),
       |	FormeJuridique varchar(50),
       |	CapitalSocial varchar(50),
       |	RepartitionCapital varchar(500),
       |	Adresse varchar(200),
       |	CodePostal varchar(5),
       |	Ville varchar(50),
       |	Telephone varchar(15),
       |	Environnement varchar(max)
       |)
       |go
       |
 |create table agrement.T_PointDeVente_PersonnePhysique
       |(
       |	IdPointDeVente int not null,
       |	IdPersonnePhysique int not null,
       |	constraint PK_T_PointDeVente_PersonnePhysique
       |		primary key (IdPointDeVente, IdPersonnePhysique)
       |)
       |go
       |
 |create table agrement.T_StatutAgrement
       |(
       |	IdStatut int identity
       |		constraint PK_T_StatutAgrement
       |			primary key,
       |	NomStatut varchar(100) not null
       |)
       |go
       |
     """.stripMargin

  def compute(): Unit = {
    val file = getFile("distributionJDL")
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(createEntityFromSQLServer(posSql, "agrement"))
    bw.write(createEntityFromSQLServer(kycSql, "KYC"))
   // bw.write(unknownEntity)
    bw.write(relations)
    bw.close()
  }
}

