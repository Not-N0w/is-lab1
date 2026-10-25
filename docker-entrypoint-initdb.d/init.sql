CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE coordinates (
    id SERIAL PRIMARY KEY,
    x INTEGER NOT NULL CHECK (x > -212),
    y BIGINT NOT NULL
);

CREATE TABLE discipline (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL CHECK (name <> ''),
    practice_hours INTEGER
);

CREATE TABLE location (
    id SERIAL PRIMARY KEY,
    x REAL NOT NULL,
    y REAL NOT NULL,
    z DOUBLE PRECISION NOT NULL,
    name VARCHAR(255)
);
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL CHECK (name <> ''),
    eye_color VARCHAR(20) NOT NULL CHECK (eye_color IN ('RED', 'BLUE', 'ORANGE', 'WHITE')),
    hair_color VARCHAR(20) CHECK (hair_color IN ('RED', 'BLUE', 'ORANGE', 'WHITE')),
    location_id INTEGER NOT NULL REFERENCES location(id) ON DELETE RESTRICT,
    height REAL NOT NULL CHECK (height > 0),
    nationality VARCHAR(20) CHECK (nationality IN ('USA', 'SPAIN', 'SOUTH_KOREA', 'NORTH_KOREA'))
);

CREATE TABLE lab_work (
    id BIGSERIAL PRIMARY KEY CHECK (id > 0),
    name VARCHAR(255) NOT NULL CHECK (name <> ''),
    coordinates_id INTEGER NOT NULL REFERENCES coordinates(id) ON DELETE RESTRICT,
    creation_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description TEXT,
    difficulty VARCHAR(20) CHECK (difficulty IN ('VERY_EASY', 'HARD', 'TERRIBLE')),
    discipline_id INTEGER NOT NULL REFERENCES discipline(id) ON DELETE RESTRICT,
    minimal_point DOUBLE PRECISION NOT NULL CHECK (minimal_point > 0),
    tuned_in_works INTEGER NOT NULL,
    author_id INTEGER REFERENCES person(id) ON DELETE RESTRICT
);

CREATE OR REPLACE FUNCTION get_minimal_point_sum() RETURNS DOUBLE PRECISION AS $$
    SELECT COALESCE(SUM(minimal_point), 0) FROM lab_work;
$$ LANGUAGE sql;

CREATE OR REPLACE FUNCTION count_lab_works_by_minimal_point()
       RETURNS TABLE(minimal_point DOUBLE PRECISION, counted BIGINT) AS $$
    SELECT
        lab_work.minimal_point,
        COUNT(lab_work.id)
    FROM
        lab_work
    GROUP BY
        lab_work.minimal_point;
$$ LANGUAGE sql;


CREATE OR REPLACE FUNCTION get_unique_authors() RETURNS TABLE(author_id INTEGER) AS $$
       SELECT DISTINCT
           lab_work.author_id
       FROM
           lab_work
       WHERE
           lab_work.author_id IS NOT NULL;
$$ LANGUAGE sql;


CREATE OR REPLACE FUNCTION add_lab_to_discipline(lab_id INTEGER, discipline_id INTEGER)
       RETURNS void AS $$
       UPDATE lab_work SET discipline_id = $2 WHERE id = $1;
$$ LANGUAGE sql;


CREATE OR REPLACE FUNCTION add_top_lab_to_discipline(p_discipline_id INTEGER)
    RETURNS void AS $$
    UPDATE lab_work
    SET discipline_id = $1
    WHERE id IN (
        SELECT id
        FROM lab_work
        WHERE difficulty IS NOT NULL
        ORDER BY CASE difficulty
                     WHEN 'VERY_EASY' THEN 1
                     WHEN 'HARD'      THEN 2
                     WHEN 'TERRIBLE'  THEN 3
                     END DESC
        LIMIT 10
        );
$$ LANGUAGE sql;
