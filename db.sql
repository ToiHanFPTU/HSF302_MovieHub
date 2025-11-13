-- ===============================
-- DATABASE: CinemaTicketDB_2
-- ===============================
CREATE DATABASE CinemaTicketDB_2;
GO
USE CinemaTicketDB_2;
GO

-- ===============================
-- TABLE: User
-- ===============================
CREATE TABLE [dbo].[User](
                             user_id INT IDENTITY(1,1) PRIMARY KEY,
    full_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    phone NVARCHAR(20) NULL,
    role NVARCHAR(20) NULL,
    created_at DATETIME DEFAULT GETDATE()
    );
GO

-- ===============================
-- TABLE: Category
-- ===============================
CREATE TABLE [dbo].[Category](
                                 category_id INT IDENTITY(1,1) PRIMARY KEY,
    category_name NVARCHAR(100) NOT NULL
    );
GO

-- ===============================
-- TABLE: Movie
-- ===============================
CREATE TABLE [dbo].[Movie](
                              movie_id INT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX) NULL,
    duration INT NULL,
    release_date DATE NULL,
    language NVARCHAR(50) NULL,
    image_url NVARCHAR(500) NULL
    );
GO

-- ===============================
-- TABLE: Movie_Category
-- ===============================
CREATE TABLE [dbo].[MovieCategory](
                                      movie_id INT NOT NULL,
                                      category_id INT NOT NULL,
                                      PRIMARY KEY (movie_id, category_id),
    FOREIGN KEY (movie_id) REFERENCES [dbo].[Movie](movie_id),
    FOREIGN KEY (category_id) REFERENCES [dbo].[Category](category_id)
    );
GO

-- ===============================
-- TABLE: Room
-- ===============================
CREATE TABLE [dbo].[Room](
                             room_id INT IDENTITY(1,1) PRIMARY KEY,
    room_name NVARCHAR(50) NOT NULL,
    capacity INT NOT NULL
    );
GO

-- ===============================
-- TABLE: Seat
-- ===============================
CREATE TABLE [dbo].[Seat](
                             seat_id INT IDENTITY(1,1) PRIMARY KEY,
    room_id INT NOT NULL,
    seat_number NVARCHAR(10) NOT NULL,
    seat_type NVARCHAR(50) NULL,
    FOREIGN KEY (room_id) REFERENCES [dbo].[Room](room_id)
    );
GO

-- ===============================
-- TABLE: Showtime
-- ===============================
CREATE TABLE [dbo].[Showtime](
                                 showtime_id INT IDENTITY(1,1) PRIMARY KEY,
    movie_id INT NOT NULL,
    room_id INT NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES [dbo].[Movie](movie_id),
    FOREIGN KEY (room_id) REFERENCES [dbo].[Room](room_id)
    );
GO

-- ===============================
-- TABLE: Payment
-- ===============================
create table Payment
(
    payment_id         int identity
        primary key,
    amount             numeric(12, 2),
    created_at         datetimeoffset(6) default getdate(),
    order_info         varchar(255),
    paymentdate        datetimeoffset(6) default sysdatetime(),
    paymentmethod      varchar(50),
    paymentstatus      varchar(20)       default 'PENDING',
    transactioncode    varchar(100),
    updated_at         datetimeoffset(6) default getdate(),
    vnp_bank_code      varchar(20),
    vnp_bank_tran_no   varchar(255),
    vnp_card_type      varchar(20),
    vnp_pay_date       varchar(14),
    vnp_response_code  varchar(2),
    vnp_transaction_no varchar(100),
    transactionid      int
        references [Transaction]
)
    GO


-- ===============================
-- TABLE: Transaction (simplified)
-- ===============================
CREATE TABLE [dbo].[Transaction](
                                    transaction_id INT IDENTITY(1,1) PRIMARY KEY,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method NVARCHAR(50) NULL,
    transaction_date DATETIME DEFAULT GETDATE(),
    status NVARCHAR(20) NULL
    );
GO

-- ===============================
-- TABLE: Ticket
-- ===============================
CREATE TABLE [dbo].[Ticket](
                               ticket_id INT IDENTITY(1,1) PRIMARY KEY,
    showtime_id INT NOT NULL,
    seat_id INT NOT NULL,
    user_id INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    booking_time DATETIME DEFAULT GETDATE(),
    transaction_id INT NULL,
    FOREIGN KEY (showtime_id) REFERENCES [dbo].[Showtime](showtime_id),
    FOREIGN KEY (seat_id) REFERENCES [dbo].[Seat](seat_id),
    FOREIGN KEY (user_id) REFERENCES [dbo].[User](user_id),
    FOREIGN KEY (transaction_id) REFERENCES [dbo].[Transaction](transaction_id)
    );
GO

-- ===============================
-- INSERT SAMPLE DATA
-- ===============================

-- USERS
INSERT INTO [dbo].[User] (full_name, email, password_hash, phone, role)
VALUES
(N'Admin', 'admin@moviehub.com', 'admin123', '0123456789', 'ADMIN'),
(N'Nguyen Van A', 'user@moviehub.com', 'user123', '0987654321', 'USER');
GO

