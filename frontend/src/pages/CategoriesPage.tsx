import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'
import { categoryService } from '@/services/category.service'
import { Layout } from '@/components/Layout'
import { Button } from '@/components/ui/Button'
import { Input } from '@/components/ui/Input'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card'
import { Plus, Trash2, Edit } from 'lucide-react'
import type { Category } from '@/types'

export function CategoriesPage() {
  const [isFormOpen, setIsFormOpen] = useState(false)
  const [editingId, setEditingId] = useState<string | null>(null)
  const queryClient = useQueryClient()

  const { data: categories, isLoading } = useQuery({
    queryKey: ['categories'],
    queryFn: categoryService.getAll,
  })

  const { register, handleSubmit, reset, setValue } = useForm<Omit<Category, 'id'>>()

  const createMutation = useMutation({
    mutationFn: categoryService.create,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] })
      reset()
      setIsFormOpen(false)
    },
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }: { id: string; data: Partial<Category> }) =>
      categoryService.update(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] })
      reset()
      setEditingId(null)
      setIsFormOpen(false)
    },
  })

  const deleteMutation = useMutation({
    mutationFn: categoryService.delete,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['categories'] })
    },
  })

  const onSubmit = (data: Omit<Category, 'id'>) => {
    if (editingId) {
      updateMutation.mutate({ id: editingId, data })
    } else {
      createMutation.mutate(data)
    }
  }

  const handleEdit = (category: Category) => {
    setEditingId(category.id)
    setValue('name', category.name)
    setValue('type', category.type)
    setValue('icon', category.icon)
    setValue('color', category.color)
    setIsFormOpen(true)
  }

  const handleCancel = () => {
    reset()
    setEditingId(null)
    setIsFormOpen(false)
  }

  const incomeCategories = categories?.filter((c) => c.type === 'INCOME') || []
  const expenseCategories = categories?.filter((c) => c.type === 'EXPENSE') || []

  return (
    <Layout>
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h2 className="text-3xl font-bold tracking-tight">Danh mục</h2>
            <p className="text-muted-foreground">Quản lý danh mục thu chi</p>
          </div>
          <Button onClick={() => setIsFormOpen(true)}>
            <Plus className="w-4 h-4 mr-2" />
            Thêm danh mục
          </Button>
        </div>

        {isFormOpen && (
          <Card>
            <CardHeader>
              <CardTitle>{editingId ? 'Sửa danh mục' : 'Thêm danh mục mới'}</CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Tên danh mục</label>
                    <Input {...register('name')} placeholder="Ví dụ: Lương, Ăn uống" />
                  </div>

                  <div className="space-y-2">
                    <label className="text-sm font-medium">Loại</label>
                    <select
                      {...register('type')}
                      className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
                    >
                      <option value="INCOME">Thu nhập</option>
                      <option value="EXPENSE">Chi tiêu</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div className="space-y-2">
                    <label className="text-sm font-medium">Icon (emoji)</label>
                    <Input {...register('icon')} placeholder="💰" maxLength={2} />
                  </div>

                  <div className="space-y-2">
                    <label className="text-sm font-medium">Màu sắc</label>
                    <Input type="color" {...register('color')} />
                  </div>
                </div>

                <div className="flex gap-2">
                  <Button type="submit">{editingId ? 'Cập nhật' : 'Thêm'}</Button>
                  <Button type="button" variant="outline" onClick={handleCancel}>
                    Hủy
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        )}

        <div className="grid md:grid-cols-2 gap-6">
          <Card>
            <CardHeader>
              <CardTitle className="text-green-600">Thu nhập</CardTitle>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="text-center py-4">Đang tải...</div>
              ) : (
                <div className="space-y-2">
                  {incomeCategories.map((category) => (
                    <div
                      key={category.id}
                      className="flex items-center justify-between p-3 rounded-lg hover:bg-muted"
                    >
                      <div className="flex items-center space-x-3">
                        <div
                          className="w-10 h-10 rounded-full flex items-center justify-center text-xl"
                          style={{ backgroundColor: category.color + '20' }}
                        >
                          {category.icon}
                        </div>
                        <span className="font-medium">{category.name}</span>
                      </div>
                      <div className="flex gap-2">
                        <Button size="sm" variant="ghost" onClick={() => handleEdit(category)}>
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={() => deleteMutation.mutate(category.id)}
                        >
                          <Trash2 className="w-4 h-4 text-destructive" />
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle className="text-red-600">Chi tiêu</CardTitle>
            </CardHeader>
            <CardContent>
              {isLoading ? (
                <div className="text-center py-4">Đang tải...</div>
              ) : (
                <div className="space-y-2">
                  {expenseCategories.map((category) => (
                    <div
                      key={category.id}
                      className="flex items-center justify-between p-3 rounded-lg hover:bg-muted"
                    >
                      <div className="flex items-center space-x-3">
                        <div
                          className="w-10 h-10 rounded-full flex items-center justify-center text-xl"
                          style={{ backgroundColor: category.color + '20' }}
                        >
                          {category.icon}
                        </div>
                        <span className="font-medium">{category.name}</span>
                      </div>
                      <div className="flex gap-2">
                        <Button size="sm" variant="ghost" onClick={() => handleEdit(category)}>
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={() => deleteMutation.mutate(category.id)}
                        >
                          <Trash2 className="w-4 h-4 text-destructive" />
                        </Button>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </CardContent>
          </Card>
        </div>
      </div>
    </Layout>
  )
}
