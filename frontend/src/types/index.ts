export interface ApiResponse<T> {
  success: boolean
  data: T
  message?: string
}

export interface User {
  id: string
  email: string
  fullName: string
  provider: 'LOCAL' | 'GOOGLE'
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  password: string
  fullName: string
}

export interface TokenResponse {
  token: string
  refreshToken: string
  type: string
}

export interface Category {
  id: string
  name: string
  type: 'INCOME' | 'EXPENSE'
  icon: string
  color: string
}

export interface Transaction {
  id: string
  amount: number
  description: string
  transactionDate: string
  category: Category
  createdAt: string
}

export interface TransactionRequest {
  amount: number
  description: string
  transactionDate: string
  categoryId: string
}

export interface Budget {
  id: string
  amount: number
  actualSpent: number
  remaining: number
  month: number
  year: number
  category: Category
}

export interface BudgetRequest {
  amount: number
  month: number
  year: number
  categoryId: string
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}
