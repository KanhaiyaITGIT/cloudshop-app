import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'
import api from '../services/api'
import useAuthStore from '../store/authStore'

const Home = () => {
  const { user } = useAuthStore()
  const navigate = useNavigate()
  const [search, setSearch] = useState('')
  const [category, setCategory] = useState('')

  const { data: products, isLoading } = useQuery({
    queryKey: ['products', search, category],
    queryFn: async () => {
      if (search) {
        const { data } = await api.get(`/products/search?keyword=${search}`)
        return data
      }
      if (category) {
        const { data } = await api.get(`/products/category/${category}`)
        return data
      }
      const { data } = await api.get('/products')
      return data
    },
  })

  const addToCart = async (productId) => {
    if (!user) {
      navigate('/login')
      return
    }
    try {
      await api.post(`/cart/add?productId=${productId}&quantity=1`)
      alert('Added to cart!')
    } catch (err) {
      alert(err.response?.data?.error || 'Failed to add')
    }
  }

  return (
    <div className="max-w-7xl mx-auto px-4 py-8">
      {/* Search + Filter */}
      <div className="flex gap-4 mb-8">
        <input
          type="text"
          placeholder="Search products..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="flex-1 border border-gray-300 rounded-lg px-4 py-2 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
        <select
          value={category}
          onChange={(e) => setCategory(e.target.value)}
          className="border border-gray-300 rounded-lg px-4 py-2 focus:outline-none"
        >
          <option value="">All Categories</option>
          <option value="Electronics">Electronics</option>
          <option value="Clothing">Clothing</option>
          <option value="Books">Books</option>
          <option value="Home">Home</option>
        </select>
      </div>

      {/* Products Grid */}
      {isLoading ? (
        <div className="text-center py-20 text-gray-400">Loading...</div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {products?.map((product) => (
            <div
              key={product.id}
              className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden hover:shadow-md transition"
            >
              <div className="h-48 bg-gray-100 flex items-center justify-center">
                {product.imageUrl ? (
                  <img
                    src={product.imageUrl}
                    alt={product.name}
                    className="h-full w-full object-cover"
                  />
                ) : (
                  <span className="text-gray-400 text-sm">No Image</span>
                )}
              </div>
              <div className="p-4">
                <h3 className="font-semibold text-gray-800 mb-1">
                  {product.name}
                </h3>
                <p className="text-sm text-gray-500 mb-2">{product.category}</p>
                <p className="text-blue-600 font-bold mb-3">
                  ₹{product.price}
                </p>
                <button
                  onClick={() => addToCart(product.id)}
                  className="w-full bg-blue-600 text-white py-2 rounded-lg hover:bg-blue-700 text-sm"
                >
                  Add to Cart
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {products?.length === 0 && (
        <div className="text-center py-20 text-gray-400">
          No products found
        </div>
      )}
    </div>
  )
}

export default Home