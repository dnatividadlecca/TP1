module.exports = {


    friendlyName: 'Update country report',
  
  
    description: 'Update a country report',
  
  
    inputs: {
        
        id: {
            description: 'The ID of the country',
            example: "140",
            unique: true,
            required: true
        },
        cases: {
            description: 'The number of cases',
            example: 1000,
            required: true
        },
        deaths: {
            description: 'The number of deaths',
            example: 100,
            required: true
        },
        recovered: {
            description: 'The number of recovered',
            example: 10,
            required: true
        }

    },

    exits: {
        
    },
  
  
    fn: async function (inputs, exits) {

        if(inputs.cases<0 || inputs.deaths<0 || inputs.recovered<0 || !Number.isInteger(inputs.cases) || !Number.isInteger(inputs.deaths) || !Number.isInteger(inputs.recovered)) {
            return exits.success('invalidData')
        }
        await CountryReport.updateOne({ id: inputs.id })
            .set({ cases: inputs.cases, deaths: inputs.deaths, recovered: inputs.recovered })
        return exits.success('validData');


    }
  
  
  };
  