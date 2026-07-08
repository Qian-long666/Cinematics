import { movieApi } from '@/api/movieApi'

export const movieService = {
    async getMovieById(id) {
        if (!id) throw new Error('ID不能为空')
        return await movieApi.getById(id)
    },

    async addMovie(data) {
        if (!data.name) throw new Error('电影名称不能为空')
        return await movieApi.add(data)
    },

    async updateMovie(id, data) {
        if (!id) throw new Error('ID不能为空')
        const payload = {}
        if (data.name) payload.name = data.name
        if (data.time) payload.time = data.time
        if (data.type) payload.type = data.type
        if (data.director) payload.director = data.director
        if (data.actor) payload.actor = data.actor
        if (data.nation) payload.nation = data.nation
        return await movieApi.update(id, payload)
    },

    async deleteMovie(id) {
        if (!id) throw new Error('ID不能为空')
        return await movieApi.delete(id)
    }
}