import { useQuery } from '@tanstack/react-query'
import { transactionService } from '@/services/transaction.service'
import { budgetService } from '@/services/budget.service'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card'
import { formatCurrency } from '@/lib/utils'
import { TrendingUp, TrendingDown, Wallet, PiggyBank } from 'lucide-react'
import { Layout } from '@/components/Layout'

export function DashboardPage() {
  const currentMonth = new Date().getMonth() + 1
  const currentYear = new Date().getFullYear()

  const { data: transactions } = useQuery({
    queryKey: ['transactions'],
    queryFn: () => transactionService.getAll(0, 100),
  })

  const { data: budgets } = useQuery({
    queryKey: ['budgets', currentMonth, currentYear],
    queryFn: () => budgetService.getByMonth(currentMonth, currentYear),
  })

  const income = transactions?.content
    .filter((t) => t.category.type === 'INCOME')
    .reduce((sum, t) => sum + t.amount, 0) || 0

  const expense = transactions?.content
    .filter((t) => t.category.type === 'EXPENSE')
    .reduce((sum, t) => sum + t.amount, 0) || 0

  const balance = income - expense

  const totalBudget = budgets?.reduce((sum, b) => sum + b.amount, 0) || 0
  const totalSpent = budgets?.reduce((sum, b) => sum + b.actualSpent, 0) || 0

  return (
    <Layout>
      <div className="space-y-6">
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Dashboard</h2>
          <p className="text-muted-foreground">Tổng quan tài chính của bạn</p>
        </div>

        <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Số dư</CardTitle>
              <Wallet className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{formatCurrency(balance)}</div>
              <p className="text-xs text-muted-foreground">Tổng thu - Tổng chi</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Thu nhập</CardTitle>
              <TrendingUp className="h-4 w-4 text-green-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-green-600">{formatCurrency(income)}</div>
              <p className="text-xs text-muted-foreground">Tháng này</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Chi tiêu</CardTitle>
              <TrendingDown className="h-4 w-4 text-red-600" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-red-600">{formatCurrency(expense)}</div>
              <p className="text-xs text-muted-foreground">Tháng này</p>
            </CardContent>
          </Card>

          <Card>
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
              <CardTitle className="text-sm font-medium">Ngân sách</CardTitle>
              <PiggyBank className="h-4 w-4 text-muted-foreground" />
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold">{formatCurrency(totalSpent)}</div>
              <p className="text-xs text-muted-foreground">
                / {formatCurrency(totalBudget)}
              </p>
            </CardContent>
          </Card>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>Giao dịch gần đây</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {transactions?.content.slice(0, 5).map((transaction) => (
                <div key={transaction.id} className="flex items-center justify-between">
                  <div className="flex items-center space-x-4">
                    <div
                      className="w-10 h-10 rounded-full flex items-center justify-center text-xl"
                      style={{ backgroundColor: transaction.category.color + '20' }}
                    >
                      {transaction.category.icon}
                    </div>
                    <div>
                      <p className="font-medium">{transaction.description}</p>
                      <p className="text-sm text-muted-foreground">
                        {transaction.category.name}
                      </p>
                    </div>
                  </div>
                  <div
                    className={`font-semibold ${
                      transaction.category.type === 'INCOME'
                        ? 'text-green-600'
                        : 'text-red-600'
                    }`}
                  >
                    {transaction.category.type === 'INCOME' ? '+' : '-'}
                    {formatCurrency(transaction.amount)}
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    </Layout>
  )
}
