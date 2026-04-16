import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { z } from 'zod'
import { transactionService } from '@/services/transaction.service'
import { categoryService } from '@/services/category.service'
import { Layout } from '@/components/Layout'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card'
import { formatCurrency, formatDate } from '@/lib/utils'
import { Plus, Trash2, Edit } from 'lucide-react'
import type { TransactionRequest } from '@/types'

const transactionSchema = z.object({
  amount: z.number().positive('Số tiền phải lớn hơn 0'),
  description: z.string().min(1, 'Mô tả không được để trống'),
  transactionDate: z.string(),
  categoryId: z.string().min(1, 'Vui lòng chọn danh mục'),
})

type TransactionForm = z.infer<typeof transactionSchema>

export function TransactionsPage() {
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingId, setEditingId] = useState<string | null>(null)
  const queryClient = useQueryClient()

  const { data: transactions, isLoading } = useQuery({
    queryKey: ['transactions'],
    queryFn: () => transactionService.getAll(0, 50),
  })

  const { data: categories } = useQuery({
    queryKey: ['categories'],
    queryFn: categoryService.getAll,
  })

  const {
    register,
    handleSubmit,
    reset,
    setValue,
    formState: { errors },
  } = useForm<TransactionForm>({
    resolver: zodResolver(transactionSchema),
    defaultValues: {
      transactionDate: new Date().toISOString().split('T')[0],
    },
  })

  const createMutation = useMutation({
    mutationFn: transactionService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] })
      reset()
      setIsFormOpen(false)
    },
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: TransactionRequest }) =>
      transactionService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] })
      reset()
      setEditingId(null)
      setIsFormOpen(false)
    },
  })

  const deleteMutation = useMutation({
    mutationFn: transactionService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['transactions'] })
    },
  })

  const onSubmit = (data: TransactionForm) => {
    const payload: TransactionRequest = {
      amount: data.amount,
      description: data.description,
      transactionDate: data.transactionDate,
      categoryId: data.categoryId,
    }

    if (editingId) {
      updateMutation.mutate({ id: editingId, data: payload })
    } else {
      createMutation.mutate(payload)
    }
  }

  const handleEdit = (transaction: any) => {
    setEditingId(transaction.id)
    setValue('amount', transaction.amount)
    setValue('description', transaction.description)
    setValue('transactionDate', transaction.transactionDate)
    setValue('categoryId', transaction.category.id)
    setIsFormOpen(true)
  }

  const handleCancel = () => {
    reset()
    setEditingId(null)
    setIsFormOpen(false)
  }

  return (
    <Layout>
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h2 className="text-3xl font-bold tracking-tight">Giao dịch</h2>
            <p className="text-muted-foreground">Quản lý thu chi của bạn</p>
          </div>
          <Button onClick={() => setIsFormOpen(true)}>
            <Plus className="w-4 h-4 mr-2" />
            Thêm giao dịch
          </Button>
        </div>

        {isFormOpen && (
          <Card>
            <CardHeader>
              <CardTitle>{editingId ? 'Sửa giao dịch' : 'Thêm giao dịch mới'}</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Số tiền</label>
                    <Input
                      type="number"
                      step="0.01"
                      {...register('amount', { valueAsNumber: true })}
                    />
                    {errors.amount && (
                      <p className="text-sm text-destructive">{errors.amount.message}</p>
                    )}
                  </div>

                  <div className="space-y-2">
                    <label className="text-sm font-medium">Ngày</label>
                    <Input type="date" {...register('transactionDate')} />
                    {errors.transactionDate && (
                      <p className="text-sm text-destructive">{errors.transactionDate.message}</p>
                    )}
                  </div>
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Danh mục</label>
                  <select
                    {...register('categoryId')}
                    className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                  >
                    <option value="">Chọn danh mục</option>
                    {categories?.map((cat) => (
                      <option key={cat.id} value={cat.id}>
                        {cat.icon} {cat.name} ({cat.type === 'INCOME' ? 'Thu' : 'Chi'})
                      </option>
                    ))}
                  </select>
                  {errors.categoryId && (
                    <p className="text-sm text-destructive">{errors.categoryId.message}</p>
                  )}
                </div>

                <div className="space-y-2">
                  <label className="text-sm font-medium">Mô tả</label>
                  <Input {...register('description')} placeholder="Mô tả giao dịch" />
                  {errors.description && (
                    <p className="text-sm text-destructive">{errors.description.message}</p>
                  )}
                </div>

                <div className="flex gap-2">
                  <Button type="submit">
                    {editingId ? 'Cập nhật' : 'Thêm'}
                  </Button>
                  <Button type="button" variant="outline" onClick={handleCancel}>
                    Hủy
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}

        <Card>
          <CardContent className="p-0">
            {isLoading ? (
              <div className="p-8 text-center">Đang tải...</div>
            ) : (
              <div className="divide-y">
                {transactions?.content.map((transaction) => (
                  <div key={transaction.id} className="p-4 flex items-center justify-between hover:bg-muted/50">
                    <div className="flex items-center space-x-4">
                      <div
                        className="w-12 h-12 rounded-full flex items-center justify-center text-2xl"
                        style={{ backgroundColor: transaction.category.color + '20' }}
                      >
                        {transaction.category.icon}
                      </div>
                      <div>
                        <p className="font-medium">{transaction.description}</p>
                        <p className="text-sm text-muted-foreground">
                          {transaction.category.name} • {formatDate(transaction.transactionDate)}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center gap-4">
                      <div
                        className={`text-lg font-semibold ${
                          transaction.category.type === 'INCOME' ? 'text-green-600' : 'text-red-600'
                        }`}
                      >
                        {transaction.category.type === 'INCOME' ? '+' : '-'}
                        {formatCurrency(transaction.amount)}
                      </div>
                      <div className="flex gap-2">
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={() => handleEdit(transaction)}
                        >
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={() => deleteMutation.mutate(transaction.id)}
                        >
                          <Trash2 className="w-4 h-4 text-destructive" />
                        </Button>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </Layout>
  )
}
