/**
 * UploadImageController
 *
 * @description :: Server-side actions for handling incoming requests.
 * @help        :: See https://sailsjs.com/docs/concepts/actions
 */


module.exports = {

    upload: function  (req, res) {
        req.file('photo').upload({
            dirname: '../../.tmp/public/uploads/photos'
        }, async function (err, files) {
            if (err) {
                return res.serverError(err);
            } else {
                await Photo.create({imageName: files[0].fd.split("\\").pop()});
                return res.ok()
            }
        });
    }
};

