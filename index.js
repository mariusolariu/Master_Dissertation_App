const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.createUserInfo = functions.auth.user().onCreate((user) => {
	admin.database().ref("users/" + user.uid + "/userInfo/email").set(user.email);
	admin.database().ref("users/" + user.uid + "/userInfo/admin").set(false);
});

exports.deleteUserInfo = functions.auth.user().onDelete( (user) => {
	admin.database().ref("users/" + user.uid).set(null);
});
