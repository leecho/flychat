create table knowledge_base
(
    id          bigserial,
    name        varchar(100),
    tags        varchar(500),
    logo        varchar(200),
    user_id     bigint,
    update_time timestamp,
    create_time timestamp default CURRENT_TIMESTAMP
);

create table knowledge_detail
(
    id           bigserial,
    knowledge_id bigint,
    item_id      bigint,
    source       varchar(1000),
    status       integer,
    segment      text,
    embedding    vector(1536)
);


create table knowledge_item
(
    knowledge_id bigint,
    split_type   integer,
    split_step   integer,
    source       varchar(1000),
    create_time  timestamp(6),
    update_time  timestamp,
    id           bigserial
);

comment on table knowledge_item is '知识条目';


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
    topk          int,
    similarity    float,
    relevant_size int

);

create table application_knowledge
(
    id                bigserial,
    application_id    bigint,
    knowledge_base_id bigint
);

create table chat
(
    id             bigserial,
    application_id bigint,
    user_id        bigint,
    create_time    timestamp
);

create table chat_message
(
    id              bigserial,
    conversation_id varchar(32),
    chat_id         bigint,
    content         text,
    prompt          text,
    role            varchar(10),
    token_size      bigint,
    cost            decimal(10, 5)
);

create table chat_message_quote
(
    id             bigserial,
    message_id     bigint,
    segment        text,
    similarity     double precision,
    source         text,
    knowledge_id   bigint,
    knowledge_name varchar(1000)
);

