TRUNCATE TABLE link_info;

INSERT INTO link_info(id, link, end_time, description, active, short_link, opening_count)
VALUES ('b0e2d11b-4a8f-419f-93c5-4cd83c42639d', 'https://google.com', '2026-01-01T00:00', 'Google', true, 'abcd1234', 5),
       ('2642b218-361c-4d29-b3e6-f768f49711f8', 'https://ya.ru', '2027-01-01T00:00', 'Yandex', true, 'efgh5678', 10);