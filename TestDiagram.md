```mermaid
erDiagram
    ADDRESS {
        bigint id
        varchar address_id
        varchar street
        varchar city_id
    }
    CITY ||--o{ ADDRESS
    A_THE_ENTITY {
        bigint id
        varchar name
    }
    B_THE_ENTITY {
        bigint id
        varchar name
    }
    CITY {
        bigint id
        varchar city_id
        varchar name
    }
    CLASS {
        int class_id
        varchar class_cd
        varchar class_desc
        varchar class_nm
        int course_id
    }
    COURSE ||--o{ CLASS
    CLASSCOMMENT {
        int classcomment_id
        varchar classcomment_tx
        int score_int
        int roster_id
    }
    ROSTER ||--o{ CLASSCOMMENT
    CLASSINSTANCE {
        int classinstance_id
        timestamp classinstance_dt
        int classinstance_mins
        varchar classinstance_note
        int class_id
        int instructor_id
        int room_id
    }
    CLASS ||--o{ CLASSINSTANCE
    PERSON ||--o{ CLASSINSTANCE
    ROOM ||--o{ CLASSINSTANCE
    COURSE {
        int course_id
        varchar course_cd
        varchar course_desc
        varchar course_nm
    }
    C_THE_ENTITY {
        bigint id
        varchar name
    }
    EMPLOYEE {
        bigint id
        varchar name
        varchar address_id
    }
    ADDRESS ||--o{ EMPLOYEE
    F_THE_ENTITY {
        bigint id
        varchar name
    }
    PERSON {
        int person_id
        int active_ind
        varchar email
        varchar firstname
        varchar lastname
        varchar password
        numeric phone_sms
        varchar username
    }
    PERSONROLE {
        int person_id
        int role_id
    }
    PERSON ||--o{ PERSONROLE
    ROLE ||--o{ PERSONROLE
    ROLE {
        int role_id
        varchar role_cd
        varchar role_nm
    }
    ROOM {
        int room_id
        varchar room_cd
        varchar room_desc
        int floor_nbr
        varchar room_nm
        int room_seats_nbr
    }
    ROSTER {
        int roster_id
        boolean attend_ind
        int classinstance_id
        int person_id
    }
    CLASSINSTANCE ||--o{ ROSTER
    PERSON ||--o{ ROSTER
    Z_THE_ENTITY {
        bigint id
        varchar name
    }

```