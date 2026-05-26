# Tài liệu Chi tiết Cơ sở dữ liệu SQLite - Hi ! Hotel

Ứng dụng **Hi ! Hotel** sử dụng cơ sở dữ liệu **SQLite** cục bộ để lưu trữ và quản lý thông tin tài khoản, khách sạn, hạng phòng và lịch sử đặt phòng của khách hàng. Cơ sở dữ liệu được khởi tạo và quản lý bởi lớp [DatabaseHelper.kt](file:///d:/hotelvip/app/src/main/java/com/example/hotelvip/ui/db/DatabaseHelper.kt) và được truy xuất qua [HotelRepository.kt](file:///d:/hotelvip/app/src/main/java/com/example/hotelvip/ui/db/HotelRepository.kt).

---

## 1. Sơ đồ Cấu trúc các Bảng (Database Schema)

Hệ thống cơ sở dữ liệu bao gồm 4 bảng chính: `users` (Tài khoản người dùng), `hotels` (Khách sạn), `rooms` (Hạng phòng) và `bookings` (Đơn đặt phòng).

### 1.1 Bảng `users` (Tài khoản người dùng & Admin)
Lưu trữ thông tin đăng ký, đăng nhập và phân quyền (Vai trò: Khách hàng hoặc Admin).

| Tên Cột | Kiểu Dữ Liệu | Ràng Buộc | Mô Tả | Ví Dụ |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Mã định danh tài khoản | `1` |
| `email` | `TEXT` | UNIQUE NOT NULL | Email đăng nhập (Duy nhất) | `"admin@hotelvip.com"` |
| `password` | `TEXT` | NOT NULL | Mật khẩu tài khoản | `"admin123"` |
| `fullname` | `TEXT` | | Họ và tên người dùng | `"Admin Manager"` |
| `phone` | `TEXT` | | Số điện thoại liên hệ | `"0900000000"` |
| `role` | `TEXT` | | Quyền hạn tài khoản (`ADMIN` / `CUSTOMER`) | `"ADMIN"` |

### 1.2 Bảng `hotels` (Khách sạn)
Lưu trữ thông tin tổng quan của các khách sạn 5 sao.

| Tên Cột | Kiểu Dữ Liệu | Ràng Buộc | Mô Tả | Ví Dụ |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Mã định danh khách sạn | `1` |
| `title` | `TEXT` | NOT NULL | Tên khách sạn | `"Vinpearl Resort & Spa Phú Quốc"` |
| `rating` | `REAL` | | Điểm đánh giá (từ 1.0 đến 5.0) | `4.8` |
| `reviews_count` | `INTEGER` | | Số lượng lượt đánh giá từ khách | `1250` |
| `location` | `TEXT` | | Địa chỉ/Khu vực của khách sạn | `"Phú Quốc, Kiên Giang"` |
| `price` | `REAL` | | Giá phòng trung bình (VND) | `2500000.0` |
| `description` | `TEXT` | | Mô tả chi tiết về khách sạn | `"Nằm tại Bãi Dài hoang sơ..."` |
| `amenities` | `TEXT` | | Tiện nghi (Ngăn cách bởi dấu phẩy) | `"Wifi miễn phí,Hồ bơi ngoài trời..."` |
| `gradient_colors`| `TEXT` | | Mã màu gradient Hex để vẽ thẻ Card | `"#FF9800,#F44336"` |

### 1.3 Bảng `rooms` (Hạng phòng)
Lưu trữ thông tin chi tiết về các phòng/hạng phòng thuộc từng khách sạn.

| Tên Cột | Kiểu Dữ Liệu | Ràng Buộc | Mô Tả | Ví Dụ |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Mã định danh phòng | `1` |
| `hotel_id` | `INTEGER` | FOREIGN KEY REFERENCES `hotels`(`id`) | Liên kết tới khách sạn sở hữu | `1` |
| `title` | `TEXT` | NOT NULL | Tên hạng phòng | `"Deluxe Room Ocean View"` |
| `price` | `REAL` | | Giá phòng một đêm (VND) | `2500000.0` |
| `description` | `TEXT` | | Mô tả chi tiết về phòng | `"Phòng Deluxe sang trọng..."` |
| `size` | `TEXT` | | Diện tích phòng | `"46 m²"` |
| `capacity` | `INTEGER` | | Sức chứa tối đa (số người lớn) | `2` |
| `features` | `TEXT` | | Đặc quyền (Ngăn cách bởi dấu phẩy) | `"1 Giường đôi,Ban công..."` |
| `gradient_colors`| `TEXT` | | Mã màu gradient Hex để vẽ thẻ Card | `"#FF9800,#FF5722"` |

### 1.4 Bảng `bookings` (Đơn đặt phòng)
Lưu trữ thông tin lịch sử giao dịch và liên kết tới tài khoản đã thực hiện đặt.

| Tên Cột | Kiểu Dữ Liệu | Ràng Buộc | Mô Tả | Ví Dụ |
| :--- | :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | PRIMARY KEY AUTOINCREMENT | Mã định danh đơn đặt phòng | `1` |
| `user_id` | `INTEGER` | FOREIGN KEY REFERENCES `users`(`id`) | Tài khoản người đặt phòng | `2` |
| `hotel_id` | `INTEGER` | FOREIGN KEY REFERENCES `hotels`(`id`) | Khách sạn được đặt | `1` |
| `room_id` | `INTEGER` | FOREIGN KEY REFERENCES `rooms`(`id`) | Hạng phòng được chọn | `1` |
| `guest_name` | `TEXT` | NOT NULL | Họ tên người đặt phòng | `"Khách Hàng Vip"` |
| `guest_email` | `TEXT` | NOT NULL | Địa chỉ Email liên hệ | `"vip@gmail.com"` |
| `guest_phone` | `TEXT` | NOT NULL | Số điện thoại liên lạc | `"0987654321"` |
| `total_price` | `REAL` | | Tổng số tiền thanh toán (VND) | `5000000.0` |
| `status` | `TEXT` | | Trạng thái đơn phòng | `"Hoàn tất"` |
| `date` | `TEXT` | | Ngày đặt phòng (Định dạng YYYY-MM-DD) | `"2026-05-10"` |

---

## 2. Danh sách Dữ liệu nạp sẵn (Seed Data)

Khi cơ sở dữ liệu được tạo lần đầu tiên, hệ thống sẽ tự động nạp sẵn dữ liệu mẫu sau:

### 2.1 Tài khoản Đăng nhập (Bảng `users`)
1. **Tài khoản Admin (Quản trị viên)**:
   * Email: `admin@hotelvip.com`
   * Mật khẩu: `admin123`
   * Họ và tên: `Admin Manager`
   * Vai trò: `ADMIN`
2. **Tài khoản Khách hàng (Thành viên VIP)**:
   * Email: `vip@gmail.com`
   * Mật khẩu: `vip123`
   * Họ và tên: `Khách Hàng Vip`
   * Vai trò: `CUSTOMER`

### 2.2 Danh sách Khách sạn (Bảng `hotels`)
1. **Vinpearl Resort & Spa Phú Quốc** (ID: 1) - Giá: `2,500,000 VND`.
2. **InterContinental Danang Resort** (ID: 2) - Giá: `5,500,000 VND`.
3. **Sofitel Legend Metropole Hà Nội** (ID: 3) - Giá: `4,000,000 VND`.
4. **The Reverie Saigon** (ID: 4) - Giá: `6,000,000 VND`.

### 2.3 Danh sách Đơn đặt phòng mẫu (Bảng `bookings`)
* Đơn đặt 1: Tài khoản *Khách Hàng Vip* đặt phòng *Deluxe Room Ocean View* tại *Vinpearl Phú Quốc*. Tổng tiền: `5,000,000 VND`. Trạng thái: *Hoàn tất*.
* Đơn đặt 2: Tài khoản *Khách Hàng Vip* đặt phòng *Classic Resort Room* tại *InterContinental Danang*. Tổng tiền: `5,500,000 VND`. Trạng thái: *Hoàn tất*.
* Đơn đặt 3: Tài khoản *Khách Hàng Vip* đặt phòng *Premium Room Opera Wing* tại *Sofitel Metropole Hà Nội*. Tổng tiền: `8,000,000 VND`. Trạng thái: *Chờ xác nhận*.
