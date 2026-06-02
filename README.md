# DataMatch AI - Core Backend

DataMatch AI là một hệ thống backend được xây dựng dựa trên nguyên lý **Modular Monolith** kết hợp với **Clean Architecture**. Hệ thống hỗ trợ tự động bóc tách kỹ năng từ CV (ứng viên) và JD (nhà tuyển dụng) bằng AI, đồng thời thực hiện tính toán độ tương thích (Matching Score) giữa ứng viên và công việc.

## 🛠 Tech Stack & Skills

- **Ngôn ngữ:** Java 21
- **Framework:** Spring Boot 3
- **AI Integration:** Spring AI (Google Gemini Model)
- **Database:** PostgreSQL (Spring Data JPA, Hibernate)
- **Document Processing:** Apache PDFBox (trích xuất text từ PDF)
- **Build Tool:** Maven

---

## 🏛 Kiến trúc hệ thống (Architecture)

Dự án áp dụng chặt chẽ kiến trúc **Clean Architecture** lồng ghép trong một **Modular Monolith** nhằm đảm bảo khả năng bảo trì, dễ test và dễ scale (chuyển đổi thành Microservices trong tương lai nếu cần).

### Cấu trúc Module
Hệ thống được chia thành 3 Domain Modules hoàn toàn độc lập (Không module nào gọi trực tiếp database/entity của module khác):
1. `cv`: Quản lý hồ sơ ứng viên (Upload PDF, AI trích xuất kỹ năng).
2. `job`: Quản lý yêu cầu tuyển dụng (Text input, Upload PDF, AI trích xuất yêu cầu).
3. `matching`: Module trung tâm thực hiện tính toán độ phù hợp giữa CV và JD.

### Cấu trúc Clean Architecture (bên trong mỗi module)
Mỗi module đều tuân thủ 4 layer nghiêm ngặt (Dependencies chỉ hướng từ ngoài vào trong):

- **Domain Layer (`domain`)**:
  - Chứa core business logic và Domain Entities (`Resume`, `JobDescription`, `MatchResult`).
  - **Quy tắc:** Tuyệt đối KHÔNG chứa code của Framework (Spring, JPA, Jackson). 100% pure Java. Rich Domain Model (không dùng Anemic Domain Model).
- **Application Layer (`application`)**:
  - Chứa Use Cases (`*ApplicationService`) và định nghĩa các Interface Port (`*Port`).
  - Đóng vai trò điều phối logic giữa Domain và Infrastructure.
- **Infrastructure Layer (`infrastructure`)**:
  - Chứa implementation của các Port (Adapters). Giao tiếp với thế giới bên ngoài.
  - Bao gồm: JPA Entities, Spring Data Repositories, Spring AI Clients (`SpringAiGeminiAdapter`), PDFBox Parsers.
- **Presentation Layer (`presentation`)**:
  - Điểm vào của ứng dụng.
  - Chứa REST Controllers và DTOs. Không bao giờ lộ Domain Model hay JPA Entity ra API.

---

## 🧩 Các Design Pattern Được Áp Dụng

1. **Port & Adapter (Hexagonal Architecture Pattern)**
   - **Mục đích:** Tách biệt Core Logic khỏi các thư viện/công nghệ bên ngoài.
   - **Ví dụ:** `SkillExtractorPort` nằm ở Application layer. Implementation là `SpringAiGeminiAdapter` nằm ở Infrastructure layer. Nếu sau này chuyển từ Gemini sang OpenAI, chỉ cần viết Adapter mới, Core Logic không hề thay đổi.

2. **Dependency Injection (Constructor Injection Pattern)**
   - **Mục đích:** Quản lý vòng đời object và dễ dàng Mock khi viết Unit Test.
   - **Thực thi:** Dùng Lombok `@RequiredArgsConstructor` với các field `final`. Tuyệt đối không dùng `@Autowired` trên field để tránh hidden dependencies.

3. **Data Transfer Object (DTO Pattern)**
   - **Mục đích:** Tránh phơi bày cấu trúc Database hoặc Domain Model ra ngoài API, bảo vệ tính toàn vẹn của dữ liệu và giới hạn payload size.
   - **Thực thi:** `JobCreateRequestDTO`, `CvUploadResponseDTO`, v.v.

4. **Facade Pattern (thông qua Application Services)**
   - **Mục đích:** Ẩn giấu độ phức tạp của các tương tác Domain.
   - **Thực thi:** `CvApplicationService` hay `MatchingApplicationService` đóng vai trò như các Facade, cung cấp các API đơn giản cho Controller gọi vào, giấu đi các bước parse PDF, lưu DB, và gọi AI.

5. **Rich Domain Model Pattern**
   - **Mục đích:** Đóng gói trạng thái (State) và hành vi (Behavior) lại với nhau để đảm bảo business rules luôn đúng đắn.
   - **Thực thi:** Entity `Resume` tự quản lý trạng thái (`CvStatus`), tự validate đầu vào khi `updateRawText()` thay vì để service set trạng thái một cách lỏng lẻo bằng Setter.

---

## 🚀 Setup & Run

1. Đảm bảo cấu hình Database PostgreSQL trong `src/main/resources/application.yaml`.
2. Cung cấp API Key cho Gemini (Environment Variable `GEMINI_API_KEY`).
3. Chạy lệnh:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```
