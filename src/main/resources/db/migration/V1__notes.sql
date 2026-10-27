create table notes (
    id         bigint generated always as identity primary key,
    text       varchar(500) not null,
    created_at timestamp with time zone not null
);

create index notes_created_at on notes (created_at desc);
