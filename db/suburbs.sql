--------------------------------------------------------
--  File created - Sunday-July-30-2017   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table SUBURBS
--------------------------------------------------------

  CREATE TABLE "C##NXTBDY"."SUBURBS" 
   (	"ID" NUMBER(38,0), 
	"SSC_CODE" VARCHAR2(8 BYTE), 
	"SSC_CODE16" VARCHAR2(5 BYTE), 
	"SSC_NAME" VARCHAR2(45 BYTE), 
	"STATE_CODE" VARCHAR2(1 BYTE), 
	"STATE_NAME" VARCHAR2(30 BYTE), 
	"AREA_SQKM" NUMBER, 
	"GEOM" "MDSYS"."SDO_GEOMETRY" 
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  DDL for Index SUBURBS_IDX
--------------------------------------------------------

  CREATE INDEX "C##NXTBDY"."SUBURBS_IDX" ON "C##NXTBDY"."SUBURBS" ("GEOM") 
   INDEXTYPE IS "MDSYS"."SPATIAL_INDEX" ;
