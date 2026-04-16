# Finance Tracker Frontend

Giao diện quản lý tài chính cá nhân được xây dựng với React + TypeScript + Vite.

## 🚀 Công nghệ

- **React 18** - UI Library
- **TypeScript** - Type Safety
- **Vite** - Build Tool (cực nhanh)
- **TailwindCSS** - Styling
- **React Router** - Routing
- **TanStack Query** - Data Fetching & Caching
- **React Hook Form + Zod** - Form Validation
- **Axios** - HTTP Client
- **Lucide React** - Icons

## 📦 Cài đặt

```bash
# Cài đặt dependencies
npm install

# Copy file .env
cp .env.example .env

# Chỉnh sửa .env với backend URL của bạn
# VITE_API_URL=http://localhost:8080/api
```

## 🛠️ Development

```bash
# Chạy dev server (http://localhost:3000)
npm run dev

# Build production
npm run build

# Preview production build
npm run preview
```

## 📁 Cấu trúc thư mục

```
src/
├── components/       # UI components
│   ├── ui/          # Base UI components (Button, Input, Card)
│   └── Layout.tsx   # Layout chính
├── pages/           # Các trang
│   ├── LoginPage.tsx
│   ├── RegisterPage.tsx
│   ├── DashboardPage.tsx
│   ├── TransactionsPage.tsx
│   ├── CategoriesPage.tsx
│   └── BudgetsPage.tsx
├── services/        # API services
│   ├── auth.service.ts
│   ├── transaction.service.ts
│   ├── category.service.ts
│   └── budget.service.ts
├── lib/             # Utilities
│   ├── axios.ts     # Axios config
│   └── utils.ts     # Helper functions
├── types/           # TypeScript types
│   └── index.ts
├── App.tsx          # Root component
└── main.tsx         # Entry point
```

## 🎨 Features

- ✅ Đăng nhập / Đăng ký
- ✅ Dashboard tổng quan
- ✅ Quản lý giao dịch (CRUD)
- ✅ Quản lý danh mục (CRUD)
- ✅ Quản lý ngân sách
- ✅ Responsive design
- ✅ JWT Authentication
- ✅ Form validation
- ✅ Loading states
- ✅ Error handling

## 🚀 Deploy lên Vercel

1. Push code lên GitHub
2. Import project vào Vercel
3. Config:
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
4. Thêm Environment Variable:
   - `VITE_API_URL`: URL backend của bạn trên Railway

## 📝 Notes

- Backend API phải enable CORS cho frontend domain
- JWT token được lưu trong localStorage
- Auto redirect về /login khi token hết hạn (401)
