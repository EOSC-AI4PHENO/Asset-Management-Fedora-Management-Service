CREATE SCHEMA IF NOT EXISTS "file";

DROP TABLE IF EXISTS "file"."D_File" CASCADE;

CREATE TABLE "file"."D_File"
(
	id bigserial NOT NULL,
	name varchar(200) NOT NULL,
	"directory" varchar(500) NULL,
    "realm" varchar(500) NULL,
	"type" VARCHAR(255) NOT NULL, -- FILE, DIRECTORY
	"owner" VARCHAR(255) NULL,
	"sourceId" BIGINT, -- id z tabeli z source-adapter
    "origin" VARCHAR(255) NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"originCreatedAt" timestamp with time zone,
	"createdAt" timestamp with time zone NOT NULL default NOW(),
	"modifiedAt" timestamp with time zone NOT NULL default NOW()
);

ALTER TABLE "file"."D_File" ADD CONSTRAINT "PK_D_File"	PRIMARY KEY (id);