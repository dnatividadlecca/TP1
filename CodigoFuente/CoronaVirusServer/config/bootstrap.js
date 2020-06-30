/**
 * Seed Function
 * (sails.config.bootstrap)
 *
 * A function that runs just before your Sails app gets lifted.
 * > Need more flexibility?  You can also create a hook.
 *
 * For more information on seeding your app with fake data, check out:
 * https://sailsjs.com/config/bootstrap
 */

module.exports.bootstrap = async function() {

  if (await User.count() == 0) {
    await User.create({ userName: 'admin', password: await sails.helpers.passwords.hashPassword('1234') });
  }
  if (await CountryReport.count() == 0) {
    await CountryReport.createEach([
      { country: 'Afganistán', cases: 0, deaths: 0, recovered: 0, latitude: 33.9391098, longitude: 67.7099533 },
      { country: 'Albania', cases: 0, deaths: 0, recovered: 0, latitude: 41.1533318, longitude: 20.1683311 },
      { country: 'Alemania', cases: 0, deaths: 0, recovered: 0, latitude: 51.1656914, longitude: 10.4515257 },
      { country: 'Andorra', cases: 0, deaths: 0, recovered: 0, latitude: 42.5462456, longitude: 1.601554 },
      { country: 'Angola', cases: 0, deaths: 0, recovered: 0, latitude: -11.202692, longitude: 17.8738861 },
      { country: 'Antigua y Barbuda', cases: 0, deaths: 0, recovered: 0, latitude: 17.0608158, longitude: -61.7964287 },
      { country: 'Arabia Saudita', cases: 0, deaths: 0, recovered: 0, latitude: 23.8859425, longitude: 45.0791626 },
      { country: 'Argelia', cases: 0, deaths: 0, recovered: 0, latitude: 28.033886, longitude: 1.659626 },
      { country: 'Argentina', cases: 0, deaths: 0, recovered: 0, latitude: -38.4160957, longitude: -63.6166725 },
      { country: 'Armenia', cases: 0, deaths: 0, recovered: 0, latitude: 40.0690994, longitude: 45.0381889 },
      { country: 'Australia', cases: 0, deaths: 0, recovered: 0, latitude: -25.2743988, longitude: 133.7751312 },
      { country: 'Austria', cases: 0, deaths: 0, recovered: 0, latitude: 47.5162315, longitude: 14.5500717 },
      { country: 'Azerbaiyán', cases: 0, deaths: 0, recovered: 0, latitude: 40.1431046, longitude: 47.5769272 },
      { country: 'Bahamas', cases: 0, deaths: 0, recovered: 0, latitude: 25.0342808, longitude: -77.3962784 },
      { country: 'Bangladés', cases: 0, deaths: 0, recovered: 0, latitude: 23.6849937, longitude: 90.3563309 },
      { country: 'Barbados', cases: 0, deaths: 0, recovered: 0, latitude: 13.1938868, longitude: -59.5431976 },
      { country: 'Baréin', cases: 0, deaths: 0, recovered: 0, latitude: 25.9304142, longitude: 50.6377716 },
      { country: 'Bélgica', cases: 0, deaths: 0, recovered: 0, latitude: 50.5038872, longitude: 4.4699359 },
      { country: 'Belice', cases: 0, deaths: 0, recovered: 0, latitude: 17.1898766, longitude: -88.4976501 },
      { country: 'Benín', cases: 0, deaths: 0, recovered: 0, latitude: 9.3076897, longitude: 2.315834 },
      { country: 'Bielorrusia', cases: 0, deaths: 0, recovered: 0, latitude: 53.7098083, longitude: 27.9533882 },
      { country: 'Birmania', cases: 0, deaths: 0, recovered: 0, latitude: 21.9139652, longitude: 95.9562225 },
      { country: 'Bolivia', cases: 0, deaths: 0, recovered: 0, latitude: -16.2901535, longitude: -63.5886536 },
      { country: 'Bosnia y Herzegovina', cases: 0, deaths: 0, recovered: 0, latitude: 43.9158859, longitude: 17.6790752 },
      { country: 'Botsuana', cases: 0, deaths: 0, recovered: 0, latitude: -22.328474, longitude: 24.684866 },
      { country: 'Brasil', cases: 0, deaths: 0, recovered: 0, latitude: -14.2350044, longitude: -51.9252815 },
      { country: 'Brunéi', cases: 0, deaths: 0, recovered: 0, latitude: 4.5352769, longitude: 114.7276688 },
      { country: 'Bulgaria', cases: 0, deaths: 0, recovered: 0, latitude: 42.7338829, longitude: 25.4858303 },
      { country: 'Burkina Faso', cases: 0, deaths: 0, recovered: 0, latitude: 12.2383327, longitude: -1.5615931 },
      { country: 'Burundi', cases: 0, deaths: 0, recovered: 0, latitude: -3.3730559, longitude: 29.9188862 },
      { country: 'Bután', cases: 0, deaths: 0, recovered: 0, latitude: 27.5141621, longitude: 90.4336014 },
      { country: 'Cabo Verde', cases: 0, deaths: 0, recovered: 0, latitude: 16.0020828, longitude: -24.0131969 },
      { country: 'Camboya', cases: 0, deaths: 0, recovered: 0, latitude: 12.5656786, longitude: 104.9909668 },
      { country: 'Camerún', cases: 0, deaths: 0, recovered: 0, latitude: 7.3697219, longitude: 7.3697219 },
      { country: 'Canadá', cases: 0, deaths: 0, recovered: 0, latitude: 56.1303673, longitude: -106.3467712 },
      { country: 'Catar', cases: 0, deaths: 0, recovered: 0, latitude: 25.354826, longitude: 51.1838837 },
      { country: 'Chad', cases: 0, deaths: 0, recovered: 0, latitude: 15.4541664, longitude: 18.7322063 },
      { country: 'Chile', cases: 0, deaths: 0, recovered: 0, latitude: -35.675148, longitude: -71.5429688 },
      { country: 'China', cases: 0, deaths: 0, recovered: 0, latitude: 35.86166, longitude: 104.1953964 },
      { country: 'Chipre', cases: 0, deaths: 0, recovered: 0, latitude: 35.1264114, longitude: 33.4298592 },
      { country: 'Ciudad del Vaticano', cases: 0, deaths: 0, recovered: 0, latitude: 41.902916, longitude: 12.4533892 },
      { country: 'Colombia', cases: 0, deaths: 0, recovered: 0, latitude: 4.570868, longitude: -74.2973328 },
      { country: 'Comoras', cases: 0, deaths: 0, recovered: 0, latitude: -11.875001, longitude: 43.8722191 },
      { country: 'Corea del Norte', cases: 0, deaths: 0, recovered: 0, latitude: 40.3398514, longitude: 127.5100937 },
      { country: 'Corea del Sur', cases: 0, deaths: 0, recovered: 0, latitude: 35.9077568, longitude: 127.766922 },
      { country: 'Costa de Marfil', cases: 0, deaths: 0, recovered: 0, latitude: 7.539989, longitude: -5.54708 },
      { country: 'Costa Rica', cases: 0, deaths: 0, recovered: 0, latitude: 9.7489166, longitude: -83.7534256 },
      { country: 'Croacia', cases: 0, deaths: 0, recovered: 0, latitude: 45.0999985, longitude: 15.1999998 },
      { country: 'Cuba', cases: 0, deaths: 0, recovered: 0, latitude: 21.5217571, longitude: -77.7811661 },
      { country: 'Dinamarca', cases: 0, deaths: 0, recovered: 0, latitude: 56.2639198, longitude: 9.5017853 },
      { country: 'Dominica', cases: 0, deaths: 0, recovered: 0, latitude: 15.414999, longitude: -61.3709755 },
      { country: 'Ecuador', cases: 0, deaths: 0, recovered: 0, latitude: -1.831239, longitude: -78.183403 },
      { country: 'Egipto', cases: 0, deaths: 0, recovered: 0, latitude: 26.8205528, longitude: 30.8024979 },
      { country: 'El Salvador', cases: 0, deaths: 0, recovered: 0, latitude: 13.7941847, longitude: -88.8965302 },
      { country: 'Emiratos Árabes Unidos', cases: 0, deaths: 0, recovered: 0, latitude: 23.4240761, longitude: 53.8478165 },
      { country: 'Eritrea', cases: 0, deaths: 0, recovered: 0, latitude: 15.1793842, longitude: 39.7823334 },
      { country: 'Eslovaquia', cases: 0, deaths: 0, recovered: 0, latitude: 48.6690254, longitude: 19.6990242 },
      { country: 'Eslovenia', cases: 0, deaths: 0, recovered: 0, latitude: 46.1512413, longitude: 14.9954634 },
      { country: 'España', cases: 0, deaths: 0, recovered: 0, latitude: 40.4636688, longitude: -3.7492199 },
      { country: 'Estados Unidos', cases: 0, deaths: 0, recovered: 0, latitude: 37.0902405, longitude: -95.7128906 },
      { country: 'Estonia', cases: 0, deaths: 0, recovered: 0, latitude: 58.5952721, longitude: 25.013607 },
      { country: 'Etiopía', cases: 0, deaths: 0, recovered: 0, latitude: 9.1450005, longitude: 40.4896736 },
      { country: 'Filipinas', cases: 0, deaths: 0, recovered: 0, latitude: 12.8797207, longitude: 121.7740173 },
      { country: 'Finlandia', cases: 0, deaths: 0, recovered: 0, latitude: 61.9241104, longitude: 25.7481518 },
      { country: 'Fiyi', cases: 0, deaths: 0, recovered: 0, latitude: -16.5781937, longitude: 179.4144135 },
      { country: 'Francia', cases: 0, deaths: 0, recovered: 0, latitude: 46.2276382, longitude: 2.2137489 },
      { country: 'Gabón', cases: 0, deaths: 0, recovered: 0, latitude: -0.803689, longitude: 11.6094437 },
      { country: 'Gambia', cases: 0, deaths: 0, recovered: 0, latitude: 13.443182, longitude: -15.3101387 },
      { country: 'Georgia', cases: 0, deaths: 0, recovered: 0, latitude: 42.3154068, longitude: 43.3568916 },
      { country: 'Ghana', cases: 0, deaths: 0, recovered: 0, latitude: 7.946527, longitude: -1.023194 },
      { country: 'Granada', cases: 0, deaths: 0, recovered: 0, latitude: 12.2627764, longitude: -61.6041718 },
      { country: 'Grecia', cases: 0, deaths: 0, recovered: 0, latitude: 39.0742073, longitude: 21.8243122 },
      { country: 'Guatemala', cases: 0, deaths: 0, recovered: 0, latitude: 15.7834711, longitude: -90.2307587 },
      { country: 'Guyana', cases: 0, deaths: 0, recovered: 0, latitude: 4.8604159, longitude: -58.9301796 },
      { country: 'Guinea', cases: 0, deaths: 0, recovered: 0, latitude: 9.9455872, longitude: -9.6966448 },
      { country: 'Guinea Ecuatorial', cases: 0, deaths: 0, recovered: 0, latitude: 1.6508009, longitude: 10.2678947 },
      { country: 'Guinea-Bisáu', cases: 0, deaths: 0, recovered: 0, latitude: 11.8037491, longitude: -15.1804132 },
      { country: 'Haití', cases: 0, deaths: 0, recovered: 0, latitude: 18.9711876, longitude: -72.2852173 },
      { country: 'Honduras', cases: 0, deaths: 0, recovered: 0, latitude: 15.1999989, longitude: -86.2419052 },
      { country: 'Hungría', cases: 0, deaths: 0, recovered: 0, latitude: 47.1624947, longitude: 19.5033035 },
      { country: 'India', cases: 0, deaths: 0, recovered: 0, latitude: 20.5936832, longitude: 78.962883 },
      { country: 'Indonesia', cases: 0, deaths: 0, recovered: 0, latitude: -0.789275, longitude: 113.9213257 },
      { country: 'Irak', cases: 0, deaths: 0, recovered: 0, latitude: 33.2231903, longitude: 43.6792908 },
      { country: 'Irán', cases: 0, deaths: 0, recovered: 0, latitude: 32.4279099, longitude: 53.6880455 },
      { country: 'Irlanda', cases: 0, deaths: 0, recovered: 0, latitude: 53.4129105, longitude: -8.2438898 },
      { country: 'Islandia', cases: 0, deaths: 0, recovered: 0, latitude: 64.9630508, longitude: -19.0208359 },
      { country: 'Islas Marshall', cases: 0, deaths: 0, recovered: 0, latitude: 7.131474, longitude: 171.1844788 },
      { country: 'Islas Salomón', cases: 0, deaths: 0, recovered: 0, latitude: -9.64571, longitude: 160.156189 },
      { country: 'Israel', cases: 0, deaths: 0, recovered: 0, latitude: 31.046051, longitude: 34.8516121 },
      { country: 'Italia', cases: 0, deaths: 0, recovered: 0, latitude: 41.8719406, longitude: 12.56738 },
      { country: 'Jamaica', cases: 0, deaths: 0, recovered: 0, latitude: 18.109581, longitude: -77.2975082 },
      { country: 'Japón', cases: 0, deaths: 0, recovered: 0, latitude: 36.2048225, longitude: 138.2529297 },
      { country: 'Jordania', cases: 0, deaths: 0, recovered: 0, latitude: 30.5851631, longitude: 36.2384148 },
      { country: 'Kazajistán', cases: 0, deaths: 0, recovered: 0, latitude: 48.0195732, longitude: 66.9236832 },
      { country: 'Kenia', cases: 0, deaths: 0, recovered: 0, latitude: -0.023559, longitude: 37.9061928 },
      { country: 'Kirguistán', cases: 0, deaths: 0, recovered: 0, latitude: 41.20438, longitude: 74.766098 },
      { country: 'Kiribati', cases: 0, deaths: 0, recovered: 0, latitude: -3.3704171, longitude: -168.7340393 },
      { country: 'Kuwait', cases: 0, deaths: 0, recovered: 0, latitude: 29.3116608, longitude: 47.4817657 },
      { country: 'Laos', cases: 0, deaths: 0, recovered: 0, latitude: 19.8562698, longitude: 102.4954987 },
      { country: 'Lesoto', cases: 0, deaths: 0, recovered: 0, latitude: -29.6099873, longitude: 28.2336082 },
      { country: 'Letonia', cases: 0, deaths: 0, recovered: 0, latitude: 56.8796349, longitude: 24.6031895 },
      { country: 'Líbano', cases: 0, deaths: 0, recovered: 0, latitude: 33.8547211, longitude: 35.8622856 },
      { country: 'Liberia', cases: 0, deaths: 0, recovered: 0, latitude: 6.4280548, longitude: -9.4294987 },
      { country: 'Libia', cases: 0, deaths: 0, recovered: 0, latitude: 26.3351002, longitude: 17.2283306 },
      { country: 'Liechtenstein', cases: 0, deaths: 0, recovered: 0, latitude: 47.1660004, longitude: 9.5553732 },
      { country: 'Lituania', cases: 0, deaths: 0, recovered: 0, latitude: 55.1694374, longitude: 23.8812752 },
      { country: 'Luxemburgo', cases: 0, deaths: 0, recovered: 0, latitude: 49.8152733, longitude: 6.1295829 },
      { country: 'Macedonia del Norte', cases: 0, deaths: 0, recovered: 0, latitude: 41.6086349, longitude: 21.7452755 },
      { country: 'Madagascar', cases: 0, deaths: 0, recovered: 0, latitude: -18.7669468, longitude: 46.8691063 },
      { country: 'Malasia', cases: 0, deaths: 0, recovered: 0, latitude: 4.210484, longitude: 101.975769 },
      { country: 'Malaui', cases: 0, deaths: 0, recovered: 0, latitude: -13.2543077, longitude: 34.3015251 },
      { country: 'Maldivas', cases: 0, deaths: 0, recovered: 0, latitude: 3.2027781, longitude: 73.2206802 },
      { country: 'Malí', cases: 0, deaths: 0, recovered: 0, latitude: 17.5706921, longitude: -3.996166 },
      { country: 'Malta', cases: 0, deaths: 0, recovered: 0, latitude: 35.9374962, longitude: 14.3754158 },
      { country: 'Marruecos', cases: 0, deaths: 0, recovered: 0, latitude: 31.7917023, longitude: -7.0926199 },
      { country: 'Mauricio', cases: 0, deaths: 0, recovered: 0, latitude: -20.3484039, longitude: 57.5521507 },
      { country: 'Mauritania', cases: 0, deaths: 0, recovered: 0, latitude: 21.0078907, longitude: -10.940835 },
      { country: 'México', cases: 0, deaths: 0, recovered: 0, latitude: 23.6345005, longitude: -102.5527878 },
      { country: 'Micronesia', cases: 0, deaths: 0, recovered: 0, latitude: 7.4255538, longitude: 150.5508118 },
      { country: 'Moldavia', cases: 0, deaths: 0, recovered: 0, latitude: 47.4116325, longitude: 28.3698845 },
      { country: 'Mónaco', cases: 0, deaths: 0, recovered: 0, latitude: 43.7502975, longitude: 7.4128408 },
      { country: 'Mongolia', cases: 0, deaths: 0, recovered: 0, latitude: 46.8624954, longitude: 103.8466568 },
      { country: 'Montenegro', cases: 0, deaths: 0, recovered: 0, latitude: 42.7086792, longitude: 19.3743896 },
      { country: 'Mozambique', cases: 0, deaths: 0, recovered: 0, latitude: -18.6656952, longitude: 35.5295639 },
      { country: 'Namibia', cases: 0, deaths: 0, recovered: 0, latitude: -22.9576397, longitude: 18.4904099 },
      { country: 'Nauru', cases: 0, deaths: 0, recovered: 0, latitude: -0.522778, longitude: 166.9315033 },
      { country: 'Nepal', cases: 0, deaths: 0, recovered: 0, latitude: 28.3948574, longitude: 84.1240082 },
      { country: 'Nicaragua', cases: 0, deaths: 0, recovered: 0, latitude: 12.8654156, longitude: -85.2072296 },
      { country: 'Níger', cases: 0, deaths: 0, recovered: 0, latitude: 17.6077881, longitude: 8.081666 },
      { country: 'Nigeria', cases: 0, deaths: 0, recovered: 0, latitude: 9.0819988, longitude: 08.6752768 },
      { country: 'Noruega', cases: 0, deaths: 0, recovered: 0, latitude: 60.472023, longitude: 8.4689465 },
      { country: 'Nueva Zelanda', cases: 0, deaths: 0, recovered: 0, latitude: -40.9005585, longitude: 174.8859711 },
      { country: 'Omán', cases: 0, deaths: 0, recovered: 0, latitude: 21.5125828, longitude: 55.9232559 },
      { country: 'Países Bajos', cases: 0, deaths: 0, recovered: 0, latitude: 52.1326332, longitude: 5.291266 },
      { country: 'Pakistán', cases: 0, deaths: 0, recovered: 0, latitude: 30.3753204, longitude: 69.3451157 },
      { country: 'Palaos', cases: 0, deaths: 0, recovered: 0, latitude: 7.5149798, longitude: 134.5825195 },
      { country: 'Panamá', cases: 0, deaths: 0, recovered: 0, latitude: 8.537981, longitude: -80.7821274 },
      { country: 'Papúa Nueva Guinea', cases: 0, deaths: 0, recovered: 0, latitude: -6.3149929, longitude: 143.9555511 },
      { country: 'Paraguay', cases: 0, deaths: 0, recovered: 0, latitude: -23.442503, longitude: -58.4438324 },
      { country: 'Perú', cases: 0, deaths: 0, recovered: 0, latitude: -9.1899672, longitude: -75.015152 },
      { country: 'Polonia', cases: 0, deaths: 0, recovered: 0, latitude: 51.9194374, longitude: 19.1451359 },
      { country: 'Portugal', cases: 0, deaths: 0, recovered: 0, latitude: 39.3998718, longitude: -8.2244539 },
      { country: 'Reino Unido', cases: 0, deaths: 0, recovered: 0, latitude: 55.3780518, longitude: -3.4359729 },
      { country: 'República Centroafricana', cases: 0, deaths: 0, recovered: 0, latitude: 6.6111112, longitude: 20.9394436 },
      { country: 'República Checa', cases: 0, deaths: 0, recovered: 0, latitude: 49.8174934, longitude: 15.4729624 },
      { country: 'República del Congo', cases: 0, deaths: 0, recovered: 0, latitude: -0.228021, longitude: 15.8276587 },
      { country: 'República Democrática del Congo', cases: 0, deaths: 0, recovered: 0, latitude: -4.0383329, longitude: 21.7586632 },
      { country: 'República Dominicana', cases: 0, deaths: 0, recovered: 0, latitude: 18.735693, longitude: -70.1626511 },
      { country: 'República Sudafricana', cases: 0, deaths: 0, recovered: 0, latitude: -30.5594826, longitude: 22.9375057 },
      { country: 'Ruanda', cases: 0, deaths: 0, recovered: 0, latitude: -1.9402781, longitude: 29.873888 },
      { country: 'Rumanía', cases: 0, deaths: 0, recovered: 0, latitude: 45.943161, longitude: 24.9667606 },
      { country: 'Rusia', cases: 0, deaths: 0, recovered: 0, latitude: 61.5240097, longitude: 105.3187561 },
      { country: 'Samoa', cases: 0, deaths: 0, recovered: 0, latitude: -13.7590294, longitude: -172.1046295 },
      { country: 'San Cristóbal y Nieves', cases: 0, deaths: 0, recovered: 0, latitude: 17.3578224, longitude: -62.7829971 },
      { country: 'San Marino', cases: 0, deaths: 0, recovered: 0, latitude: 43.9423599, longitude: 12.457777},
      { country: 'San Vicente y las Granadinas', cases: 0, deaths: 0, recovered: 0, latitude: 12.9843054, longitude: -61.2872276 },
      { country: 'Santa Lucía', cases: 0, deaths: 0, recovered: 0, latitude: 13.9094439, longitude: -60.9788933 },
      { country: 'Santo Tomé y Príncipe', cases: 0, deaths: 0, recovered: 0, latitude: 0.18636, longitude: 6.613081 },
      { country: 'Senegal', cases: 0, deaths: 0, recovered: 0, latitude: 14.4974012, longitude: -14.4523621 },
      { country: 'Serbia', cases: 0, deaths: 0, recovered: 0, latitude: 44.0165215, longitude: 21.0058594 },
      { country: 'Seychelles', cases: 0, deaths: 0, recovered: 0, latitude: -4.679574, longitude: 55.4919777 },
      { country: 'Sierra Leona', cases: 0, deaths: 0, recovered: 0, latitude: 8.4605551, longitude: -11.7798891 },
      { country: 'Singapur', cases: 0, deaths: 0, recovered: 0, latitude: 1.352083, longitude: 103.8198395 },
      { country: 'Siria', cases: 0, deaths: 0, recovered: 0, latitude: 34.8020744, longitude: 38.9968147 },
      { country: 'Somalia', cases: 0, deaths: 0, recovered: 0, latitude: 5.1521492, longitude: 46.1996155 },
      { country: 'Sri Lanka', cases: 0, deaths: 0, recovered: 0, latitude: 7.873054, longitude: 80.7717972 },
      { country: 'Suazilandia', cases: 0, deaths: 0, recovered: 0, latitude: -26.5225029, longitude: 31.4658661 },
      { country: 'Sudán', cases: 0, deaths: 0, recovered: 0, latitude: 12.8628073, longitude: 30.2176361 },
      { country: 'Sudán del Sur', cases: 0, deaths: 0, recovered: 0, latitude: 6.8769908, longitude: 31.3069782 },
      { country: 'Suecia', cases: 0, deaths: 0, recovered: 0, latitude: 60.1281624, longitude: 18.6435013 },
      { country: 'Suiza', cases: 0, deaths: 0, recovered: 0, latitude: 46.8181877, longitude: 8.2275124 },
      { country: 'Surinam', cases: 0, deaths: 0, recovered: 0, latitude: 3.9193051, longitude: -56.0277824 },
      { country: 'Tailandia', cases: 0, deaths: 0, recovered: 0, latitude: 15.8700323, longitude: 100.9925385 },
      { country: 'Tanzania', cases: 0, deaths: 0, recovered: 0, latitude: -6.3690281, longitude: 34.8888206 },
      { country: 'Tayikistán', cases: 0, deaths: 0, recovered: 0, latitude: 38.8610344, longitude: 71.2760925 },
      { country: 'Timor Oriental', cases: 0, deaths: 0, recovered: 0, latitude: -8.874217, longitude: 125.7275391 },
      { country: 'Togo', cases: 0, deaths: 0, recovered: 0, latitude: 8.6195431, longitude: 0.824782 },
      { country: 'Tonga', cases: 0, deaths: 0, recovered: 0, latitude: -21.1789856, longitude: -175.1982422 },
      { country: 'Trinidad y Tobago', cases: 0, deaths: 0, recovered: 0, latitude: 10.691803, longitude: -61.2225037 },
      { country: 'Túnez', cases: 0, deaths: 0, recovered: 0, latitude: 33.8869171, longitude: 9.5374994 },
      { country: 'Turkmenistán', cases: 0, deaths: 0, recovered: 0, latitude: 38.9697189, longitude: 59.5562782 },
      { country: 'Turquía', cases: 0, deaths: 0, recovered: 0, latitude: 38.9637451, longitude: 35.2433205 },
      { country: 'Tuvalu', cases: 0, deaths: 0, recovered: 0, latitude: -7.1095352, longitude: 177.6493225 },
      { country: 'Ucrania', cases: 0, deaths: 0, recovered: 0, latitude: 48.3794327, longitude: 31.1655807 },
      { country: 'Uganda', cases: 0, deaths: 0, recovered: 0, latitude: 1.373333, longitude: 32.2902756 },
      { country: 'Uruguay', cases: 0, deaths: 0, recovered: 0, latitude: -32.5227776, longitude: -55.7658348 },
      { country: 'Uzbekistán', cases: 0, deaths: 0, recovered: 0, latitude: 41.377491, longitude: 64.5852585 },
      { country: 'Vanuatu', cases: 0, deaths: 0, recovered: 0, latitude: -15.3767061, longitude: 166.9591522 },
      { country: 'Venezuela', cases: 0, deaths: 0, recovered: 0, latitude: 6.4237499, longitude: -66.5897293 },
      { country: 'Vietnam', cases: 0, deaths: 0, recovered: 0, latitude: 14.0583239, longitude: 108.2771988 },
      { country: 'Yemen', cases: 0, deaths: 0, recovered: 0, latitude: 15.5527267, longitude: 48.5163879 },
      { country: 'Yibuti', cases: 0, deaths: 0, recovered: 0, latitude: 11.8251381, longitude: 42.5902748 },
      { country: 'Zambia', cases: 0, deaths: 0, recovered: 0, latitude: -13.1338968, longitude: 27.8493328 },
      { country: 'Zimbabue', cases: 0, deaths: 0, recovered: 0, latitude: -19.0154381, longitude: 29.1548576 }
    ])
  }
  
  // By convention, this is a good place to set up fake data during development.
  //
  // For example:
  // ```
  // // Set up fake development data (or if we already have some, avast)
  // if (await User.count() > 0) {
  //   return;
  // }
  //
  // await User.createEach([
  //   { emailAddress: 'ry@example.com', fullName: 'Ryan Dahl', },
  //   { emailAddress: 'rachael@example.com', fullName: 'Rachael Shaw', },
  //   // etc.
  // ]);
  // ```
  const ifaces = require('os').networkInterfaces();
  let address;
  Object.keys(ifaces).forEach(dev => {
    ifaces[dev].filter(details => {
      if (details.family === 'IPv4' && details.internal === false) {
        address = details.address;
      }
    });
  });
  sails.log('IP          : ' + address + ':' + sails.config.port );
  sails.log('USUARIO     : ' + 'admin' );
  sails.log('CONTRASEÑA  : ' + '1234' );

};
