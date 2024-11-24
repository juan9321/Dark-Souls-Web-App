const { 
  getAuth, 
  createUserWithEmailAndPassword, 
  signInWithEmailAndPassword, 
  signOut, 
  sendEmailVerification,
  sendPasswordResetEmail,
  admin,
} = require('../config/firebase');

const auth = getAuth();



class FirebaseAuthController 
{
  registerUser(req, res) 
  {
    const { email, password } = req.body;

    if (!email || !password) 
    {
      req.session.error = 'Campos Vázios!';

      return res.redirect("/register");
    }

    if(password.length < 6){ 
      req.session.error = 'A Senha deve ter no mínimo 6 caracteres!';

      return res.redirect("/login");
    }

    createUserWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        sendEmailVerification(auth.currentUser)
          .then(() => {
            const idToken = userCredential._tokenResponse.idToken;
            req.session.user = { email: userCredential.user.email };

            if (idToken) {
              res.cookie('access_token', idToken, {
                  httpOnly: true
              });

              req.session.success = 'Cadastro efetuado com Sucesso!';
              res.redirect("/dashboard");
            } 
            else {
              res.render("errors/error", {layout: "guest", codError: "500", textError: 'Erro no Servidor!'});
            }
          })
          .catch((error) => {
            console.error(error);
            res.render("errors/error", {layout: "guest", codError: "500", textError: 'Erro no Servidor!'});
          });
      })
      .catch((error) => {
        console.error("" + error);
        req.session.error = "Ocorreu um Erro ao Registrar o Bandido!";
        res.redirect("/register");
      });
  }



  loginUser(req, res) 
  {
    const { email, password } = req.body;

    if (!email || !password) {
      req.session.error = 'Campos Vázios!';

      return res.redirect("/login");
    }

    signInWithEmailAndPassword(auth, email, password)
      .then((userCredential) => { 
        const idToken = userCredential._tokenResponse.idToken;
        req.session.user = { email: userCredential.user.email };
        
        if (idToken) {
          res.cookie('access_token', idToken, {
              httpOnly: true
          });

          req.session.success = 'Login efetuado com Sucesso!';
          res.redirect("/dashboard");
        } else {
          res.render("errors/error", {layout: "guest", codError: "500", textError: 'Erro no Servidor!'});
        }
      })
      .catch((error) => {
        console.error("" + error);
        req.session.error = "Credenciais Incorretas!";
        res.redirect("/login");
      });
  }



  logoutUser(req, res) 
  {
    signOut(auth)
      .then(() => {
        res.clearCookie('access_token');
        req.session.success = 'Você saiu da sua conta!';
        res.redirect("/");
      })
      .catch((error) => {
        console.error(error);
        res.render("errors/error", {layout: "guest", codError: "500", textError: 'Erro no Servidor!'});
      });
  }



  resetPassword(req, res)
  {
    const { email } = req.body;

    if (!email ) {
      req.session.error = 'Campos Vázios!';

      return res.redirect("/");
    }

    sendPasswordResetEmail(auth, email)
      .then(() => {
        res.status(200).json({ message: "Password reset email sent successfully!" });
      })
      .catch((error) => {
        console.error(error);
        res.status(500).json({ codError: "Internal Server Error" });
      });
  }



  async alterPassword(req, res) 
  {
    const { password, password_confirmation } = req.body;

    if (!password || !password_confirmation) {
      req.session.error = 'Campos Vázios!';

      return res.redirect("/profile");
    }

    if(password !== password_confirmation){
      req.session.error = 'As senhas não Conferem!';

      return res.redirect("/profile");
    }

    try{
      await admin.auth().updateUser(req.user.uid, {
        password: password
      });

      req.session.success = 'Senha Alterada com Sucesso!';
    }
    catch(e){
      req.session.error = 'Ocorreu um Erro';
      console.error(e)
    }

    return res.redirect("/profile");
  }
}



module.exports = new FirebaseAuthController();