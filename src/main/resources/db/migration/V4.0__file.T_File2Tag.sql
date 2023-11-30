CREATE SCHEMA IF NOT EXISTS "file";

DROP TABLE IF EXISTS "file"."T_File2Tag" CASCADE;

CREATE TABLE "file"."T_File2Tag"
(
	id bigserial NOT NULL,
	"fileId" BIGINT NOT NULL,
	"tagId" BIGINT NOT NULL,
	"value" varchar(500) NOT NULL,
	"userId" VARCHAR(255) NULL,
	"isActive" boolean NOT NULL DEFAULT TRUE,
	"createdAt" timestamp with time zone NOT NULL default NOW(),
	"modifiedAt" timestamp with time zone NOT NULL default NOW()
);

ALTER TABLE "file"."T_File2Tag" ADD CONSTRAINT "PK_T_File2Tag"	PRIMARY KEY (id);

ALTER TABLE "file"."T_File2Tag" ADD CONSTRAINT "FK_T_File2Tag_D_File"
	FOREIGN KEY ("fileId") REFERENCES "file"."D_File" (id) ON DELETE No Action ON UPDATE No Action;

ALTER TABLE "file"."T_File2Tag" ADD CONSTRAINT "FK_T_File2Tag_D_Tag"
	FOREIGN KEY ("tagId") REFERENCES "file"."D_Tag" (id) ON DELETE No Action ON UPDATE No Action;