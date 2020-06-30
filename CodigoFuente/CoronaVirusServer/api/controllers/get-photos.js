module.exports = {


    friendlyName: 'Get photos',
  
  
    description: 'Get all photos',
  
  
    inputs: {
        
  
    },

    exits: {
        
    },
  
  
    fn: async function (inputs, exits) {

        var photos = await Photo.find();
        return exits.success({ photos: photos})


    }
  
  
};
  