-- CATEGORIES
INSERT INTO [dbo].[Category] (category_name)
VALUES
(N'Hành động'),
(N'Hài'),
(N'Tình cảm'),
(N'Kinh dị'),
(N'Hoạt hình'),
(N'Phiêu lưu'),
(N'Tội phạm'),
(N'Khoa học viễn tưởng'),
(N'Chiến tranh'),
(N'Gia đình');
GO

-- MOVIES
INSERT INTO [dbo].[Movie] (title, description, duration, release_date, language, image_url)
VALUES
(N'Avengers: Endgame', N'Thế giới siêu anh hùng chống lại Thanos.', 180, '2019-04-26', N'English', ''),
(N'Spiderman: No Way Home', N'Peter Parker đối mặt đa vũ trụ.', 150, '2021-12-17', N'English', ''),
(N'Titanic', N'Câu chuyện tình yêu giữa Jack và Rose.', 195, '1997-12-19', N'English', ''),
(N'Parasite', N'Một gia đình nghèo thâm nhập vào nhà giàu.', 132, '2019-05-30', N'Korean', ''),
(N'Inception', N'Giấc mơ trong giấc mơ.', 148, '2010-07-16', N'English', ''),
(N'Interstellar', N'Hành trình xuyên không gian cứu lấy nhân loại.', 169, '2014-11-07', N'English', ''),
(N'Frozen', N'Nữ hoàng băng giá Elsa và hành trình tìm bản thân.', 102, '2013-11-27', N'English', ''),
(N'Joker', N'Câu chuyện về nguồn gốc tên hề tội phạm.', 122, '2019-10-04', N'English', ''),
(N'Avatar', N'Hành tinh Pandora và người Na’vi.', 162, '2009-12-18', N'English', ''),
(N'Fast & Furious 9', N'Đua xe, hành động và gia đình.', 145, '2021-06-25', N'English', '');
GO

-- MOVIE_CATEGORY (ghép ngẫu nhiên)
INSERT INTO [dbo].[MovieCategory] (movie_id, category_id)
SELECT TOP 10 movie_id, ((movie_id - 1) % 10) + 1 FROM [dbo].[Movie];
GO

-- ROOMS
INSERT INTO [dbo].[Room] (room_name, capacity)
VALUES
(N'Phòng 1', 50),
(N'Phòng 2', 50),
(N'Phòng 3', 50);
GO

-- SEATS (mỗi phòng 60 ghế: 40 Thường, 10 VIP, 10 Couple)
DECLARE @room INT = 1;
DECLARE @i INT;
DECLARE @seatType NVARCHAR(50);

WHILE @room <= 3
BEGIN
    SET @i = 1;

    WHILE @i <= 60
    BEGIN
        IF @i <= 40
            SET @seatType = N'Thường';
        ELSE IF @i <= 50
            SET @seatType = N'VIP';
        ELSE
            SET @seatType = N'Couple';

        INSERT INTO [dbo].[Seat] (room_id, seat_number, seat_type)
        VALUES (@room, CONCAT('S', FORMAT(@i,'00')), @seatType);

        SET @i += 1;
    END

    SET @room += 1;
END
GO


-- 🕒 SHOWTIME (2-3 khung giờ mỗi phim, 10 phim, xen kẽ các phòng)
INSERT INTO [dbo].[Showtime] (movie_id, room_id, start_time, end_time)
VALUES
-- Avengers: Endgame
(1,1,'2025-11-06T09:00:00','2025-11-06T11:30:00'),
(1,2,'2025-11-06T12:00:00','2025-11-06T14:30:00'),
(1,3,'2025-11-06T15:00:00','2025-11-06T17:30:00'),

-- Spiderman: No Way Home
(2,1,'2025-11-06T10:00:00','2025-11-06T12:30:00'),
(2,2,'2025-11-06T13:00:00','2025-11-06T15:30:00'),
(2,3,'2025-11-06T16:00:00','2025-11-06T18:30:00'),

-- Titanic
(3,1,'2025-11-06T11:00:00','2025-11-06T14:15:00'),
(3,2,'2025-11-06T15:00:00','2025-11-06T18:15:00'),

-- Parasite
(4,1,'2025-11-06T09:30:00','2025-11-06T11:30:00'),
(4,2,'2025-11-06T12:00:00','2025-11-06T14:00:00'),

-- Inception
(5,2,'2025-11-06T14:00:00','2025-11-06T16:30:00'),
(5,3,'2025-11-06T17:00:00','2025-11-06T19:30:00'),

-- Interstellar
(6,1,'2025-11-06T16:00:00','2025-11-06T19:00:00'),
(6,2,'2025-11-06T19:30:00','2025-11-06T22:30:00'),

-- Frozen
(7,3,'2025-11-06T10:00:00','2025-11-06T12:00:00'),
(7,1,'2025-11-06T12:30:00','2025-11-06T14:30:00'),

-- Joker
(8,2,'2025-11-06T15:00:00','2025-11-06T17:00:00'),
(8,3,'2025-11-06T17:30:00','2025-11-06T19:30:00'),

-- Avatar
(9,1,'2025-11-06T09:00:00','2025-11-06T11:45:00'),
(9,2,'2025-11-06T12:15:00','2025-11-06T15:00:00'),

-- Fast & Furious 9
(10,3,'2025-11-06T16:00:00','2025-11-06T18:25:00'),
(10,1,'2025-11-06T18:45:00','2025-11-06T21:10:00');
GO
