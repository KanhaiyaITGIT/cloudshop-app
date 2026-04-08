import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import api from '../services/api'

const Cart = () => {
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const { data: cartItems, isLoading } = useQuery({
    queryKey: ['cart'],
    queryFn: async () => {
      const { data } = await api.get('/cart')
      return data
    },
  })

  const removeMutation = useMutation({
    mutationFn: (id) => api.delete(`/cart/${id}`),
    onSuccess: () => queryClient.invalidateQueries(['cart']),
  })

  const total = cartItems?.reduce(
    (sum, item) => sum + item.priceAtTime * item.quantity, 0
  ) || 0

  const handleOrder = async () => {
    const address = prompt('Enter shipping address:')
    if (!address) return
    try {
      await api.post('/orders', { shippingAddress: address })
      queryClient.invalidateQueries(['cart'])
      alert('Order placed successfully!')
      navigate('/orders')
    } catch (err) {
      alert(err.response?.data?.error || 'Order failed')
    }
  }

  if (isLoading) return (
    <div className="text-center py-20 text-gray-400">Loading...</div>
  )

  return (
    <div className="max-w-3xl mx-auto px-4 py-8">
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Your Cart</h2>

      {cartItems?.length === 0 ? (
        <div className="text-center py-20 text-gray-400">Cart is empty</div>
      ) : (
        <>
          <div className="space-y-4 mb-6">
            {cartItems?.map((item) => (
              <div
                key={item.id}
                className="bg-white rounded-xl shadow-sm border border-gray-100 p-4 flex justify-between items-center"
              >
                <div>
                  <h3 className="font-semibold text-gray-800">
                    {item.product.name}
                  </h3>
                  <p className="text-sm text-gray-500">
                    Qty: {item.quantity} × ₹{item.priceAtTime}
                  </p>
                </div>
                <div className="flex items-center gap-4">
                  <span className="font-bold text-blue-600">
                    ₹{(item.priceAtTime * item.quantity).toFixed(2)}
                  </span>
                  <button
                    onClick={() => removeMutation.mutate(item.id)}
                    className="text-red-500 hover:text-red-700 text-sm"
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>

          <div className="bg-white rounded-xl shadow-sm border border-gray-100 p-4">
            <div className="flex justify-between items-center mb-4">
              <span className="font-bold text-gray-800 text-lg">Total</span>
              <span className="font-bold text-blue-600 text-xl">
                ₹{total.toFixed(2)}
              </span>
            </div>
            <button
              onClick={handleOrder}
              className="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700 font-semibold"
            >
              Place Order
            </button>
          </div>
        </>
      )}
    </div>
  )
}

export default Cart