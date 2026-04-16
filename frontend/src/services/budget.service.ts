import { api } from '@/lib/axios'
import type { ApiResponse, Budget, BudgetRequest } from '@/types'

export const budgetService = {
  async getByMonth(month: number, year: number): Promise<Budget[]> {
    const response = await api.get<ApiResponse<Budget[]>>('/budgets', {
      params: { month, year },
    })
    return response.data.data
  },

  async create(data: BudgetRequest): Promise<Budget> {
    const response = await api.post<ApiResponse<Budget>>('/budgets', data)
    return response.data.data
  },

  async update(id: string, data: Partial<Budget>): Promise<Budget> {
    const response = await api.put<ApiResponse<Budget>>(`/budgets/${id}`, data)
    return response.data.data
  },

  async delete(id: string): Promise<void> {
    await api.delete(`/budgets/${id}`)
  },
}
