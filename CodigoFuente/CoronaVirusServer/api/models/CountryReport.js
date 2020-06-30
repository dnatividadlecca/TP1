module.exports = {
    attributes: {
        country: {
            type: 'string',
            required: true,
            unique: true,
            example: 'Perú'
        },
        cases: {
            type: 'number',
            required: true,
            example: '80894'
        },
        deaths: {
            type: 'number',
            required: true,
            example: '3237'
        },
        recovered: {
            type: 'number',
            required: true,
            example: '69614'
        },
        latitude: {
            type: 'number',
            required: true,
            example: -9
        },
        longitude: {
            type: 'number',
            required: true,
            example: 75
        }
    }
}