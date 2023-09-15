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
    Z_THE_ENTITY {
        BIGINT ID PK
        VARCHAR NAME
    }

```