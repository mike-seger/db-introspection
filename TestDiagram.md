```mermaid
erDiagram
    ADDRESS {
        BIGINT ID PK
        VARCHAR ADDRESS_ID
        VARCHAR STREET
        VARCHAR CITY_ID
    }
    CITY ||--o{ ADDRESS : references
    A_THE_ENTITY {
        BIGINT ID PK
        VARCHAR NAME
    }
    B_THE_ENTITY {
        BIGINT ID PK
        VARCHAR NAME
    }
    CITY {
        BIGINT ID PK
        VARCHAR CITY_ID
        VARCHAR NAME
    }
    CLASS {
        INT CLASS_ID PK
        VARCHAR CLASS_CD
        VARCHAR CLASS_DESC
        VARCHAR CLASS_NM
        INT COURSE_ID
    }
    COURSE ||--o{ CLASS : references
    CLASSCOMMENT {
        INT CLASSCOMMENT_ID PK
        VARCHAR CLASSCOMMENT_TX
        INT SCORE_INT
        INT ROSTER_ID
    }
    ROSTER ||--o{ CLASSCOMMENT : references
    CLASSINSTANCE {
        INT CLASSINSTANCE_ID PK
        TIMESTAMP CLASSINSTANCE_DT
        INT CLASSINSTANCE_MINS
        VARCHAR CLASSINSTANCE_NOTE
        INT CLASS_ID
        INT INSTRUCTOR_ID
        INT ROOM_ID
    }
    CLASS ||--o{ CLASSINSTANCE : references
    PERSON ||--o{ CLASSINSTANCE : references
    ROOM ||--o{ CLASSINSTANCE : references
    COURSE {
        INT COURSE_ID PK
        VARCHAR COURSE_CD
        VARCHAR COURSE_DESC
        VARCHAR COURSE_NM
    }
    C_THE_ENTITY {
        BIGINT ID PK
        VARCHAR NAME
    }
    EMPLOYEE {
        BIGINT ID PK
        VARCHAR NAME
        VARCHAR ADDRESS_ID
    }
    ADDRESS ||--o{ EMPLOYEE : references
    F_THE_ENTITY {
        BIGINT ID PK
        VARCHAR NAME
    }
    PERSON {
        INT PERSON_ID PK
        INT ACTIVE_IND
        VARCHAR EMAIL
        VARCHAR FIRSTNAME
        VARCHAR LASTNAME
        VARCHAR PASSWORD
        NUMERIC PHONE_SMS
        VARCHAR USERNAME
    }
    PERSONROLE {
        INT PERSON_ID PK
        INT ROLE_ID PK
    }
    PERSON ||--o{ PERSONROLE : references
    ROLE ||--o{ PERSONROLE : references
    ROLE {
        INT ROLE_ID PK
        VARCHAR ROLE_CD
        VARCHAR ROLE_NM
    }
    ROOM {
        INT ROOM_ID PK
        VARCHAR ROOM_CD
        VARCHAR ROOM_DESC
        INT FLOOR_NBR
        VARCHAR ROOM_NM
        INT ROOM_SEATS_NBR
    }
    ROSTER {
        INT ROSTER_ID PK
        BOOLEAN ATTEND_IND
        INT CLASSINSTANCE_ID
        INT PERSON_ID
    }
    CLASSINSTANCE ||--o{ ROSTER : references
    PERSON ||--o{ ROSTER : references
    Z_THE_ENTITY {
        BIGINT ID PK
        VARCHAR NAME
    }

```