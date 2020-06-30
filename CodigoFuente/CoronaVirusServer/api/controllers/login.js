module.exports = {


    friendlyName: 'Verify user',
  
  
    description: 'Verifies a category',
  
  
    inputs: {

        userName: {
            description: 'The name of the category',
            example: "Embutidos",
            unique: true,
            required: true
        },
        password: {
            description: 'The image name of the category',
            example: "abcdefghi.jpg",
            required: false
        }

    },

    exits: {
        
    },
  
  
    fn: async function (inputs, exits) {

        var userRecord = await User.findOne({
            userName: inputs.userName,
        });
        if(!userRecord) {
            return exits.success('unauthorized')
        }
        try {
            await sails.helpers.passwords.checkPassword(inputs.password, userRecord.password);
            return exits.success('authorized')
        } catch (error) {
            return exits.success('unauthorized')
        }

    }
  
  
  };
  