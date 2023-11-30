CREATE SCHEMA IF NOT EXISTS "file";

DROP TABLE IF EXISTS "file"."D_Tag" CASCADE;

CREATE TABLE "file"."D_Tag"
(
	id bigserial NOT NULL,
	name varchar(200) NOT NULL,
	"desc" varchar(500) NULL,
    "realm" varchar(500) NULL,
	"isPublic" boolean NOT NULL DEFAULT TRUE,
	"isRequired" boolean NOT NULL DEFAULT TRUE,
	"owner" VARCHAR(255) NULL,
	"type" VARCHAR(255) NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"createdAt" timestamp with time zone NOT NULL default NOW(),
	"modifiedAt" timestamp with time zone NOT NULL default NOW()
);

ALTER TABLE "file"."D_Tag" ADD CONSTRAINT "PK_D_Tag"	PRIMARY KEY (id);