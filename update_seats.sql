-- ===============================
-- SCRIPT CẬP NHẬT LẠI DỮ LIỆU GHẾ
-- Chạy script này để cập nhật format ghế từ S01-S60 thành A1-A20, B1-B20, C1-C10, D1-D10
-- ===============================

USE CinemaTicketDB_2;
GO

-- Xóa tất cả dữ liệu ticket (vì có foreign key tới seat)
DELETE FROM [dbo].[Ticket];
GO

-- Xóa tất cả ghế cũ
DELETE FROM [dbo].[Seat];
GO

-- Tạo lại ghế với format mới
-- Format: A1-A20, B1-B20 (Ghế Thường - 40 ghế)
--         C1-C10 (Ghế VIP - 10 ghế)
--         D1-D10 (Ghế Couple - 10 ghế)

DECLARE @room INT = 1;
DECLARE @row CHAR(1);
DECLARE @seatNum INT;

WHILE @room <= 3
BEGIN
    -- Hàng A, B (Ghế Thường) - 40 ghế
    SET @row = 'A';
    WHILE @row <= 'B'
    BEGIN
        SET @seatNum = 1;
        WHILE @seatNum <= 20
        BEGIN
            INSERT INTO [dbo].[Seat] (room_id, seat_number, seat_type)
            VALUES (@room, CONCAT(@row, @seatNum), N'Thường');

            SET @seatNum += 1;
        END
        SET @row = CHAR(ASCII(@row) + 1);
    END

    -- Hàng C (10 ghế VIP)
    SET @seatNum = 1;
    WHILE @seatNum <= 10
    BEGIN
        INSERT INTO [dbo].[Seat] (room_id, seat_number, seat_type)
        VALUES (@room, CONCAT('C', @seatNum), N'VIP');
        SET @seatNum += 1;
    END

    -- Hàng D (10 ghế Couple)
    SET @seatNum = 1;
    WHILE @seatNum <= 10
    BEGIN
        INSERT INTO [dbo].[Seat] (room_id, seat_number, seat_type)
        VALUES (@room, CONCAT('D', @seatNum), N'Couple');
        SET @seatNum += 1;
    END

    SET @room += 1;
END
GO

PRINT 'Đã cập nhật lại tất cả ghế!';
PRINT 'Phòng 1, 2, 3 mỗi phòng có:';
PRINT '  - Hàng A: A1-A20 (Ghế Thường)';
PRINT '  - Hàng B: B1-B20 (Ghế Thường)';
PRINT '  - Hàng C: C1-C10 (Ghế VIP)';
PRINT '  - Hàng D: D1-D10 (Ghế Couple)';
PRINT 'Tổng: 60 ghế/phòng';
GO

-- Kiểm tra kết quả
SELECT room_id, seat_type, COUNT(*) as total
FROM [dbo].[Seat]
GROUP BY room_id, seat_type
ORDER BY room_id, seat_type;
GO

