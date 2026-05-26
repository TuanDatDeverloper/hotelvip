# Biểu đồ Ca Tuần Tự (Sequence Diagram) - Đăng Nhập & Đăng Ký

Tài liệu này mô tả chi tiết luồng xử lý và tương tác giữa các thành phần trong hệ thống khi người dùng thực hiện chức năng **Đăng nhập** và **Đăng ký** trên ứng dụng **Hi ! Hotel**.

---

## 1. Biểu đồ Ca Tuần Tự: Đăng Nhập (Login Sequence Diagram)

Biểu đồ dưới đây thể hiện luồng tương tác từ giao diện người dùng (`LoginScreen`) qua lớp dữ liệu (`HotelRepository`, `DatabaseHelper`) đến cơ sở dữ liệu SQLite, cũng như các nhánh rẽ hướng điều hướng dựa trên vai trò của người dùng (`CUSTOMER` hoặc `ADMIN`).

```mermaid
sequenceDiagram
    autonumber
    actor User as 👤 Người dùng
    participant UI as 📱 LoginScreen
    participant Repo as 🗃️ HotelRepository
    participant DB as 💾 Database (SQLite)
    participant State as ⚙️ AppState
    participant Nav as 🗺️ NavController

    User->>UI: Nhập Email và Mật khẩu
    User->>UI: Nhấn nút "Đăng nhập"

    activate UI
    rect rgb(240, 248, 255)
        note over UI: Kiểm tra dữ liệu đầu vào (Validate)
        alt Email hoặc Mật khẩu để trống
            UI-->>User: Hiển thị Toast: "Vui lòng điền đầy đủ thông tin!"
        else Thông tin hợp lệ
            UI->>UI: Bật trạng thái Loading (isLoading = true)
            UI->>Repo: loginUser(email, password)
            activate Repo
            
            Repo->>DB: Truy vấn TABLE_USERS (email & password)
            activate DB
            DB-->>Repo: Trả về kết quả (Cursor)
            deactivate DB
            
            alt Tìm thấy người dùng hợp lệ
                Repo-->>UI: Trả về đối tượng User (id, email, fullname, phone, role)
            else Thông tin đăng nhập sai
                Repo-->>UI: Trả về null
            end
            deactivate Repo
            
            UI->>UI: Tắt trạng thái Loading (isLoading = false)
            
            alt User != null (Thành công)
                UI->>State: Cập nhật AppState (userId, userRole, guestInfo)
                UI-->>User: Hiển thị Toast: "Đăng nhập thành công!"
                
                alt Vai trò là ADMIN (role == "ADMIN")
                    UI->>Nav: navigate(Routes.ADMIN_ROOT)
                else Vai trò là CUSTOMER (role == "CUSTOMER")
                    UI->>Nav: navigate(Routes.USER_ROOT)
                end
            else User == null (Thất bại)
                UI-->>User: Hiển thị Toast: "Tài khoản hoặc mật khẩu không chính xác!"
            end
        end
    end
    deactivate UI
```

---

## 2. Biểu đồ Ca Tuần Tự: Đăng Ký (Signup Sequence Diagram)

Biểu đồ dưới đây mô tả luồng đăng ký tài khoản mới cho Khách hàng (`CUSTOMER`). Hệ thống sẽ kiểm tra trùng lặp email và thực hiện lưu trữ thông tin mới vào cơ sở dữ liệu.

```mermaid
sequenceDiagram
    autonumber
    actor User as 👤 Người dùng
    participant UI as 📱 SignupScreen
    participant Repo as 🗃️ HotelRepository
    participant DB as 💾 Database (SQLite)
    participant Nav as 🗺️ NavController

    User->>UI: Nhập Họ tên, Email, SĐT, Mật khẩu & Xác nhận mật khẩu
    User->>UI: Nhấn nút "Đăng ký"

    activate UI
    rect rgb(255, 240, 245)
        note over UI: Kiểm tra dữ liệu đầu vào (Validate)
        alt Các trường thông tin bị thiếu
            UI-->>User: Hiển thị Toast: "Vui lòng điền đầy đủ các thông tin!"
        else Mật khẩu xác nhận không khớp
            UI-->>User: Hiển thị Toast: "Mật khẩu xác nhận không trùng khớp!"
        else Thông tin đầu vào hợp lệ
            UI->>UI: Bật trạng thái Loading (isLoading = true)
            UI->>Repo: registerUser(email, password, fullname, phone)
            activate Repo
            
            Repo->>DB: Chèn bản ghi mới với role "CUSTOMER"
            activate DB
            DB-->>Repo: Trả về hàng ID chèn được (hoặc -1 nếu email đã tồn tại)
            deactivate DB
            
            alt Đăng ký thành công (ID != -1)
                Repo-->>UI: Trả về true
            else Lỗi hoặc trùng Email (ID == -1)
                Repo-->>UI: Trả về false
            end
            deactivate Repo
            
            UI->>UI: Tắt trạng thái Loading (isLoading = false)
            
            alt Kết quả là true (Thành công)
                UI-->>User: Hiển thị Toast: "Đăng ký thành công! Hãy đăng nhập."
                UI->>Nav: popBackStack() (Trở về màn hình Đăng nhập)
            else Kết quả là false (Thất bại)
                UI-->>User: Hiển thị Toast: "Email đăng ký đã tồn tại trong hệ thống!"
            end
        end
    end
    deactivate UI
```
