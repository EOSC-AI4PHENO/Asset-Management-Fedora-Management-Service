ALTER TABLE "file"."T_File2Tag" ADD COLUMN IF NOT EXISTS "type" VARCHAR(255);

COMMENT ON COLUMN "file"."T_File2Tag"."type" IS 'value: OWNER, USER';