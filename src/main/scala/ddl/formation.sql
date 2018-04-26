CREATE TABLE public.configurations (
	quiz_default_media_id int4 NOT NULL,
	quiz_config json NOT NULL,
	quiz_success_threshold int4 NOT NULL,
	control_secret_code int4 NOT NULL,
	last_modifier varchar(255) NOT NULL,
	change_date timestamp NOT NULL
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE public.contents (
	id int4 NOT NULL,
	content_type int4 NOT NULL,
	content json NOT NULL,
	status int4 NOT NULL,
	creation_date timestamp NOT NULL,
	domain_type int4 NOT NULL,
	change_date timestamp NOT NULL,
	last_modifier varchar(255) NOT NULL,
	PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE public.formation_models (
	"type" int4 NOT NULL,
	status int4 NOT NULL,
	ordered_contents json NOT NULL,
	icon varchar(100) NULL,
	title varchar(200) NULL,
	"position" int4 NULL,
	change_date timestamp NOT NULL,
	last_modifier varchar(255) NOT NULL,
	PRIMARY KEY (type)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE public.formations (
	id int4 NOT NULL,
	user_id int4 NOT NULL,
	formation_type int4 NOT NULL,
	status int4 NOT NULL,
	steps json NOT NULL,
	steps_progression json NULL,
	start_date timestamp NOT NULL,
	end_date timestamp NULL,
	final_scoring int4 NULL,
	PRIMARY KEY (id),
	UNIQUE (user_id, formation_type),
	FOREIGN KEY (formation_type) REFERENCES formation_models(type)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE public.medias (
	id int4 NOT NULL,
	content bytea NOT NULL,
	"name" varchar(255) NULL,
	media_type int4 NOT NULL,
	associated_user_id int4 NULL,
	last_modifier varchar(255) NULL,
	PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE public.quiz (
	id int4 NOT NULL,
	user_id int4 NOT NULL,
	quiz_type int4 NOT NULL,
	status int4 NOT NULL,
	steps json NOT NULL,
	steps_progression json NULL,
	start_date timestamp NOT NULL,
	end_date timestamp NULL,
	final_scoring int4 NULL,
	media_id int4 NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users(id)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE public.users (
	id int4 NOT NULL,
	status int4 NOT NULL,
	user_type int4 NOT NULL,
	gender int4 NULL,
	first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
	last_usage_name varchar(255) NULL,
	birthday timestamp NULL,
	contract_end_date timestamp NULL,
	photo_id int4 NULL,
	identity_card_id int4 NULL,
	creation_date timestamp NOT NULL,
	change_date timestamp NOT NULL,
	last_connexion_date timestamp NULL,
	original_terminal_id int4 NOT NULL,
	origin int4 NOT NULL,
	original_id varchar(255) NULL,
	secret_code varchar(255) NULL,
	last_quiz_score int4 NULL,
	last_quiz_date timestamp NULL,
	subscription_validation_date timestamp NULL,
	subscription_id varchar(255) NULL,
	agency_id varchar(255) NOT NULL,
	last_docs_status varchar NULL,
	last_score_synchro_date timestamp NULL,
	last_score_synchro_status int4 NULL,
	old_photo_id varchar NULL,
	terminal_refresh_date timestamp NULL,
	PRIMARY KEY (id)
)
WITH (
	OIDS=FALSE
) ;

CREATE TABLE public.users_documents (
	id int4 NOT NULL,
	user_id int4 NOT NULL,
	status int4 NOT NULL,
	files json NOT NULL,
	document_type int4 NOT NULL,
	creation_date timestamp NOT NULL,
	change_date timestamp NOT NULL,
	last_modifier varchar(255) NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users(id)
)
WITH (
	OIDS=FALSE
) ;
