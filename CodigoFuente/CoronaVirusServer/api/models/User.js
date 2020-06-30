module.exports = {
    attributes: {
        userName: {
            type: 'string',
            required: true,
            unique: true,
            example: 'admin'
        },
        password: {
            type: 'string',
            required: true,
            example: '1234'
        }
    }
}