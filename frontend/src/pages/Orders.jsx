import { useQuery } from '@tanstack/react-query'
import api from '../services/api'

const statusColors = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  CONFIRMED: 'bg-blue-100 text-blue-700',
  SHIPPED: 'bg-purple-100 text-purple-700',
  DELIVERED: 'bg-green-100 text-green-700',
  CANCELLED: 'bg-red-100 text-red-700',
}

const Orders = () => {
  const { data: orders, isLoading } = useQuery({
    queryKey: ['orders'],
    queryFn: async () => {
      const { data } = await api.get('/orders')
      return data
    },
  })

  if (isLoading) return (
    <div className="text-center py-20 text-gray-400">Loading...</div>
  )

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">My Orders</h2>

      {orders?.length === 0 ? (
        <div className="text-center py-20 text-gray-400">No orders yet</div>
      ) : (
        <div className="space-y-4">
          {orders?.map((order) => (
            <div
              key={order.id}
              className="bg-white rounded-xl shadow-sm border border-gray-100 p-4"
            >
              <div className="flex justify-between items-center mb-3">
                <h3 className="font-semibold text-gray-800">
                  Order #{order.id}
                </h3>
                <span className={`text-xs px-3 py-1 rounded-full font-medium ${statusColors[order.status]}`}>
                  {order.status}
                </span>
              </div>

              <div className="space-y-1 mb-3">
                {order.items?.map((item) => (
                  <p key={item.id} className="text-sm text-gray-600">
                    {item.product.name} × {item.quantity} —
                    ₹{item.priceAtTime}
                  </p>
                ))}
              </div>

              <div className="flex justify-between items-center pt-3 border-t border-gray-100">
                <span className="text-sm text-gray-500">
                  {new Date(order.createdAt).toLocaleDateString('en-IN')}
                </span>
                <span className="font-bold text-blue-600">
                  ₹{order.totalAmount}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}

export default Orders