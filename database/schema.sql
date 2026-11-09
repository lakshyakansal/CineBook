CREATE TABLE IF NOT EXISTS app_user(id BIGSERIAL PRIMARY KEY,name VARCHAR(120) NOT NULL,email VARCHAR(180) UNIQUE NOT NULL,password_hash VARCHAR(100) NOT NULL,role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',created_at TIMESTAMPTZ DEFAULT NOW());
CREATE TABLE IF NOT EXISTS movie(id BIGSERIAL PRIMARY KEY,title VARCHAR(200) NOT NULL,description TEXT,poster_url TEXT,backdrop_url TEXT,language VARCHAR(60),genre VARCHAR(120),duration_minutes INT NOT NULL DEFAULT 120,certification VARCHAR(12),release_date DATE DEFAULT CURRENT_DATE,status VARCHAR(20) DEFAULT 'NOW_SHOWING');
CREATE TABLE IF NOT EXISTS theatre(id BIGSERIAL PRIMARY KEY,name VARCHAR(180) NOT NULL,city VARCHAR(100) NOT NULL,address TEXT NOT NULL);
CREATE TABLE IF NOT EXISTS screen(id BIGSERIAL PRIMARY KEY,theatre_id BIGINT REFERENCES theatre(id) ON DELETE CASCADE,name VARCHAR(80),total_rows INT NOT NULL,seats_per_row INT NOT NULL);
CREATE TABLE IF NOT EXISTS cinema_seat(id BIGSERIAL PRIMARY KEY,screen_id BIGINT REFERENCES screen(id) ON DELETE CASCADE,row_label VARCHAR(5),seat_number INT,category VARCHAR(20) DEFAULT 'REGULAR',UNIQUE(screen_id,row_label,seat_number));
CREATE TABLE IF NOT EXISTS show_time(id BIGSERIAL PRIMARY KEY,movie_id BIGINT REFERENCES movie(id),screen_id BIGINT REFERENCES screen(id),starts_at TIMESTAMPTZ NOT NULL,base_price NUMERIC(10,2) NOT NULL,status VARCHAR(20) DEFAULT 'SCHEDULED');
CREATE TABLE IF NOT EXISTS booking(id BIGSERIAL PRIMARY KEY,booking_code VARCHAR(30) UNIQUE NOT NULL,user_id BIGINT REFERENCES app_user(id),show_id BIGINT REFERENCES show_time(id),status VARCHAR(20) DEFAULT 'CONFIRMED',total_amount NUMERIC(10,2),created_at TIMESTAMPTZ DEFAULT NOW());
CREATE TABLE IF NOT EXISTS booking_seat(id BIGSERIAL PRIMARY KEY,booking_id BIGINT REFERENCES booking(id) ON DELETE CASCADE,seat_id BIGINT REFERENCES cinema_seat(id),UNIQUE(booking_id,seat_id));
CREATE TABLE IF NOT EXISTS payment(id BIGSERIAL PRIMARY KEY,booking_id BIGINT UNIQUE REFERENCES booking(id),provider VARCHAR(30),provider_reference VARCHAR(150),status VARCHAR(20),amount NUMERIC(10,2));
CREATE INDEX IF NOT EXISTS idx_show_movie_start ON show_time(movie_id,starts_at);
CREATE INDEX IF NOT EXISTS idx_booking_user ON booking(user_id);
INSERT INTO theatre(name,city,address) SELECT 'PVR Central Mall','Delhi','Central Mall, New Delhi' WHERE NOT EXISTS(SELECT 1 FROM theatre);
INSERT INTO screen(theatre_id,name,total_rows,seats_per_row) SELECT id,'Audi 1',8,10 FROM theatre WHERE NOT EXISTS(SELECT 1 FROM screen);
INSERT INTO cinema_seat(screen_id,row_label,seat_number,category) SELECT s.id,chr(64+r),n,CASE WHEN r<=2 THEN 'PREMIUM' ELSE 'REGULAR' END FROM screen s CROSS JOIN generate_series(1,8) r CROSS JOIN generate_series(1,10) n ON CONFLICT DO NOTHING;
INSERT INTO movie(title,description,poster_url,backdrop_url,language,genre,duration_minutes,certification,status)
SELECT 'The Last Horizon','A crew travels beyond the known edge of space.','https://image.tmdb.org/t/p/w500/8b8R8l88Qje9dn9OE8PY05Nxl1X.jpg','https://image.tmdb.org/t/p/w1280/8b8R8l88Qje9dn9OE8PY05Nxl1X.jpg','English','Sci-Fi',148,'UA','NOW_SHOWING'
WHERE NOT EXISTS(SELECT 1 FROM movie);
INSERT INTO show_time(movie_id,screen_id,starts_at,base_price)
SELECT m.id,s.id,date_trunc('day',NOW())+interval '1 day'+v.h,220 FROM movie m CROSS JOIN screen s CROSS JOIN (VALUES(interval '11 hours'),(interval '15 hours'),(interval '19 hours')) v(h)
WHERE NOT EXISTS(SELECT 1 FROM show_time st WHERE st.movie_id=m.id);
