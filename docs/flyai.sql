

create table document
(
    id           bigserial,
    split_type   integer,
    split_step   integer,
    name         varchar(1000),
    user_id      bigint,
    create_time  timestamp(6),
    update_time  timestamp
);

create table document_segment
(
    id           bigserial,
    document_id  bigint,
    name       varchar(1000),
    status       integer,
    segment      text,
    embedding    vector(1536)
);


create table application
(
    id            bigserial,
    name          varchar(100),
    tags          varchar(500),
    logo          varchar(200),
    introduction  varchar(500),
    owner         bigint,
    model_name    varchar(100),
    limit_prompt  varchar(1000),
    temperature   float,
    top_p          float,
    similarity    float,
    relevant_size int
);

create table application_document
(
    id                bigserial,
    application_id    bigint,
    document_id bigint
);

create table conversation
(
    id             bigserial,
    application_id bigint,
    user_id        bigint,
    name           varchar(200),
    create_time    timestamp,
    update_time    timestamp
);

create table message
(
    id              bigserial,
    conversation_id bigint,
    application_id         bigint,
    content         text,
    prompt          text,
    role            varchar(10),
    token_size      bigint,
    user_id        bigint,
    create_time    timestamp,
    update_time    timestamp
);

create table quote
(
    id             bigserial,
    message_id     bigint,
    segment        text,
    similarity     double precision,
    document         text
);

