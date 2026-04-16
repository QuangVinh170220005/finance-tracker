import { api } from '@/lib/axios'
import type { ApiResponse, Transaction, TransactionRequest, PageResponse } from '@/types'

export const transactionService = {
  async getAll(page = 0, size = 10): Promise<PageResponse<Transaction>> {
    const response = await api.get<ApiResponse<PageResponse<Transaction>>>('/transactions', {
      params: { page, size },
    })
    return response.data.data
  },

  async create(data: TransactionRequest): Promise<Transaction> {
    const response = await api.post<ApiResponse<Transaction>>('/transactions', data)
    return response.data.data
  },

  async update(id: string, data: TransactionRequest): Promise<Transaction> {
    const response = await api.put<ApiResponse<Transaction>>(`/transactions/${id}`, data)
    return response.data.data
  },

  async delete(id: string): Promise<void> {
    await api.delete(`/transactions/${id}`)
  },
}
