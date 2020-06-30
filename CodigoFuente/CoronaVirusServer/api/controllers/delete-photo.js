module.exports = {


    friendlyName: 'Delete photos',
  
  
    description: 'Delete a photos',
  
  
    inputs: {
        
        imageName: {
            description: 'The name of the image',
            example: "2c6d12de-c60f-45bb-8f17-1225758689f2.jpg",
            required: true
        },
  
    },

    exits: {
        
    },
  
  
    fn: async function (inputs, exits) {

        var fs = require('fs');
        await Photo.destroy({ imageName: inputs.imageName });
        fs.unlinkSync('.tmp/public/uploads/photos/'+inputs.imageName);
        return exits.success();

    }
  
  
};
  