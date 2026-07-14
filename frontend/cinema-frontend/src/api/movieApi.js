import request from './request.js'

export const movieApi = {
    // ===== 认证 =====
    login(username, password) {
        return request({
            url: '/api/auth/login',
            method: 'post',
            data: { username, password }
        })
    },

    register(username, password) {
        return request({
            url: '/api/auth/register',
            method: 'post',
            data: { username, password }
        })
    },

    getMe() {
        return request({
            url: '/api/auth/me',
            method: 'get'
        })
    },

    // ===== 电影 CRUD =====
    getById(id) {
        return request({
            url: `/movie/${id}`,
            method: 'get'
        })
    },

    add(data) {
        return request({
            url: '/movie',
            method: 'post',
            data
        })
    },

    update(id, data) {
        return request({
            url: `/movie/${id}`,
            method: 'put',
            data
        })
    },

    delete(id) {
        return request({
            url: `/movie/${id}`,
            method: 'delete'
        })
    },

    searchMovies(params) {
        return request({
            url: '/movies/search',
            method: 'get',
            params
        })
    },

    // ===== 评论 =====
    addComment(data) {
        return request({
            url: '/api/comments',
            method: 'post',
            data
        })
    },

    getCommentsByMovie(movieId) {
        return request({
            url: `/api/comments/movie/${movieId}`,
            method: 'get'
        })
    },

    getMyComments() {
        return request({
            url: '/api/comments/my',
            method: 'get'
        })
    },

    deleteComment(id) {
        return request({
            url: `/api/comments/${id}`,
            method: 'delete'
        })
    },


}
