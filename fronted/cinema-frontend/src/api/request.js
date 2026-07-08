import axios from 'axios'

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 30000,
    headers: {
        'Content-Type': 'application/json'
    }
})

// 响应拦截器
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
        console.error('请求异常:', error)
        return Promise.reject(error)
    }
)

export default request