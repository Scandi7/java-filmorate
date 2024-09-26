INSERT INTO genres (name)
SELECT 'Комедия' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Комедия');
INSERT INTO genres (name)
SELECT 'Драма' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Драма');
INSERT INTO genres (name)
SELECT 'Мультфильм' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Мультфильм');
INSERT INTO genres (name)
SELECT 'Триллер' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Триллер');
INSERT INTO genres (name)
SELECT 'Документальный' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Документальный');
INSERT INTO genres (name)
SELECT 'Боевик' WHERE NOT EXISTS (SELECT 1 FROM genres WHERE name = 'Боевик');

INSERT INTO mpa_ratings (rating, description)
SELECT 'G', 'У фильма нет возрастных ограничений' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'G');
INSERT INTO mpa_ratings (rating, description)
SELECT 'PG', 'Детям рекомендуется смотреть фильм с родителями' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'PG');
INSERT INTO mpa_ratings (rating, description)
SELECT 'PG-13', 'Детям до 13 лет просмотр не желателен' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'PG-13');
INSERT INTO mpa_ratings (rating, description)
SELECT 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'R');
INSERT INTO mpa_ratings (rating, description)
SELECT 'NC-17', 'Лицам до 18 лет просмотр запрещён' WHERE NOT EXISTS (SELECT 1 FROM mpa_ratings WHERE rating = 'NC-17');