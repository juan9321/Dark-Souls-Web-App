const express = require('express');
const router = express.Router();
const verifyToken = require('../middleware');

const firebaseAuthController = require('../controllers/firebase-auth-controller');
const BuildController = require('../controllers/buildController.js');


// Auth routes
router.post('/api/register', firebaseAuthController.registerUser);
router.post('/api/login', firebaseAuthController.loginUser);
router.post('/api/logout', verifyToken, firebaseAuthController.logoutUser);
router.post('/api/reset-password', verifyToken, firebaseAuthController.resetPassword);
router.post('/api/alter-password', verifyToken, firebaseAuthController.alterPassword);


// Authenticated Routes
router.get('/dashboard', verifyToken, BuildController.dashboard);
router.get('/profile', verifyToken, BuildController.profile);
router.get('/create/build', verifyToken, BuildController.createBuild);
router.post('/create/build', verifyToken, BuildController.storeBuild);
router.get('/read/:key', verifyToken, BuildController.readBuild);



module.exports = router;