# Biểu đồ Thực thể Liên kết (ERD) & Ca Sử Dụng (Use Case Diagram) - Hi ! Hotel

Tài liệu này cung cấp các biểu đồ thiết kế hệ thống của ứng dụng **Hi ! Hotel** được mô tả và vẽ trực quan bằng cú pháp **Mermaid**.

---

## 1. Biểu đồ Thực thể Liên kết (Entity Relationship Diagram - ERD)

Biểu đồ dưới đây thể hiện cấu trúc quan hệ giữa các bảng trong cơ sở dữ liệu SQLite:
* Một khách sạn (`hotels`) có nhiều hạng phòng (`rooms`) (Quan hệ `1 - Nhiều`).
* Một khách sạn (`hotels`) xuất hiện trong nhiều đơn đặt phòng (`bookings`) (Quan hệ `1 - Nhiều`).
* Một hạng phòng (`rooms`) xuất hiện trong nhiều đơn đặt phòng (`bookings`) (Quan hệ `1 - Nhiều`).

```mermaid
erDiagram
    HOTELS ||--o{ ROOMS : "chứa"
    HOTELS ||--o{ BOOKINGS : "được đặt"
    ROOMS ||--o{ BOOKINGS : "được chọn"

    HOTELS {
        int id PK "Mã khách sạn"
        string title "Tên khách sạn"
        real rating "Điểm đánh giá"
        int reviews_count "Số lượt đánh giá"
        string location "Địa chỉ"
        real price "Giá phòng trung bình"
        string description "Mô tả khách sạn"
        string amenities "Tiện nghi"
        string gradient_colors "Mã màu thẻ"
    }

    ROOMS {
        int id PK "Mã hạng phòng"
        int hotel_id FK "Liên kết khách sạn"
        string title "Tên hạng phòng"
        real price "Giá phòng / đêm"
        string description "Mô tả phòng"
        string size "Diện tích phòng"
        int capacity "Sức chứa tối đa"
        string features "Đặc quyền phòng"
        string gradient_colors "Mã màu thẻ"
    }

    BOOKINGS {
        int id PK "Mã đơn đặt phòng"
        int hotel_id FK "Liên kết khách sạn"
        int room_id FK "Liên kết hạng phòng"
        string guest_name "Tên người nhận phòng"
        string guest_email "Email liên hệ"
        string guest_phone "Số điện thoại"
        real total_price "Tổng tiền thanh toán"
        string status "Trạng thái đơn phòng"
        string date "Ngày đặt phòng"
    }
```

---

## 2. Biểu đồ Ca Sử Dụng (Use Case Diagram)

Sơ đồ thể hiện sự tương tác của hai đối tượng người dùng chính trong hệ thống: **Khách hàng (Customer)** và **Quản trị viên (Admin)** đối với các chức năng chính của ứng dụng.

```mermaid
flowchart TD
    %% Actors Definition
    Customer[("👤 Khách Hàng (Customer)")]
    Admin[("👮 Quản Trị Viên (Admin)")]

    subgraph "Ứng Dụng Hi ! Hotel"
        %% Customer Use Cases
        UC_Home["Xem danh sách khách sạn 5 sao"]
        UC_Search["Tìm kiếm khách sạn (Điểm đến, Tên)"]
        UC_Detail["Xem chi tiết khách sạn & Tiện nghi"]
        UC_SelectRoom["Xem & Chọn hạng phòng"]
        UC_FillInfo["Nhập thông tin đặt phòng"]
        UC_Pay["Xác nhận & Thanh toán (Pay Now)"]
        UC_History["Xem lịch sử đặt phòng cá nhân"]

        %% Admin Use Cases
        UC_AdminLogin["Đăng nhập quyền Admin (Email: admin)"]
        UC_ViewStats["Xem Dashboard (Tổng doanh thu, Tổng đơn)"]
        UC_ViewChart["Xem biểu đồ cột doanh thu 6 tháng"]
        UC_ViewRecent["Xem danh sách 5 đơn hàng gần đây nhất"]
    end

    %% Customer Connections
    Customer --> UC_Home
    Customer --> UC_Search
    Customer --> UC_Detail
    Customer --> UC_SelectRoom
    Customer --> UC_FillInfo
    Customer --> UC_Pay
    Customer --> UC_History

    %% Interconnection (Thanh toán xong -> cập nhật lịch sử)
    UC_Pay -.-> |"Tự động ghi nhận"| UC_History

    %% Admin Connections
    Admin --> UC_AdminLogin
    UC_AdminLogin --> UC_ViewStats
    UC_AdminLogin --> UC_ViewChart
    UC_AdminLogin --> UC_ViewRecent

    %% Data flow correlation (Đặt phòng xong -> Admin thấy doanh thu tăng tức thời)
    UC_Pay -.-> |"Cập nhật dữ liệu tức thời"| UC_ViewStats
    UC_Pay -.-> |"Ghi nhận giao dịch mới nhất"| UC_ViewRecent
```
