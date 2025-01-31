CREATE SCHEMA IF NOT EXISTS pedroNewsWhipDB;

CREATE TABLE articles (
    url varchar(255) NOT NULL,
    social_score int NOT NULL,
    country_code varchar(2) NOT NULL,
    PRIMARY KEY (Url)
);

INSERT INTO articles (url, social_score, country_code)
VALUES ('http://www.rte.ie/news/politics/2018/1004/1001034-cso/', 20, 'ie'),
       ('https://elpais.com/cultura/festival-de-musica-de-canarias/', 10, 'es'),
       ('https://www.rte.ie/news/weather/2025/0110/1007-weather-cold-snap/', 60, 'ie'),
       ('https://www.irishtimes.com/opinion/cartoon/2025/martyn-turner/', 50, 'ie'),
       ('http://finance.yahoo.com/q/h?s=^IXIC', 40, 'pt')


