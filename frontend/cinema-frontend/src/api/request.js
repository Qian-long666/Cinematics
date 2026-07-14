import axios from 'axios'
import { getToken, clearAuth } from '@/auth.js'

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// ===== 请求拦截器：自动带 JWT Token =====
request.interceptors.request.use(
    (config) => {
        const token = getToken()
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        return config
    },
    (error) => Promise.reject(error)
)

// ===== 响应拦截器 =====
request.interceptors.response.use(
    (response) => {
        const res = response.data
        if (res.success === true) {
            return res.data
        } else {
            return Promise.reject(new Error(res.errorMsg || '请求失败'))
        }
    },
    (error) => {
        // 401 → Token 过期或无效，清空登录状态
        if (error.response && error.response.status === 401) {
            clearAuth()
            window.location.href = '/login'
        }
        console.error('请求异常:', error)
        return Promise.reject(error)
    }
)

export default request
