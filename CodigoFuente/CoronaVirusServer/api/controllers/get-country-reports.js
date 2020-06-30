module.exports = {


    friendlyName: 'Get country report',
  
  
    description: 'Get all country report',
  
  
    inputs: {
        
  
    },

    exits: {
        
    },
  
  
    fn: async function (inputs, exits) {

        var countryReports = await CountryReport.find().sort('country ASC')
        return exits.success({ countryReports: countryReports})

    }
  
  
};
