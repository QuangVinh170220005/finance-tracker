import { api } from '@/lib/axios'
import type { ApiResponse, LoginRequest, RegisterRequest, TokenResponse, User } from '@/types'

export const authService = {
  async login(data: LoginRequest): Promise<TokenResponse> {
    const response = await api.post<ApiResponse<TokenResponse>>('/auth/login', data)
    const token = response.data.data.token
    localStorage.setItem('token', token)
    return response.data.data
  },

  async register(data: RegisterRequest): Promise<User> {
    const response = await api.post<ApiResponse<User>>('/auth/register', data)
    return response.data.data
  },

  logout() {
    localStorage.removeItem('token')
    window.location.href = '/login'
  },

  getToken(): string | null {
    return localStorage.getItem('token')
  },

  isAuthenticated(): boolean {
    return !!this.getToken()
  },
}
