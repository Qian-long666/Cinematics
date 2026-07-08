import request from './request.js'

export const movieApi = {
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
    }
}