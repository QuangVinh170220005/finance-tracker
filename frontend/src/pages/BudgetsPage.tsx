import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'
import { budgetService } from '@/services/budget.service'
import { categoryService } from '@/services/category.service'
import { Layout } from '@/components/Layout'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card'
import { formatCurrency } from '@/lib/utils'
import { Plus, Trash2 } from 'lucide-react'
import type { BudgetRequest } from '@/types'

export function BudgetsPage() {
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [selectedMonth, setSelectedMonth] = useState(new Date().getMonth() + 1)
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear())
  const queryClient = useQueryClient()

  const { data: budgets, isLoading } = useQuery({
    queryKey: ['budgets', selectedMonth, selectedYear],
    queryFn: () => budgetService.getByMonth(selectedMonth, selectedYear),
  })

  const { data: categories } = useQuery({
    queryKey: ['categories'],
    queryFn: categoryService.getAll,
  })

  const { register, handleSubmit, reset } = useForm<BudgetRequest>({
    defaultValues: {
      month: selectedMonth,
      year: selectedYear,
    },
  })

  const createMutation = useMutation({
    mutationFn: budgetService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['budgets'] })
      reset()
      setIsFormOpen(false)
    },
  })

  const deleteMutation = useMutation({
    mutationFn: budgetService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['budgets'] })
    },
  })

  const onSubmit = (data: BudgetRequest) => {
    createMutation.mutate({
      ...data,
      month: selectedMonth,
      year: selectedYear,
    })
  }

  const expenseCategories = categories?.filter((c) => c.type === 'EXPENSE') || []

  return (
    <Layout>
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h2 className="text-3xl font-bold tracking-tight">Ngân sách</h2>
            <p className="text-muted-foreground">Quản lý ngân sách chi tiêu</p>
          </div>
          <Button onClick={() => setIsFormOpen(true)}>
            <Plus className="w-4 h-4 mr-2" />
            Thêm ngân sách
          </Button>
        </div>

        <Card>
          <CardContent className="pt-6">
            <div className="flex gap-4">
              <div className="space-y-2">
                <label className="text-sm font-medium">Tháng</label>
                <select
                  value={selectedMonth}
                  onChange={(e) => setSelectedMonth(Number(e.target.value))}
                  className="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
                >
                  {Array.from({ length: 12 }, (_, i) => i + 1).map((month) => (
                    <option key={month} value={month}>
                      Tháng {month}
                    </option>
                  ))}
                </select>
              </div>

              <div className="space-y-2">
                <label className="text-sm font-medium">Năm</label>
                <select
                  value={selectedYear}
                  onChange={(e) => setSelectedYear(Number(e.target.value))}
                  className="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
                >
                  {Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - 2 + i).map((year) => (
                    <option key={year} value={year}>
                      {year}
                    </option>
                  ))}
                </select>
              </div>
            </div>
          </CardContent>
        </Card>

        {isFormOpen && (
          <Card>
            <CardHeader>
              <CardTitle>Thêm ngân sách mới</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Danh mục</label>
                    <select
                      {...register('categoryId')}
                      className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                    >
                      <option value="">Chọn danh mục</option>
                      {expenseCategories.map((cat) => (
                        <option key={cat.id} value={cat.id}>
                          {cat.icon} {cat.name}
                        </option>
                      ))}
                    </select>
                  </div>

                  <div className="space-y-2">
                    <label className="text-sm font-medium">Số tiền</label>
                    <Input
                      type="number"
                      step="0.01"
                      {...register('amount', { valueAsNumber: true })}
                      placeholder="0"
                    />
                  </div>
                </div>

                <div className="flex gap-2">
                  <Button type="submit">Thêm</Button>
                  <Button type="button" variant="outline" onClick={() => setIsFormOpen(false)}>
                    Hủy
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}

        <div className="grid gap-4">
          {isLoading ? (
            <Card>
              <CardContent className="p-8 text-center">Đang tải...</CardContent>
            </Card>
          ) : budgets && budgets.length > 0 ? (
            budgets.map((budget) => {
              const percentage = (budget.actualSpent / budget.amount) * 100
              const isOverBudget = percentage > 100

              return (
                <Card key={budget.id}>
                  <CardContent className="pt-6">
                    <div className="flex items-center justify-between mb-4">
                      <div className="flex items-center space-x-3">
                        <div
                          className="w-12 h-12 rounded-full flex items-center justify-center text-2xl"
                          style={{ backgroundColor: budget.category.color + '20' }}
                        >
                          {budget.category.icon}
                        </div>
                        <div>
                          <h3 className="font-semibold text-lg">{budget.category.name}</h3>
                          <p className="text-sm text-muted-foreground">
                            {formatCurrency(budget.actualSpent)} / {formatCurrency(budget.amount)}
                          </p>
                        </div>
                      </div>
                      <div className="flex items-center gap-4">
                        <div className="text-right">
                          <p className={`text-lg font-semibold ${isOverBudget ? 'text-red-600' : 'text-green-600'}`}>
                            {formatCurrency(budget.remaining)}
                          </p>
                          <p className="text-sm text-muted-foreground">Còn lại</p>
                        </div>
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={() => deleteMutation.mutate(budget.id)}
                        >
                          <Trash2 className="w-4 h-4 text-destructive" />
                        </Button>
                      </div>
                    </div>

                    <div className="space-y-2">
                      <div className="flex justify-between text-sm">
                        <span>{percentage.toFixed(1)}%</span>
                        <span className={isOverBudget ? 'text-red-600 font-medium' : ''}>
                          {isOverBudget ? 'Vượt ngân sách!' : ''}
                        </span>
                      </div>
                      <div className="w-full bg-gray-200 rounded-full h-2.5">
                        <div
                          className={`h-2.5 rounded-full ${
                            isOverBudget ? 'bg-red-600' : 'bg-primary'
                          }`}
                          style={{ width: `${Math.min(percentage, 100)}%` }}
                        />
                      </div>
                    </div>
                  </CardContent>
                </Card>
              )
            })
          ) : (
            <Card>
              <CardContent className="p-8 text-center text-muted-foreground">
                Chưa có ngân sách nào cho tháng này
              </CardContent>
            </Card>
          )}
        </div>
      </div>
    </Layout>
  )
}
