# Finance Tracker

Ứng dụng quản lý tài chính cá nhân với Spring Boot backend và React frontend.

## 📁 Cấu trúc Project (Monorepo)

```
finance-tracker/
├── src/                    # Backend (Spring Boot)
├── pom.xml                 # Maven config
├── frontend/               # Frontend (React + Vite)
│   ├── src/
│   ├── package.json
│   └── README.md
└── README.md              # File này
```

## 🚀 Backend (Spring Boot)

### Công nghệ
- Java 21
- Spring Boot 3.5.12
- PostgreSQL
- JWT Authentication
- Flyway Migration

### Chạy Backend

```bash
# Cài đặt dependencies
./mvnw clean install

# Chạy application
./mvnw spring-boot:run

# Backend sẽ chạy tại http://localhost:8080
```

### Environment Variables
Tạo file `.env` ở root:
```
DB_URL=jdbc:postgresql://localhost:5432/finance_tracker
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your_secret_key
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

## 🎨 Frontend (React)

### Công nghệ
- React 18 + TypeScript
- Vite
- TailwindCSS
- React Query
- React Router

### Chạy Frontend

```bash
cd frontend

# Cài đặt dependencies
npm install

# Chạy dev server
npm run dev

# Frontend sẽ chạy tại http://localhost:3000
```

Chi tiết xem [frontend/README.md](frontend/README.md)

## 🚀 Deploy

### Backend → Railway
1. Connect GitHub repo
2. Root Directory: `/` (hoặc để trống)
3. Build Command: `mvn clean package -DskipTests`
4. Start Command: `java -jar target/finance-tracker-0.0.1-SNAPSHOT.jar`
5. Thêm Environment Variables (DB_URL, JWT_SECRET, etc.)

### Frontend → Vercel
1. Import GitHub repo
2. Root Directory: `frontend`
3. Build Command: `npm run build`
4. Output Directory: `dist`
5. Environment Variable: `VITE_API_URL=https://your-backend.railway.app/api`

## 📝 API Endpoints

### Auth
- `POST /api/auth/register` - Đăng ký
- `POST /api/auth/login` - Đăng nhập

### Transactions
- `GET /api/transactions` - Lấy danh sách giao dịch
- `POST /api/transactions` - Tạo giao dịch mới
- `PUT /api/transactions/{id}` - Cập nhật giao dịch
- `DELETE /api/transactions/{id}` - Xóa giao dịch

### Categories
- `GET /api/categories` - Lấy danh sách danh mục
- `POST /api/categories` - Tạo danh mục mới
- `PUT /api/categories/{id}` - Cập nhật danh mục
- `DELETE /api/categories/{id}` - Xóa danh mục

### Budgets
- `GET /api/budgets?month=1&year=2024` - Lấy ngân sách theo tháng
- `POST /api/budgets` - Tạo ngân sách mới
- `PUT /api/budgets/{id}` - Cập nhật ngân sách
- `DELETE /api/budgets/{id}` - Xóa ngân sách

## 🔧 Development

### Chạy cả Backend + Frontend

Terminal 1 (Backend):
```bash
./mvnw spring-boot:run
```

Terminal 2 (Frontend):
```bash
cd frontend
npm run dev
```

## 📄 License

MIT
